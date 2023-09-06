package br.net.pin.qin_sunwiz.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;
import com.google.gson.Gson;
import br.net.pin.qin_sunwiz.mage.WizChars;
import br.net.pin.qin_sunwiz.mage.WizData;

public class Registry implements Fixable {
    public String catalog;
    public String schema;
    public String name;
    public String alias;

    public Registry() {}

    public Registry(String name) {
        this.name = name;
    }

    public Registry(String schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    public Registry(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    public Registry(String catalog, String schema, String name, String alias) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
        this.alias = alias;
    }

    public String getSource() {
        return this.alias != null && !this.alias.isEmpty() ? this.alias
                        : this.getCatalogSchemaName();
    }

    public String getSchemaName() {
        return WizChars.sum(".", this.schema, this.name);
    }

    public String getCatalogSchemaName() {
        return WizChars.sum(".", this.catalog, this.schema, this.name);
    }

    public String getNameForFile() {
        return WizChars.sum(".", this.catalog, this.schema, this.name);
    }

    public Table getTable(Connection connection) throws Exception {
        var result = new Table(this, new ArrayList<>(), new ArrayList<>());
        var meta = connection.getMetaData();
        var set = meta.getPrimaryKeys(this.catalog, this.schema, this.name);
        while (set.next()) {
            result.keys.add(set.getString(4));
        }
        var rst = meta.getColumns(this.catalog, this.schema, this.name, "%");
        while (rst.next()) {
            var campo = new Field();
            campo.name = rst.getString(4);
            campo.nature = WizData.getNatureOfSQL(rst.getInt(5));
            campo.size = rst.getInt(7);
            campo.precision = rst.getInt(9);
            campo.notNull = "NO".equals(rst.getString(18));
            campo.key = false;
            if (result.keys.contains(campo.name)) {
                campo.key = true;
            }
            result.fields.add(campo);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Registry)) {
            return false;
        }
        Registry registry = (Registry) o;
        return Objects.equals(catalog, registry.catalog)
                        && Objects.equals(schema, registry.schema) && Objects.equals(name,
                                        registry.name)
                        && Objects.equals(alias, registry.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalog, schema, name, alias);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Registry fromString(String json) {
        return new Gson().fromJson(json, Registry.class);
    }
}
