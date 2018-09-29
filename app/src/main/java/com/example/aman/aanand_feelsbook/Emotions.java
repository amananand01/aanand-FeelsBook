package com.example.aman.aanand_feelsbook;

import java.util.Date;

public abstract class Emotions {
    protected Date date;
    protected String comment;

    public Emotions(Date date){
        this.date=date;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date=date;
    }

    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment=comment;
    }

    public abstract String getEmotion();
}