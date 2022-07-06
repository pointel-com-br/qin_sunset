package br.net.pin.qin_sunwiz.flow;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

import br.net.pin.qin_sunwiz.mage.WizChars;

public class CSVRead implements Closeable {

  private final BufferedReader reader;

  public CSVRead(Reader reader) {
    this.reader = new BufferedReader(reader);
  }

  public String[] readLine() throws Exception {
    final var line = this.reader.readLine();
    if (line == null) {
      return null;
    }
    var result = new LinkedList<>();
    var openQuotes = false;
    var builder = new StringBuilder();
    for (var i = 0; i < line.length(); i++) {
      var actual = line.charAt(i);
      var next = i < line.length() - 1 ? line.charAt(i + 1) : ' ';
      if (openQuotes) {
        if (actual == '"') {
          if (next == '"') {
            builder.append('"');
            i++;
          } else {
            openQuotes = false;
          }
        } else {
          builder.append(actual);
        }
      } else if (actual == '"') {
        openQuotes = true;
      } else if (actual == ',' || actual == ';') {
        result.add(WizChars.remakeControlFlow(builder.toString()));
        builder = new StringBuilder();
      } else {
        builder.append(actual);
      }
    }
    result.add(WizChars.remakeControlFlow(builder.toString()));
    return result.toArray(new String[result.size()]);
  }

  @Override
  public void close() throws IOException {
    this.reader.close();
  }
}
