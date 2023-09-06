package br.net.pin.qin_sunwiz.flow;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import br.net.pin.qin_sunwiz.data.DataLink;
import br.net.pin.qin_sunwiz.data.Field;
import br.net.pin.qin_sunwiz.data.Insert;
import br.net.pin.qin_sunwiz.data.Registier;
import br.net.pin.qin_sunwiz.data.Registry;
import br.net.pin.qin_sunwiz.data.Table;
import br.net.pin.qin_sunwiz.data.Valued;
import br.net.pin.qin_sunwiz.mage.WizFile;

public class CSVImport implements Runnable {

    private final File origin;
    private final DataLink destiny;
    private final Pace pace;

    public CSVImport(File origin, DataLink destiny) {
        this(origin, destiny, null);
    }

    public CSVImport(File origin, DataLink destiny, Pace pace) {
        this.origin = origin;
        this.destiny = destiny;
        this.pace = pace != null ? pace
                        : new Pace(LoggerFactory.getLogger(CSVImport.class));
    }

    public void run() {
        try {
            if (!origin.exists()) {
                throw new Exception("The origin must exist.");
            }
            try (var connection = destiny.connect()) {
                pace.info("Connected to destiny database.");
                if (origin.isFile()) {
                    importCSVFile(origin, connection);
                } else {
                    for (File inside : origin.listFiles()) {
                        if (isCSVFile(inside)) {
                            importCSVFile(inside, connection);
                        }
                    }
                }
            }
            pace.info("Finished to import from CSV.");
        } catch (Exception e) {
            pace.error("Could not import", e);
        }
    }

    private boolean isCSVFile(File file) {
        return file.isFile() && file.getName().toLowerCase().endsWith(".csv");
    }

    private void importCSVFile(File csvFile, Connection connection) throws Exception {
        pace.waitIfPausedAndThrowIfStopped();
        pace.info("Importing CSV File: " + csvFile.getName());
        var tableName = WizFile.getBaseName(csvFile.getName());
        var tableFile = new File(csvFile.getParent(), tableName + ".tab");
        Table table;
        if (tableFile.exists()) {
            pace.info("Loading table metadata from file.");
            table = Table.fromString(Files.readString(tableFile.toPath()));
            destiny.base.helper.create(connection, table, true);
        } else {
            pace.info("Loading table metadata from connection.");
            String schema = null;
            var name = tableName;
            if (name.contains(".")) {
                schema = WizFile.getBaseName(name);
                name = WizFile.getExtension(name);
            }
            table = new Registry(null, schema, name).getTable(connection);
        }
        try (var reader = new CSVFile(csvFile, CSVFile.Mode.READ)) {
            pace.info("CSV File: " + csvFile.getName() + " opened.");
            var firstLine = true;
            String[] line;
            var lineCount = 0l;
            while ((line = reader.readLine()) != null) {
                pace.waitIfPausedAndThrowIfStopped();
                lineCount++;
                pace.info("Processing line  " + lineCount + " of file: " + csvFile
                                .getName());
                var values = new Object[line.length];
                for (var i = 0; i < values.length; i++) {
                    if (firstLine) {
                        values[i] = line[i];
                    } else {
                        var field = table.fields.get(i);
                        values[i] = field.getValueFrom(line[i]);
                        fixValuesForSQLTypes(values, i, field);
                    }
                }
                var fields = new ArrayList<Field>();
                if (firstLine) {
                    pace.info("Making sure the table fields matchs on the first line.");
                    firstLine = false;
                    for (Object value : values) {
                        for (Field field : table.fields) {
                            if (Objects.equals(value, field.name)) {
                                fields.add(field);
                                break;
                            }
                        }
                    }
                    if (fields.size() == values.length) {
                        table.fields = fields;
                    }
                } else {
                    pace.info("Inserting line  " + lineCount + " of file: " + csvFile
                                    .getName());
                    var valueds = new ArrayList<Valued>();
                    for (var i = 0; i < values.length; i++) {
                        var field = table.fields.get(i);
                        var valued = new Valued(field.name, field.nature, values[i]);
                        valueds.add(valued);
                    }
                    pace.waitIfPausedAndThrowIfStopped();
                    destiny.base.helper.insert(connection, new Insert(new Registier(
                                    table.registry), valueds), null);
                }
            }
        }
    }

    private void fixValuesForSQLTypes(Object[] values, int i, Field field) {
        if (values[i] == null) {
            return;
        }
        switch (field.nature) {
            case DATE:
                values[i] = new java.sql.Date(((java.util.Date) values[i]).getTime());
                break;
            case TIME:
                values[i] = new java.sql.Time(((java.util.Date) values[i]).getTime());
                break;
            case TIMESTAMP:
                values[i] = new java.sql.Timestamp(((java.util.Date) values[i])
                                .getTime());
                break;
            default:
                break;
        }
    }
}
