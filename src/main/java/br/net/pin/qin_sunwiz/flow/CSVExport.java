package br.net.pin.qin_sunwiz.flow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.slf4j.LoggerFactory;

import br.net.pin.qin_sunwiz.data.DataLink;
import br.net.pin.qin_sunwiz.data.Registier;
import br.net.pin.qin_sunwiz.data.Registry;
import br.net.pin.qin_sunwiz.data.Select;

public class CSVExport implements Runnable {
  
  private final DataLink origin;
  private final File destiny;
  private final Pace pace;

  public CSVExport(DataLink origin, File destiny) {
    this(origin, destiny, null);
  }

  public CSVExport(DataLink origin, File destiny, Pace pace) {
    this.origin = origin;
    this.destiny = destiny;
    this.pace = pace != null ? pace : new Pace(LoggerFactory.getLogger(CSVExport.class));
  }

  public void run() {
    try {
      pace.info("Origin: " + origin);
      pace.info("Destiny: " + destiny);
      if (!destiny.exists()) {
        Files.createDirectories(destiny.toPath());
      }
      if (!destiny.exists()) {
        throw new Exception("Could not create the destination folder.");
      }
      if (!destiny.isDirectory()) {
        throw new Exception("The destination must be a directory.");
      }
      pace.waitIfPausedAndThrowIfStopped();
      pace.info("Connecting to Origin...");
      try (var originConn = origin.connect()) {
        pace.info("Connected.");
        pace.waitIfPausedAndThrowIfStopped();
        pace.info("Getting tables...");
        var heads = origin.base.helper.getHeads(originConn);
        for (Registry head : heads) {
          pace.info("Processing: " + head);
          var table = head.getTable(originConn);
          try (var writer = new PrintWriter(new FileOutputStream(new File(
              destiny,
              head.getNameForFile() + ".tab"), false), true)) {
            writer.write(table.toString());
          }
          final var fileDestiny = new File(destiny, head.getNameForFile() + ".csv");
          try (var csvFile = new CSVFile(fileDestiny, CSVFile.Mode.WRITE)) {
            final var row = new String[table.fields.size()];
            for (var i = 0; i < table.fields.size(); i++) {
              row[i] = table.fields.get(i).name;
            }
            csvFile.writeLine(row);
            var rstOrigin = origin.base.helper.select(originConn, new Select(new Registier(head)), null);
            var recordCount = 0L;
            while (rstOrigin.next()) {
              recordCount++;
              pace.info("Writing record " + recordCount + " of " + head.name);
              for (var i = 0; i < table.fields.size(); i++) {
                row[i] = table.fields.get(i).formatValue(rstOrigin.getObject(i + 1));
              }
              csvFile.writeLine(row);
            }
          }
        }
      }
      pace.info("CSV Export Finished!");
    } catch (Exception error) {
      pace.error("Could not export", error);
    }
  }
}
