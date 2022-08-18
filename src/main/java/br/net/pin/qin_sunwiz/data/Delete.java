package br.net.pin.qin_sunwiz.data;

import java.util.List;

import com.google.gson.Gson;

public class Delete implements Fixable {
  public Registier registier;
  public List<Filter> filters;
  public Integer limit;

  public Delete() {
  }

  public Delete(Registier registier, List<Filter> filters, Integer limit) {
    this.registier = registier;
    this.filters = filters;
    this.limit = limit;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Delete fromString(String json) {
    return new Gson().fromJson(json, Delete.class);
  }
}
