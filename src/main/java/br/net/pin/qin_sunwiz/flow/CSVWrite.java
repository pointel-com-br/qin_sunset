package br.net.pin.qin_sunwiz.flow;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import br.net.pin.qin_sunwiz.mage.WizChars;

public class CSVWrite implements Closeable {

  private final PrintWriter writer;

  public CSVWrite(Writer writer) throws Exception {
    this.writer = new PrintWriter(writer);
  }

  public void writeLine(String... columns) {
    if (columns != null) {
      for (var i = 0; i < columns.length; i++) {
        var column = WizChars.replaceControlFlow(columns[i]);
        if (WizChars.contains(column, '"', ' ', ',', ';')) {
          column = '"' + column.replace("\"", "\"\"") + '"';
        }
        if (i > 0) {
          this.writer.write(",");
        }
        if (WizChars.isNotEmpty(column)) {
          this.writer.write(column);
        }
      }
    }
    this.writer.write(System.lineSeparator());
  }

  @Override
  public void close() throws IOException {
    this.writer.close();
  }
}
