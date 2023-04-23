package com.example.netcloudsharing;

public class MusicBean {
    private String id; //歌曲id 1
    private String song; //歌曲名称 2
    private String singer; //歌手名 3
    private String album; //专辑 4
    private String duration; //持续的时间 5
    private String path; //歌曲路径 6
    private String pic;
    private int rid;
    private int lid;
    boolean isNetMusic;

    public MusicBean(String id, String song, String singer, String album, String duration, String path, String pic, int rid, int lid, boolean isNetMusic) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.pic = pic;
        this.rid = rid;
        this.lid = lid;
        this.isNetMusic = isNetMusic;
    }

    /**
     * 播放网络音乐
     *
     * @param id
     * @param song
     * @param singer
     * @param album
     * @param duration
     * @param rid
     * @param pic
     * @param isNetMusic
     */
    public MusicBean(String id, String song, String singer, String album, String duration, int rid, String pic, boolean isNetMusic) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.pic = pic;
        this.rid = rid;
        this.isNetMusic = isNetMusic;
    }

    /**
     * 播放本地音乐
     *
     * @param id
     * @param song
     * @param singer
     * @param album
     * @param duration
     * @param path
     * @param isNetMusic
     * @param lid
     */
    public MusicBean(String id, String song, String singer, String album, String duration, String path, boolean isNetMusic, int lid) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.lid = lid;
        this.path = path;
        this.isNetMusic = isNetMusic;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public int getLid() {
        return lid;
    }

    public void setMusicItem(boolean netMusic) {
        isNetMusic = netMusic;
    }

    public boolean isNetMusic() {
        return isNetMusic;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRid() {
        return rid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }
}
