package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class Strain implements Fixable {
  public String restrict;
  public String modify;
  public String include;

  public Strain() {
  }

  public Strain(String restrict) {
    this.restrict = restrict;
  }

  public Strain(String restrict, String modify) {
    this.restrict = restrict;
    this.modify = modify;
  }

  public Strain(String restrict, String modify, String include) {
    this.restrict = restrict;
    this.modify = modify;
    this.include = include;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Strain fromString(String json) {
    return new Gson().fromJson(json, Strain.class);
  }
}
