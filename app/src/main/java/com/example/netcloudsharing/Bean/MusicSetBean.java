package com.example.netcloudsharing.Bean;

public class MusicSetBean {
    private String photoUrl;
    private String setName;
    private String setUrl;

    public MusicSetBean(String photoUrl, String setName, String setUrl) {
        this.photoUrl = photoUrl;
        this.setName = setName;
        this.setUrl = setUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public void setSetUrl(String setUrl) {
        this.setUrl = setUrl;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSetName() {
        return setName;
    }

    public String getSetUrl() {
        return setUrl;
    }

}
