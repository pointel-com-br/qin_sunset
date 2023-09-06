package br.net.pin.qin_sunwiz.mage;

public class WizInt {
    public static Integer fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.parseInt(value);
    }
}
