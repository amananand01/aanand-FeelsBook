package com.example.aman.aanand_feelsbook;

import java.util.Date;

public class Anger extends Emotions {

    public Anger(Date date){
        super(date);
    }

    @Override
    public String getEmotion(){
        return "Anger";
    }
}
