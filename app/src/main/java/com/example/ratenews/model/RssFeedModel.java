package com.example.ratenews.model;

public class RssFeedModel {

        public String novostiFeed;
        public String title;
        public String description;
        public String content;
        public String date;
        public int positiveVotes;
        public int negativeVotes;
        public boolean isTrueBtnClicked;
        public boolean isLieBtnClicked;

    public RssFeedModel(String novostiFeed, String title, String description, String content, String date, int positiveVotes, int negativeVotes, boolean isTrueBtnClicked, boolean isLieBtnClicked) {
        this.novostiFeed = novostiFeed;
        this.title = title;
        this.description = description;
        this.content = content;
        this.date = date;
        this.positiveVotes = positiveVotes;
        this.negativeVotes = negativeVotes;
        this.isTrueBtnClicked = isTrueBtnClicked;
        this.isLieBtnClicked = isLieBtnClicked;
    }

    public String getNovostiFeed() {
        return novostiFeed;
    }

    public void setNovostiFeed(String novostiFeed) {
        this.novostiFeed = novostiFeed;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isTrueBtnClicked() {return isTrueBtnClicked; }

    public int getPositiveVotes() { return positiveVotes; }

    public void setPositiveVotes( int positiveVotes ) { this.positiveVotes = positiveVotes; }

    public int getNegativeVotes() { return negativeVotes; }

    public void setNegativeVotes(int negativeVotes) { this.negativeVotes = negativeVotes; }

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
