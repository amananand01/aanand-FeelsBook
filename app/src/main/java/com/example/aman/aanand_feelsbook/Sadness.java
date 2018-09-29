package com.example.aman.aanand_feelsbook;

import java.util.Date;

public class Sadness extends Emotions {

    public Sadness(Date date){
        super(date);
    }

    @Override
    public String getEmotion(){
        return "Sadness";
    }
}
