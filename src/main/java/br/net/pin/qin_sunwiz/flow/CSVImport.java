package br.net.pin.qin_sunwiz.flow;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

import br.net.pin.qin_sunwiz.data.DataLink;
import br.net.pin.qin_sunwiz.data.Field;
import br.net.pin.qin_sunwiz.data.Insert;
import br.net.pin.qin_sunwiz.data.Registier;
import br.net.pin.qin_sunwiz.data.Registry;
import br.net.pin.qin_sunwiz.data.Table;
import br.net.pin.qin_sunwiz.data.Valued;
import br.net.pin.qin_sunwiz.mage.WizFile;

public class CSVImport extends Thread {
  private final Pace progress;
  private final File origin;
  private final DataLink destiny;

  public CSVImport(File origin, DataLink destiny, Pace progress) {
    super("ImportFromCSV");
    this.origin = origin;
    this.destiny = destiny;
    this.progress = progress;
  }

  @Override
  public void run() {
    try {
      if (!this.origin.exists()) {
        throw new Exception("The origin must exist.");
      }
      try (var connection = this.destiny.connect()) {
        this.progress.log("Connected to destiny database.");
        if (this.origin.isFile()) {
          this.importCSVFile(this.origin, connection);
        } else {
          for (File inside : this.origin.listFiles()) {
            if (this.isCSVFile(inside)) {
              this.importCSVFile(inside, connection);
            }
          }
        }
      }
      this.progress.log("Finished to import from CSV.");
    } catch (Exception e) {
      this.progress.log(e);
    }
  }

  private boolean isCSVFile(File file) {
    return file.isFile() && file.getName().toLowerCase().endsWith(".csv");
  }

  private void importCSVFile(File csvFile, Connection connection) throws Exception {
    this.progress.log("Importing CSV File: " + csvFile.getName());
    var tableName = WizFile.getBaseName(csvFile.getName());
    var tableFile = new File(csvFile.getParent(), tableName + ".tab");
    Table table;
    if (tableFile.exists()) {
      this.progress.log("Loading table metadata from file.");
      table = Table.fromString(Files.readString(tableFile.toPath()));
      this.destiny.base.helper.create(connection, table, true);
    } else {
      this.progress.log("Loading table metadata from connection.");
      String schema = null;
      var name = tableName;
      if (name.contains(".")) {
        schema = WizFile.getBaseName(name);
        name = WizFile.getExtension(name);
      }
      table = new Registry(null, schema, name).getTable(connection);
    }
    try (var reader = new CSVFile(csvFile, CSVFile.Mode.READ)) {
      this.progress.log("CSV File: " + csvFile.getName() + " opened.");
      var firstLine = true;
      String[] line;
      var lineCount = 0l;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        this.progress.log("Processing line  " + lineCount + " of file: " + csvFile
            .getName());
        var values = new Object[line.length];
        for (var i = 0; i < values.length; i++) {
          if (firstLine) {
            values[i] = line[i];
          } else {
            var field = table.fields.get(i);
            values[i] = field.getValueFrom(line[i]);
            this.fixValuesForSQLTypes(values, i, field);
          }
        }
        var fields = new ArrayList<Field>();
        if (firstLine) {
          this.progress.log("Making sure the table fields matchs on the first line.");
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
          this.progress.log("Inserting line  " + lineCount + " of file: " + csvFile
              .getName());
          var valueds = new ArrayList<Valued>();
          for (var i = 0; i < values.length; i++) {
            var field = table.fields.get(i);
            var valued = new Valued(field.name, field.nature, values[i]);
            valueds.add(valued);
          }
          this.destiny.base.helper.insert(connection, new Insert(new Registier(table.registry), valueds), null);
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
        values[i] = new java.sql.Timestamp(((java.util.Date) values[i]).getTime());
        break;
      default:
        break;
    }
  }
}
