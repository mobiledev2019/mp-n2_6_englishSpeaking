package com.lamtt.learnenglish.object;

public class Phrase {
    private int idPhrase;

    private String english;

    private String vietnamese;

    private String pathAudio;

    private String favorite;

    private String catalog;

    private String tag;

    public Phrase(int idPhrase, String english, String vietnamese,
                  String pathAudio, String favorite, String catalog, String tag) {
        this.idPhrase = idPhrase;
        this.english = english;
        this.vietnamese = vietnamese;
        this.pathAudio = pathAudio;
        this.favorite = favorite;
        this.catalog = catalog;
        this.tag = tag;
    }

    public Phrase() {

    }

    public int getIdPhrase() {
        return idPhrase;
    }

    public void setIdPhrase(int idPhrase) {
        this.idPhrase = idPhrase;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    public String getPathAudio() {
        return pathAudio;
    }

    public void setPathAudio(String pathAudio) {
        this.pathAudio = pathAudio;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
