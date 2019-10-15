package com.abhinandan.nimus.Models;

public class EventFeed {
    private String imageUrl;
    private String title;
    private String desc;
    private String domainImageLink;
    private String domainName;
    private String date;
    private String startDate;
    private String endDate;
    private String venue;
    private String info_desc;
    private String highlightOne;
    private String highlightTwo;
    private String highlightThree;
    private String highlightMain;
    private String startDateFormatTwo;
    private String startTime;

    public EventFeed(String imageUrl, String title, String desc, String domainImageLink, String domainName, String date, String startDate, String endDate, String venue, String info_desc, String highlightOne, String highlightTwo, String highlightThree, String highlightMain, String startDateFormatTwo, String startTime) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.domainImageLink = domainImageLink;
        this.domainName = domainName;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        this.info_desc = info_desc;
        this.highlightOne = highlightOne;
        this.highlightTwo = highlightTwo;
        this.highlightThree = highlightThree;
        this.highlightMain = highlightMain;
        this.startDateFormatTwo = startDateFormatTwo;
        this.startTime = startTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDomainImageLink() {
        return domainImageLink;
    }

    public void setDomainImageLink(String domainImageLink) {
        this.domainImageLink = domainImageLink;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getInfo_desc() {
        return info_desc;
    }

    public void setInfo_desc(String info_desc) {
        this.info_desc = info_desc;
    }

    public String getHighlightOne() {
        return highlightOne;
    }

    public void setHighlightOne(String highlightOne) {
        this.highlightOne = highlightOne;
    }

    public String getHighlightTwo() {
        return highlightTwo;
    }

    public void setHighlightTwo(String highlightTwo) {
        this.highlightTwo = highlightTwo;
    }

    public String getHighlightThree() {
        return highlightThree;
    }

    public void setHighlightThree(String highlightThree) {
        this.highlightThree = highlightThree;
    }

    public String getHighlightMain() {
        return highlightMain;
    }

    public void setHighlightMain(String highlightMain) {
        this.highlightMain = highlightMain;
    }

    public String getStartDateFormatTwo() {
        return startDateFormatTwo;
    }

    public void setStartDateFormatTwo(String startDateFormatTwo) {
        this.startDateFormatTwo = startDateFormatTwo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
