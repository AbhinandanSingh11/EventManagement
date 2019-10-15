package com.abhinandan.nimus.Models;

public class User {
    private String bio;
    private String email;
    private String full_name;
    private String user_name;
    private String uid;
    private String url;
    private String university;
    private String isteamLeader;
    private Team team;



    public User() {

    }

    public User(String bio, String email, String full_name, String user_name, String uid, String url, String university, String isteamLeader, Team team) {
        this.bio = bio;
        this.email = email;
        this.full_name = full_name;
        this.user_name = user_name;
        this.uid = uid;
        this.url = url;
        this.university = university;
        this.isteamLeader = isteamLeader;
        this.team = team;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getIsteamLeader() {
        return isteamLeader;
    }

    public void setIsteamLeader(String isteamLeader) {
        this.isteamLeader = isteamLeader;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}