package br.net.pin.qin_sunset.swap;

import com.google.gson.Gson;

public class PathWrite {
    public String path;
    public Boolean base64;
    public String data;
    public Integer rangeStart;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PathWrite fromString(String json) {
        return new Gson().fromJson(json, PathWrite.class);
    }
}
