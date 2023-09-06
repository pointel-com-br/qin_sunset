package br.net.pin.qin_sunwiz.data;

import com.google.gson.Gson;

public class Filter implements Fixable {
    public Filter.Seems seems;
    public Filter.Likes likes;
    public Valued valued;
    public Linked linked;
    public Filter.Ties ties;

    public Filter() {
        this(Seems.SAME, Likes.EQUALS, null, null, Ties.AND);
    }

    public Filter(Valued valued) {
        this(Seems.SAME, Likes.EQUALS, valued, null, Ties.AND);
    }

    public Filter(Linked linked) {
        this(Seems.SAME, Likes.EQUALS, null, linked, Ties.AND);
    }

    public Filter(Seems seem, Likes likes, Valued valued, Linked linked, Ties ties) {
        this.seems = seem;
        this.likes = likes;
        this.valued = valued;
        this.linked = linked;
        this.ties = ties;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Filter fromString(String json) {
        return new Gson().fromJson(json, Filter.class);
    }

    public static enum Seems {
        SAME, DIVERSE
    }

    public static enum Likes {
        EQUALS,

        BIGGER, LESSER,

        BIGGER_EQUALS, LESSER_EQUALS,

        STARTS_WITH, ENDS_WITH,

        CONTAINS,
    }

    public static enum Ties {
        AND, OR
    }
}
