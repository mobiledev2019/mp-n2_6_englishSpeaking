package com.lamtt.learnenglish.object;

public class Category {

    private int id;

    private String tag;

    private String vi;

    public Category(int id, String tag, String vi) {
        this.id = id;
        this.tag = tag;
        this.vi = vi;
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }
}
