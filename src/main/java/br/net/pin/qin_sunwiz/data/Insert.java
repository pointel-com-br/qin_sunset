package br.net.pin.qin_sunwiz.data;

import java.util.List;

import com.google.gson.Gson;

public class Insert implements Fixable {
  public Registry registry;
  public List<Valued> valueds;

  public Insert() {
  }

  public Insert(Registry registry) {
    this.registry = registry;
  }

  public Insert(Registry registry, List<Valued> valueds) {
    this.registry = registry;
    this.valueds = valueds;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Insert fromString(String json) {
    return new Gson().fromJson(json, Insert.class);
  }
}
