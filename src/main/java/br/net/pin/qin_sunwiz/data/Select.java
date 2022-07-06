package br.net.pin.qin_sunwiz.data;

import java.util.List;

import com.google.gson.Gson;

public class Select implements Fixable {
  public Registry registry;
  public List<Typed> fields;
  public List<Joined> joins;
  public List<Filter> filters;
  public List<Order> orders;
  public Integer offset;
  public Integer limit;

  public Select() {
  }

  public Select(Registry registry) {
    this.registry = registry;
  }

  public Select(Registry registry, List<Typed> fields) {
    this.registry = registry;
    this.fields = fields;
  }

  public Select(Registry registry, List<Typed> fields, List<Joined> joins) {
    this.registry = registry;
    this.fields = fields;
    this.joins = joins;
  }

  public Select(Registry registry, List<Typed> fields, List<Joined> joins, List<Filter> filters) {
    this.registry = registry;
    this.fields = fields;
    this.joins = joins;
    this.filters = filters;
  }

  public Select(Registry registry, List<Typed> fields, List<Joined> joins, List<Filter> filters,
      List<Order> orders) {
    this.registry = registry;
    this.fields = fields;
    this.joins = joins;
    this.filters = filters;
    this.orders = orders;
  }

  public Select(Registry registry, List<Typed> fields, List<Joined> joins, List<Filter> filters, List<Order> orders,
      Integer offset) {
    this.registry = registry;
    this.fields = fields;
    this.joins = joins;
    this.filters = filters;
    this.orders = orders;
    this.offset = offset;
  }

  public Select(Registry registry, List<Typed> fields, List<Joined> joins, List<Filter> filters, List<Order> orders,
      Integer offset, Integer limit) {
    this.registry = registry;
    this.fields = fields;
    this.joins = joins;
    this.filters = filters;
    this.orders = orders;
    this.offset = offset;
    this.limit = limit;
  }

  public boolean hasJoins() {
    return this.joins != null && !this.joins.isEmpty();
  }

  public boolean hasFilters() {
    return this.filters != null && !this.filters.isEmpty();
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Select fromString(String json) {
    return new Gson().fromJson(json, Select.class);
  }
}
