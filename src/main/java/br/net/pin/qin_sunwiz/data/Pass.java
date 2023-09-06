package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class Pass implements Fixable {
    private byte[] data;

    public Pass() {
        this.data = null;
    }

    public Pass(byte[] data) {
        this.data = data;
    }

    public Pass(String data) {
        this.data = data.getBytes();
    }

    public String getPass() {
        return this.data != null ? new String(this.data) : null;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Pass fromString(String json) {
        return new Gson().fromJson(json, Pass.class);
    }
}
