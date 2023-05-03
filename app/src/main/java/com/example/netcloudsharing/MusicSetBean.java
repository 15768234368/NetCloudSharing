package com.example.netcloudsharing;

public class MusicSetBean {
    private String photoUrl;
    private String setName;
    private int bangId;

    public MusicSetBean(String photoUrl, String setName, int bangId) {
        this.photoUrl = photoUrl;
        this.setName = setName;
        this.bangId = bangId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public void setBangId(int bangId) {
        this.bangId = bangId;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSetName() {
        return setName;
    }

    public int getBangId() {
        return bangId;
    }

}
