package com.example.nasa_imageoftheday;

public class imageNasa {

    long id;
    String textDate;
    String nameFile;
    String url;
    String titl;
    String explan;
    String urlHd;

    public imageNasa(long id, String titl, String textDate, String nameFile, String explan, String urlHd){
        this.id = id;
        this.nameFile = nameFile;
        this.explan = explan;
        this.titl = titl;
        this.textDate = textDate;
        this.urlHd = urlHd;
    }

    public long getId() { return id;}

    public void setId(int id) { this.id = id; }

    public String getNameFile() { return nameFile; }

    public void setNameFile(String nameFile) { this.nameFile = nameFile; }

    public String getDate() {
        return textDate;
    }

    public void setDate(String date) {
        this.textDate = date;
    }

    public String getTitl() {
        return titl;
    }

    public void setTitl(String titl) {
        this.titl = titl;
    }

    public String getExplan() {
        return explan;
    }

    public void setExplan(String explan) {
        this.explan = explan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHDurl() {
        return urlHd;
    }

    public void setHDurl(String hdurl) {
        this.urlHd = hdurl;
    }
}
