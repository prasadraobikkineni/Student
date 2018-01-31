package com.studentparty.model;

import java.io.Serializable;

/**
 * Created by Admin on 16-01-2018.
 */

public class People implements Serializable{

    String firstName;
    String surName;
    String imageUrl;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public People(){

    }

    public People(String artistId, String artistName, String artistGenre,String uid) {
        this.firstName = artistId;
        this.surName = artistName;
        this.imageUrl = artistGenre;
        this.uid=uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
