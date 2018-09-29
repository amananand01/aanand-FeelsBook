package com.example.aman.aanand_feelsbook;

import java.util.Date;

public class Joy extends Emotions {

    public Joy(Date date){
        super(date);
    }

    @Override
    public String getEmotion(){
        return "Joy";
    }
}
