package br.net.pin.qin_sunwiz.data;

import java.util.List;

import com.google.gson.Gson;

public class Insert implements Fixable {
  public Registier registier;
  public List<Valued> valueds;

  public Insert() {
  }

  public Insert(Registier registier) {
    this.registier = registier;
  }

  public Insert(Registier registier, List<Valued> valueds) {
    this.registier = registier;
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
