package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class DataWays implements Fixable {
  public DataJdbc jdbc;
  public DataLink link;

  public DataWays() {
  }

  public DataWays(DataJdbc jdbc) {
    this.jdbc = jdbc;
  }

  public DataWays(DataLink link) {
    this.link = link;
  }

  public DataWays(DataJdbc jdbc, DataLink link) {
    this.jdbc = jdbc;
    this.link = link;
  }

  public String getName() {
    if (this.jdbc != null) {
      return this.jdbc.name;
    }
    if (this.link != null) {
      return this.link.name;
    }
    return null;
  }

  public String getUrl() {
    if (this.jdbc != null) {
      return this.jdbc.url;
    }
    if (this.link != null) {
      return this.link.formUrl();
    }
    return null;
  }

  public String getUser() {
    if (this.jdbc != null) {
      return this.jdbc.user;
    }
    if (this.link != null) {
      return this.link.user;
    }
    return null;
  }

  public String getPass() {
    if (this.jdbc != null) {
      return this.jdbc.pass;
    }
    if (this.link != null) {
      return this.link.pass;
    }
    return null;
  }

  public Helper getHelper() {
    if (this.link != null && this.link.base != null) {
      return this.link.base.helper;
    }
    return DataBase.getHelperFromURL(this.getUrl());
  }

  @Override
  public void fixNulls() throws Exception {
    if (this.jdbc != null) {
      this.jdbc.fixNulls();
    }
    if (this.link != null) {
      this.link.fixNulls();
    }
  }

  @Override
  public void fixNullsAndEnvs() throws Exception {
    if (this.jdbc != null) {
      this.jdbc.fixNullsAndEnvs();
    }
    if (this.link != null) {
      this.link.fixNullsAndEnvs();
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static DataWays fromString(String json) {
    return new Gson().fromJson(json, DataWays.class);
  }
}
