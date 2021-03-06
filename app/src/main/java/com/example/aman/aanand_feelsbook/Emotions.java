package com.example.aman.aanand_feelsbook;

/*
Emotions Class
    - Used to make emotion objects
    - Emotions recorded are : Joy, Love, Sadness, Fear, Anger, Surprise
    - Constructor - stores Date
    - toString() - returns the string format for the object
    - Rest are getters and setters for the attributes.
*/
public class Emotions {
    protected String date;
    protected String comment;
    protected String feeling;

    public Emotions(String date){
        this.date=date;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment=comment;
    }

    public void setFeeling(String feeling){
        this.feeling=feeling;
    }
    public String getFeeling(){
        return feeling;
    }
    public String toString(){
        return this.date.toString()+" | "+ this.feeling +" | Comment : "+ this.comment;
    }
}