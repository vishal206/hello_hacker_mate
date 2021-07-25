package com.example.hellohackermate;

public class ModelPost {
    public ModelPost(){

    }
    String description;
    String pid;
    String ptime;
    String title;
    String uphone;
    String uid;
    String uname;

//    public ModelPost(String uid, String uname, String phone, String link, String description, String pinterested, String title, String ptime) {
//    }

    public String getpLink() {
        return pLink;
    }

    public void setpLink(String pLink) {
        this.pLink = pLink;
    }

    String pLink;

    public ModelPost(String uid, String uname,String uphone,String pLink,String description,  String pinterested, String title, String ptime) {
        this.description = description;
        this.ptime = ptime;
        this.title = title;
        this.uphone = uphone;
        this.uid = uid;
        this.uname = uname;
        this.pinterested = pinterested;
        this.pLink=pLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPinterested() {
        return pinterested;
    }

    public void setPinterested(String pinterested) {
        this.pinterested = pinterested;
    }

    String pinterested;




}
