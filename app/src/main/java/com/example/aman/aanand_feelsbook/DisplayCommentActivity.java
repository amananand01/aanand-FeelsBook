package com.example.aman.aanand_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/*
DisplayCommentActivity Class
 - Used to add a comment for the feeling
 - USer can either save or go back ot the main screen
 */
public class DisplayCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comment);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView heading_text = findViewById(R.id.textView3);
        heading_text.setText(" Feeling: "+ message + " Saved");
    }

    // save comment listner
    // saves the comment for the last feeling recorded
    public void SaveComment(View view){

        EditText bodyText = (EditText) findViewById(R.id.edit_Date);
        String text = bodyText.getText().toString();

        // change the comments of the last emotion added to new comment
        int last_elem = MainActivity.emotions.size()-1;
        Emotions last_emotion = MainActivity.emotions.get(last_elem);
        last_emotion.setComment(text);
        MainActivity.emotions.set(last_elem,last_emotion);

        // save emotion in file
        try{
            FileOutputStream fos = openFileOutput(MainActivity.FILENAME,0);
            MainActivity.saveInFile(fos,MainActivity.FILENAME);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }
}
