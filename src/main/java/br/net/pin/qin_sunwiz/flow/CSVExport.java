package br.net.pin.qin_sunwiz.flow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import br.net.pin.qin_sunwiz.data.DataLink;
import br.net.pin.qin_sunwiz.data.Registry;
import br.net.pin.qin_sunwiz.data.Select;

public class CSVExport extends Thread {

  private final DataLink origin;
  private final File destiny;
  private final Pace pace;

  public CSVExport(DataLink origin, File destiny) {
    this(origin, destiny, null);
  }

  public CSVExport(DataLink origin, File destiny, Pace pace) {
    super("ExportToCSV");
    this.origin = origin;
    this.destiny = destiny;
    this.pace = pace != null ? pace : new PaceCmd();
  }

  @Override
  public void run() {
    try {
      this.pace.log("Origin: " + this.origin);
      this.pace.log("Establishing destiny: " + this.destiny);
      if (!this.destiny.exists()) {
        Files.createDirectories(this.destiny.toPath());
      }
      if (!this.destiny.exists()) {
        throw new Exception("Could not create the destination folder.");
      }
      if (!this.destiny.isDirectory()) {
        throw new Exception("The destination must be a directory.");
      }
      this.pace.waitIfPausedAndThrowIfStopped();
      this.pace.log("Connecting to Origin...");
      try (var originConn = this.origin.connect()) {
        this.pace.log("Connected.");
        this.pace.waitIfPausedAndThrowIfStopped();
        this.pace.log("Getting tables...");
        var heads = this.origin.base.helper.getHeads(originConn);
        for (Registry head : heads) {
          this.pace.log("Processing: " + head);
          var table = head.getTable(originConn);
          try (var writer = new PrintWriter(new FileOutputStream(new File(
              this.destiny,
              head.getNameForFile() + ".tab"), false), true)) {
            writer.write(table.toString());
          }
          final var fileDestiny = new File(this.destiny, head.getNameForFile() + ".csv");
          try (var csvFile = new CSVFile(fileDestiny, CSVFile.Mode.WRITE)) {
            final var row = new String[table.fields.size()];
            for (var i = 0; i < table.fields.size(); i++) {
              row[i] = table.fields.get(i).name;
            }
            csvFile.writeLine(row);
            var rstOrigin = this.origin.base.helper.select(originConn, new Select(head));
            var recordCount = 0L;
            while (rstOrigin.next()) {
              recordCount++;
              this.pace.log("Writing record " + recordCount + " of " + head.name);
              for (var i = 0; i < table.fields.size(); i++) {
                row[i] = table.fields.get(i).formatValue(rstOrigin.getObject(i + 1));
              }
              csvFile.writeLine(row);
            }
          }
        }
      }
      this.pace.log("CSV Export Finished!");
    } catch (Exception error) {
      this.pace.log(error);
    }
  }
}
