package com.example.pesan;

public class note {
    String key, title,  ayat, content;

    public note(String key, String title, String ayat, String content) {
        this.key = key;
        this.title = title;
        this.ayat = ayat;
        this.content = content;
    }

    public note() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
