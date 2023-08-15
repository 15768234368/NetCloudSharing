package com.example.netcloudsharing.Bean;

public class ArtistBean {
    private int artistId;
    private String artistName;
    private String artistPic;

    private String aartist;
    private int albumNum;
    private int artistFans;
    private String country;
    private String info;
    private int musicNum;
    private int mvNum;

    public ArtistBean(int artistId, String artistName, String artistPic) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistPic = artistPic;
    }

    public ArtistBean(int artistId, String artistName, String artistPic, String aartist, int albumNum, int artistFans, String country, String info, int musicNum, int mvNum) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistPic = artistPic;
        this.aartist = aartist;
        this.albumNum = albumNum;
        this.artistFans = artistFans;
        this.country = country;
        this.info = info;
        this.musicNum = musicNum;
        this.mvNum = mvNum;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistPic(String artistPic) {
        this.artistPic = artistPic;
    }

    public int getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistPic() {
        return artistPic;
    }

    public void setAartist(String aartist) {
        this.aartist = aartist;
    }

    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public void setArtistFans(int artistFans) {
        this.artistFans = artistFans;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMusicNum(int musicNum) {
        this.musicNum = musicNum;
    }

    public void setMvNum(int mvNum) {
        this.mvNum = mvNum;
    }

    public String getAartist() {
        return aartist;
    }

    public int getAlbumNum() {
        return albumNum;
    }

    public int getArtistFans() {
        return artistFans;
    }

    public String getCountry() {
        return country;
    }

    public String getInfo() {
        return info;
    }

    public int getMusicNum() {
        return musicNum;
    }

    public int getMvNum() {
        return mvNum;
    }
}
