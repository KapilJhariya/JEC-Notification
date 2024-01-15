package com.example.jec;

public class Item {
    StringBuilder name;
    String download;

    public Item(StringBuilder name,String download) {
        this.name = name;
        this.download = download;
    }

    public StringBuilder getName() {
        return name;
    }

    public void setName(StringBuilder name) {
        this.name = name;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
}
