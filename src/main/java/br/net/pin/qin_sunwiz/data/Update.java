package br.net.pin.qin_sunwiz.data;

import java.util.List;

import com.google.gson.Gson;

public class Update implements Fixable {
  public Registier registier;
  public List<Valued> valueds;
  public List<Filter> filters;
  public Integer limit;

  public Update() {
  }

  public Update(Registier registier) {
    this.registier = registier;
  }

  public Update(Registier registier, List<Valued> valueds) {
    this.registier = registier;
    this.valueds = valueds;
  }

  public Update(Registier registier, List<Valued> valueds, List<Filter> filters) {
    this.registier = registier;
    this.valueds = valueds;
    this.filters = filters;
  }

  public Update(Registier registier, List<Valued> valueds, List<Filter> filters, Integer limit) {
    this.registier = registier;
    this.valueds = valueds;
    this.filters = filters;
    this.limit = limit;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Update fromString(String json) {
    return new Gson().fromJson(json, Update.class);
  }
}
