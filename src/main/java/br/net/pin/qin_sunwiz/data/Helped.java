package br.net.pin.qin_sunwiz.data;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Helped implements Closeable {
  public Connection link;
  public Helper helper;

  public Helped() {
  }

  public Helped(Connection link) {
    this.link = link;
  }

  public Helped(Helper helper) {
    this.helper = helper;
  }

  public Helped(Connection link, Helper helper) {
    this.link = link;
    this.helper = helper;
  }

  @Override
  public void close() throws IOException {
    try {
      this.link.close();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }
}
