package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class DataJdbc implements Fixable {
  public String name;
  public String url;
  public String user;
  public String pass;

  public DataJdbc() {
  }

  public DataJdbc(String url) {
    this.url = url;
  }

  public DataJdbc(String url, String user) {
    this.url = url;
    this.user = user;
  }

  public DataJdbc(String url, String user, String pass) {
    this.url = url;
    this.user = user;
    this.pass = pass;
  }

  public DataJdbc(String name, String url, String user, String pass) {
    this.name = name;
    this.url = url;
    this.user = user;
    this.pass = pass;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static DataJdbc fromString(String json) {
    return new Gson().fromJson(json, DataJdbc.class);
  }
}
