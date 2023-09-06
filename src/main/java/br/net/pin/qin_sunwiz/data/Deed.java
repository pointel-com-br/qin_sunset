package br.net.pin.qin_sunwiz.data;

public enum Deed {
    INSERT(true), SELECT(false), UPDATE(true), DELETE(true);

    public final boolean mutates;

    private Deed(boolean mutates) {
        this.mutates = mutates;
    }
}
