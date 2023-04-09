package com.example.netcloudsharing.diary;

import java.io.Serializable;

public class NoteInfo implements Serializable {
    private String content;
    private String path;
    private String video;
    private String id;
    private String time;
    private int type;

    public NoteInfo() {

    }

    public NoteInfo(String content, String path, String video, String id, String time,int type) {
        this.content = content;
        this.path = path;
        this.video = video;
        this.id = id;
        this.time = time;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }

    public String getVideo() {
        return video;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
