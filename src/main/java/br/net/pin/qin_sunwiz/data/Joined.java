package br.net.pin.qin_sunwiz.data;

import java.util.List;
import com.google.gson.Gson;

public class Joined {
    public Registry registry;
    public String alias;
    public List<Filter> filters;
    public Joined.Ties ties;

    public Joined() {}

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

    public Joined(Registry registry, Ties ties) {
        this.registry = registry;
        this.ties = ties;
    }

    public Joined(Registry registry, String alias, Ties ties) {
        this.registry = registry;
        this.alias = alias;
        this.ties = ties;
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

    public Joined(Registry registry, List<Filter> filters, Ties ties) {
        this.registry = registry;
        this.filters = filters;
        this.ties = ties;
    }

    public Joined(Registry registry, String alias, List<Filter> filters, Ties ties) {
        this.registry = registry;
        this.alias = alias;
        this.filters = filters;
        this.ties = ties;
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
