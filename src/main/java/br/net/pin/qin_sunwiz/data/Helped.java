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

  public int update(String sql, Object... params) throws Exception {
    if (params != null && params.length > 0) {
      var statement = this.link.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) {
        statement.setObject(i + 1, params[i]);
      }
      return statement.executeUpdate();
    } else {
      return this.link.createStatement().executeUpdate(sql);
    }
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
