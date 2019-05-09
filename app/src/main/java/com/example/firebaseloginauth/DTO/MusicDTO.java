package com.example.firebaseloginauth.DTO;

import java.io.Serializable;

public class MusicDTO implements Serializable {
    private String idMusic;
    private String photoMusic;
    private String displayname;
    private String singer;
    private String timeMusic;
    private String linkMusic;

    public MusicDTO(){

    }

    public MusicDTO(String idMusic, String photoMusic, String displayname, String singer, String timeMusic,String linkMusic){
        this.idMusic = idMusic;
        this.photoMusic = photoMusic;
        this.displayname = displayname;
        this.singer = singer;
        this.timeMusic = timeMusic;
        this.linkMusic = linkMusic;
    }

    public String getIdMusic() {
        return idMusic;
    }

    public void setIdMusic(String idMusic) {
        this.idMusic = idMusic;
    }

    public String getPhotoMusic() {
        return photoMusic;
    }

    public void setPhotoMusic(String photoMusic) {
        this.photoMusic = photoMusic;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTimeMusic() {
        return timeMusic;
    }

    public void setTimeMusic(String timeStrat) {
        this.timeMusic = timeStrat;
    }

    public String getLinkMusic() {
        return linkMusic;
    }

    public void setLinkMusic(String linkMusic) {
        this.linkMusic = linkMusic;
    }
}
