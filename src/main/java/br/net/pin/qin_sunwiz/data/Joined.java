package br.net.pin.qin_sunwiz.data;

import java.util.List;

import com.google.gson.Gson;

public class Joined {
  public Joined.Ties ties;
  public Registry registry;
  public String alias;
  public List<Filter> filters;

  public Joined() {
  }

  public Joined(Ties ties) {
    this.ties = ties;
  }

  public Joined(Registry registry) {
    this.registry = registry;
  }

  public Joined(Registry registry, String alias) {
    this.registry = registry;
    this.alias = alias;
  }

  public Joined(Ties ties, Registry registry) {
    this.ties = ties;
    this.registry = registry;
  }

  public Joined(Ties ties, Registry registry, String alias) {
    this.ties = ties;
    this.registry = registry;
    this.alias = alias;
  }

  public Joined(Registry registry, List<Filter> filters) {
    this.registry = registry;
    this.filters = filters;
  }

  public Joined(Registry registry, String alias, List<Filter> filters) {
    this.registry = registry;
    this.alias = alias;
    this.filters = filters;
  }

  public Joined(Ties ties, Registry registry, List<Filter> filters) {
    this.ties = ties;
    this.registry = registry;
    this.filters = filters;
  }

  public Joined(Ties ties, Registry registry, String alias, List<Filter> filters) {
    this.ties = ties;
    this.registry = registry;
    this.alias = alias;
    this.filters = filters;
  }

  public boolean hasFilters() {
    return this.filters != null && !this.filters.isEmpty();
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Joined fromString(String json) {
    return new Gson().fromJson(json, Joined.class);
  }

  public static enum Ties {
    INNER, LEFT, RIGHT, FULL, CROSS
  }
}
