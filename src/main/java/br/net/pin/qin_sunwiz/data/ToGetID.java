package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class ToGetID {
    public String name;
    public Valued filter;

    public ToGetID() {}

    public ToGetID(String name) {
        this.name = name;
    }

    public ToGetID(String name, Valued filter) {
        this.name = name;
        this.filter = filter;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static ToGetID fromString(String json) {
        return new Gson().fromJson(json, ToGetID.class);
    }
}
