package com.example.aman.aanand_feelsbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.aman.aanand_feelsbook.MESSAGE";
    private static final String FILENAME = "EmotionHistory.sav";
    private ListView oldEmotions;

    private ArrayAdapter<Emotions> adapter;
    private ArrayList<Emotions> emotions = new ArrayList<Emotions>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // dialog box code : https://www.youtube.com/watch?v=gLbH5bAHCiY
    // called when the user clicks any emotion and takes to the comments page
    private void SaveEmotion(View view){


        // save emotion
        saveInFile();

        // show dialog box for comment section
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        Button btn = (Button) findViewById(view.getId());
        final String text = btn.getText().toString();

        builder.setTitle("Feels Book");
        builder.setMessage("Do you want to add any comments?");

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

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            Gson gson = new Gson();
            // creates a type
            Type listTweetType = new TypeToken<ArrayList<Emotions>>(){}.getType();
            emotions = gson.fromJson(reader, listTweetType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            emotions = new ArrayList<Emotions>();
            e.printStackTrace();
        }
    }

    private void saveInFile(){
        try {

            FileOutputStream fos = openFileOutput(FILENAME,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);

            Gson gson =new Gson();
            gson.toJson(emotions,writer);
            writer.flush();

            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
