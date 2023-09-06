package br.net.pin.qin_sunwiz.data;

import java.util.List;
import com.google.gson.Gson;

public class Table implements Fixable {
    public Registry registry;
    public List<Field> fields;
    public List<String> keys;

    public Table() {}

    public Table(Registry head) {
        this.registry = head;
    }

    public Table(Registry head, List<Field> fields) {
        this.registry = head;
        this.fields = fields;
    }

    public Table(Registry head, List<Field> fields, List<String> keys) {
        this.registry = head;
        this.fields = fields;
        this.keys = keys;
    }

    public String getSchemaName() {
        return this.registry.getSchemaName();
    }

    public String getCatalogSchemaName() {
        return this.registry.getCatalogSchemaName();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Table fromString(String json) {
        return new Gson().fromJson(json, Table.class);
    }
}
