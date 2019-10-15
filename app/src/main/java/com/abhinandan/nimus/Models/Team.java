package com.abhinandan.nimus.Models;

public class Team {
    private String memberOne;
    private String memberTwo;
    private String memberThree;
    private String memberFour;
    private String memberFive;
    private String teamName;

    public Team(String memberOne, String memberTwo, String memberThree, String memberFour, String memberFive, String teamName) {
        this.memberOne = memberOne;
        this.memberTwo = memberTwo;
        this.memberThree = memberThree;
        this.memberFour = memberFour;
        this.memberFive = memberFive;
        this.teamName = teamName;
    }

    public Team() {

    }

    public String getMemberOne() {
        return memberOne;
    }

    public void setMemberOne(String memberOne) {
        this.memberOne = memberOne;
    }

    public String getMemberTwo() {
        return memberTwo;
    }

    public void setMemberTwo(String memberTwo) {
        this.memberTwo = memberTwo;
    }

    public String getMemberThree() {
        return memberThree;
    }

    public void setMemberThree(String memberThree) {
        this.memberThree = memberThree;
    }

    public String getMemberFour() {
        return memberFour;
    }

    public void setMemberFour(String memberFour) {
        this.memberFour = memberFour;
    }

    public String getMemberFive() {
        return memberFive;
    }

    public void setMemberFive(String memberFive) {
        this.memberFive = memberFive;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
