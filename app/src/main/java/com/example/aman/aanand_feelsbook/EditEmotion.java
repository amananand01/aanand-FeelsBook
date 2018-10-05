package com.example.aman.aanand_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
EditEmotion Class
 - Used to change the items in the history list
 - User can change date and comments
*/
public class EditEmotion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emotion);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // Fill the fields with the item clicked
        FillValues();
    }

    // Populates the fields(Feeling, Date, Comment when the item is clicked)
    private void FillValues(){

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int position = Integer.parseInt(message);
        Emotions emotion = MainActivity.emotions.get(position);

        TextView comments_text = (EditText) findViewById(R.id.edit_comment);
        comments_text.setText(" " + emotion.getComment());

        TextView feeling_text = (TextView) findViewById(R.id.edit_feeling);
        feeling_text.setText(" "+emotion.getFeeling());

        String datetime = emotion.getDate();

        String date = (datetime.split("-")[2]).split("T")[0]  ;
        String mnth = datetime.split("-")[1];
        String year = datetime.split("-")[0];
        String hour = datetime.split(":")[0].split("T")[1];
        String min = datetime.split(":")[1];
        String sec = datetime.split(":")[2];

        ((EditText)findViewById(R.id.editText_dd)).setText(date);
        ((EditText)findViewById(R.id.editText_mm)).setText(mnth);
        ((EditText)findViewById(R.id.editText_yy)).setText(year);
        ((EditText)findViewById(R.id.editText_hh)).setText(hour);
        ((EditText)findViewById(R.id.editText_min)).setText(min);
        ((EditText)findViewById(R.id.editText_ss)).setText(sec);
    }

    // Save all the changes made by the user to the fields Date and Comment
    public void SaveChanges(View view){

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int position = Integer.parseInt(message);

        String date = ((EditText) findViewById(R.id.editText_yy)).getText().toString() + "-" +
                        ((EditText) findViewById(R.id.editText_mm)).getText().toString() + "-" +
                        ((EditText) findViewById(R.id.editText_dd)).getText().toString() + "T" +
                        ((EditText) findViewById(R.id.editText_hh)).getText().toString() + ":" +
                        ((EditText) findViewById(R.id.editText_min)).getText().toString() + ":" +
                        ((EditText) findViewById(R.id.editText_ss)).getText().toString() ;

        // make changes to the date and comment of the emotion
        Emotions emotion = MainActivity.emotions.get(position);

        emotion.setComment(((EditText) findViewById(R.id.edit_comment)).getText().toString());
        MainActivity.emotions.set(position,emotion);

        if(!date.equals(MainActivity.emotions.get(position).date)) DevideAndConquer(position,date);

        // save emotion in file
        try{
            FileOutputStream fos = openFileOutput(MainActivity.FILENAME,0);
            MainActivity.saveInFile(fos,MainActivity.FILENAME);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }

    // https://stackoverflow.com/questions/45987761/quicksort-divide-and-conquer/45987872
    // By Josh Evans

    // Used to sort emotions if a feeling date is changed
    private void DevideAndConquer(int position,String date){

        ArrayList<Emotions> less = new ArrayList<Emotions>();
        ArrayList<Emotions> equal = new ArrayList<Emotions>();
        ArrayList<Emotions> more = new ArrayList<Emotions>();

        Emotions editted_emotion =  MainActivity.emotions.get(position);
        editted_emotion.setDate(date);

        String editted_Date = MainActivity.emotions.get(position).date;
        MainActivity.emotions.remove(position);

        for (int i = 0; i < MainActivity.emotions.size(); i++) {
            String elem_date = MainActivity.emotions.get(i).date;

            if (compareDate(elem_date,editted_Date))
                less.add(MainActivity.emotions.get(i));
            else if (compareDate(elem_date,editted_Date))
                equal.add(MainActivity.emotions.get(i));
            else
                more.add(MainActivity.emotions.get(i));
        }

        ArrayList<Emotions> sorted = new ArrayList<Emotions>(less);
        sorted.addAll(equal);
        sorted.add(editted_emotion);
        sorted.addAll(more);

        MainActivity.emotions=sorted;
    }

    // used to compare two dates if date1 is before date2
    private boolean compareDate(String date1, String date2) {
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        boolean bool = false;
        try {
            if (newDate.parse(date1).before(newDate.parse(date2))) bool = true;
            else if (newDate.parse(date1).equals(newDate.parse(date2))) bool = true;
            else bool = false;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return bool;
    }
}
