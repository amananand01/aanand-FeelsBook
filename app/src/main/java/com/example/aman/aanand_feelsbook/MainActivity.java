package com.example.aman.aanand_feelsbook;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.aman.aanand_feelsbook.MESSAGE";
    public static final String FILENAME = "EmotionHistory.sav";
    public static final String FILENAME_1 = "EmotionCount.sav";
    public ListView oldEmotions;


    public static HashMap<String,Integer> feelingCount = new HashMap<String,Integer>();

    private ArrayAdapter<Emotions> adapter;
    public static ArrayList<Emotions> emotions = new ArrayList<Emotions>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oldEmotions = (ListView) findViewById(R.id.old_emotions);

        oldEmotions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // show dialog box for comment section
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Feels Book");
                builder.setMessage("Please select any one of the options");

                builder.setPositiveButton("Edit Feeling", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, EditEmotion.class);
                        intent.putExtra(EXTRA_MESSAGE, Integer.toString(position));
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Delete Feeling", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // decrement the feeling count
                        String feeling = emotions.get(position).getFeeling();
                        feelingCount.put(feeling,feelingCount.get(feeling)-1);

                        // delete feeling from list
                        emotions.remove(position);

                        // save new list and count of emotions
                        try{

                            FileOutputStream fos = openFileOutput(FILENAME,0);
                            saveInFile(fos,FILENAME);
                            FileOutputStream fos_1 = openFileOutput(FILENAME_1,0);
                            saveInFile(fos_1,FILENAME_1);

                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();

                        Toast.makeText(MainActivity.this,"Feeling: "+ ".." + " Deleted",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

            }
        });
    }

    // dialog box code : https://www.youtube.com/watch?v=gLbH5bAHCiY
    // called when the user clicks any emotion and takes to the comments page
    public void SaveEmotion(View view){

        Button btn = (Button) findViewById(view.getId());
        final String text = btn.getText().toString();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date_formatted = date.format(new Date());

        final Emotions currEmotion = new Emotions(date_formatted);

        currEmotion.setComment("");
        currEmotion.setFeeling(text);

        // update the emotion ArrayList
        emotions.add(currEmotion);

        // update the emotion count
        if(feelingCount.get(text)!=null){
            feelingCount.put(text,  feelingCount.get(text)+1);
        }else{
            feelingCount.put(text, 1);
        }

        // update the list view
        adapter.notifyDataSetChanged();

        // save emotion & count in file
        try{
            FileOutputStream fos = openFileOutput(FILENAME,0);
            saveInFile(fos,FILENAME);
            FileOutputStream fos_1 = openFileOutput(FILENAME_1,0);
            saveInFile(fos_1,FILENAME_1);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // show dialog box for comment section
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Feels Book");
        builder.setMessage("Do you want to add any comments ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, DisplayCommentActivity.class);
                intent.putExtra(EXTRA_MESSAGE, text);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"Feeling: "+text+ " Saved",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();

        // Adapter between listview and tweets
        adapter = new ArrayAdapter<Emotions>(this,
                R.layout.list_item, emotions);
        oldEmotions.setAdapter(adapter);
    }

    public void count_feelings(View view){
        // show dialog box for comment section
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Count of Emotions Recorded");

        String message = "";

        for (String key : feelingCount.keySet()) {
            if(feelingCount.get(key)>0) message=message + key + " : " + feelingCount.get(key) + "\n\n";
        }
        if(message.equals("")) message = "No Emotion Recorded.";

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void loadFromFile() {
        try {

            //load the emotions
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            Gson gson = new Gson();
            // creates a type
            Type listType = new TypeToken<ArrayList<Emotions>>(){}.getType();
            emotions = gson.fromJson(reader, listType);

            //load the emotion count
            FileInputStream fis_1 = openFileInput(FILENAME_1);
            InputStreamReader isr_1 = new InputStreamReader(fis_1);
            BufferedReader reader_1 = new BufferedReader(isr_1);

            Gson gson_1 = new Gson();
            Type listType_1 = new TypeToken<HashMap<String,Integer>>(){}.getType();
            feelingCount  = gson_1.fromJson(reader_1, listType_1);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            emotions = new ArrayList<Emotions>();
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void saveInFile(FileOutputStream fos, String FileName){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);

            Gson gson =new Gson();

            if(FileName.equals(FILENAME)) gson.toJson(emotions,writer);
            else if(FileName.equals(FILENAME_1)) gson.toJson(feelingCount ,writer);

            writer.flush();
            fos.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
