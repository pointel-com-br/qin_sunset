package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class Linked implements Fixable {
  public String name;
  public String with;

  public Linked() {
  }

  public Linked(String name) {
    this.name = name;
  }

  public Linked(String name, String with) {
    this.name = name;
    this.with = with;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Valued fromString(String json) {
    return new Gson().fromJson(json, Valued.class);
  }
}
