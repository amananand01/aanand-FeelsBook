package com.example.aman.aanand_feelsbook;

import java.util.Date;

public class Fear extends Emotions {

    public Fear(Date date){
        super(date);
    }

    @Override
    public String getEmotion(){
        return "Fear";
    }
}
