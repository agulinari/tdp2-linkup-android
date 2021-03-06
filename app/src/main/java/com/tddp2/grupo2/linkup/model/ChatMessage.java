package com.tddp2.grupo2.linkup.model;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private boolean liked;
    private String fbid;
    private String fbidTo;

    public ChatMessage(String messageText, String messageUser, String fbid, String fbidTo) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.fbid = fbid;
        this.fbidTo = fbidTo;
        this.liked = false;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getFbidTo() {
        return fbidTo;
    }

    public void setFbidTo(String fbidTo) {
        this.fbidTo = fbidTo;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}