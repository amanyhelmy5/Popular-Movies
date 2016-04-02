package com.timotiusoktorio.popularmovies.models;

/**
 * Created by Timotius on 2016-04-01.
 */

public class Trailer {

    String key;
    String name;
    String type;

    public Trailer() {}

    public Trailer(String key, String name, String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}