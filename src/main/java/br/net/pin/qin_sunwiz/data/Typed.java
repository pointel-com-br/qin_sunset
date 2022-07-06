package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class Typed implements Fixable {
  public String name;
  public Nature type;
  public String alias;

  public Typed() {
  }

  public Typed(String name) {
    this.name = name;
  }

  public Typed(String name, Nature type) {
    this.name = name;
    this.type = type;
  }

  public Typed(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }

  public Typed(String name, Nature type, String alias) {
    this.name = name;
    this.type = type;
    this.alias = alias;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Valued fromString(String json) {
    return new Gson().fromJson(json, Valued.class);
  }
}
