package com.example.ratenews.model;



public class AllNewsModel {

    public AllNewsModel(Integer id, String title, String description, String link, String date,
                        String image, Integer positiveVotes, Integer negativeVotes, String paper,
                        boolean isTrueBtnClicked, boolean isLieBtnClicked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.image = image;
        this.positiveVotes = positiveVotes;
        this.negativeVotes = negativeVotes;
        this.paper = paper;
        this.isTrueBtnClicked = isTrueBtnClicked;
        this.isLieBtnClicked = isLieBtnClicked;
    }

    private Integer id;

    private String title;

    private String description;

    private String link;

    private String date;

    private String image;

    private Integer positiveVotes;

    private Integer negativeVotes;

    private String paper;

    public boolean isTrueBtnClicked;

    public boolean isLieBtnClicked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPositiveVotes() {
        return positiveVotes;
    }

    public void setPositiveVotes(Integer positiveVotes) {
        this.positiveVotes = positiveVotes;
    }

    public Integer getNegativeVotes() {
        return negativeVotes;
    }

    public void setNegativeVotes(Integer negativeVotes) {
        this.negativeVotes = negativeVotes;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public boolean isTrueBtnClicked() {
        return isTrueBtnClicked;
    }

    public void setTrueBtnClicked(boolean trueBtnClicked) {
        isTrueBtnClicked = trueBtnClicked;
    }

    public boolean isLieBtnClicked() {
        return isLieBtnClicked;
    }

    public void setLieBtnClicked(boolean lieBtnClicked) {
        isLieBtnClicked = lieBtnClicked;
    }

}