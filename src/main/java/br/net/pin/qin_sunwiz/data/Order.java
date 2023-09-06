package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class Order {
    public String name;
    public Boolean desc;

    public Order() {}

    public Order(String name) {
        this.name = name;
    }

    public Order(String name, Boolean desc) {
        this.name = name;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Order fromString(String json) {
        return new Gson().fromJson(json, Order.class);
    }
}
