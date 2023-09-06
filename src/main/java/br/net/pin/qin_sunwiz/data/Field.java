package br.net.pin.qin_sunwiz.data;

import br.net.pin.qin_sunwiz.mage.WizData;

public class Field implements Fixable {
    public String name;
    public Nature nature;
    public Integer size;
    public Integer precision;
    public Boolean notNull;
    public Boolean key;

    public Field() {}

    public Field(String name) {
        this.name = name;
    }

    public Field(String name, Nature nature) {
        this.name = name;
        this.nature = nature;
    }

    public Field(String name, Nature nature, Boolean notNull) {
        this.name = name;
        this.nature = nature;
        this.notNull = notNull;
    }

    public Field(String name, Nature nature, Integer size) {
        this.name = name;
        this.nature = nature;
        this.size = size;
    }

    public Field(String name, Nature nature, Integer size, Boolean notNull) {
        this.name = name;
        this.nature = nature;
        this.size = size;
        this.notNull = notNull;
    }

    public Field(String name, Nature nature, Integer size, Integer precision) {
        this.name = name;
        this.nature = nature;
        this.size = size;
        this.precision = precision;
    }

    public Field(String name, Nature nature, Integer size, Integer precision,
                    Boolean notNull) {
        this.name = name;
        this.nature = nature;
        this.size = size;
        this.precision = precision;
        this.notNull = notNull;
    }

    public Field(String name, Nature nature, Integer size, Integer precision,
                    Boolean notNull, Boolean key) {
        this.name = name;
        this.nature = nature;
        this.size = size;
        this.precision = precision;
        this.notNull = notNull;
        this.key = key;
    }

    public Object getValueFrom(String formatted) throws Exception {
        return WizData.getValueFrom(this.nature, formatted);
    }

    public String formatValue(Object value) throws Exception {
        return WizData.formatValue(this.nature, value);
    }
}
