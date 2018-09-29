package com.example.aman.aanand_feelsbook;

import java.util.Date;

public class Surprise extends Emotions {

    public Surprise(Date date){
        super(date);
    }

    @Override
    public String getEmotion(){
        return "Surprise";
    }
}
