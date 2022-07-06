package br.net.pin.qin_sunwiz.flow;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CSVFile implements Closeable {
  private final File file;
  private final Mode mode;
  private final CSVRead reader;
  private final CSVWrite writer;

  public CSVFile(File file, Mode mode) throws Exception {
    this.file = file;
    this.mode = mode;
    if (mode == Mode.READ) {
      this.reader = new CSVRead(new FileReader(file, StandardCharsets.UTF_8));
      this.writer = null;
    } else {
      this.reader = null;
      this.writer = new CSVWrite(new FileWriter(file, StandardCharsets.UTF_8, mode == Mode.APPEND));
    }
  }

  public File getFile() {
    return this.file;
  }

  public Mode getMode() {
    return this.mode;
  }

  public String[] readLine() throws Exception {
    return this.reader.readLine();
  }

  public void writeLine(String... columns) {
    this.writer.writeLine(columns);
  }

  @Override
  public void close() throws IOException {
    if (this.reader != null) {
      this.reader.close();
    }
    if (this.writer != null) {
      this.writer.close();
    }
  }

  public static enum Mode {
    READ, WRITE, APPEND
  }
}
