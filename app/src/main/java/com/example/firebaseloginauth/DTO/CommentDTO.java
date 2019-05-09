package com.example.firebaseloginauth.DTO;

import java.io.Serializable;

public class CommentDTO implements Serializable {
    private String displayName;
    private String stringComment;

    public CommentDTO(){

    }

    public CommentDTO(String dis,String stringComment){
        this.displayName = dis;
        this.stringComment = stringComment;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStringComment() {
        return stringComment;
    }

    public void setStringComment(String stringComment) {
        this.stringComment = stringComment;
    }
}
