package com.example.aman.aanand_feelsbook;

import java.util.Date;

public class Love extends Emotions {

    public Love(Date date){
        super(date);
    }

    @Override
    public String getEmotion(){
        return "Love";
    }
}
