package com.example.aman.aanand_feelsbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.aman.aanand_feelsbook.MESSAGE";
    private static final String FILENAME = "EmotionHistory.sav";
    private ListView oldEmotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // dialog box code : https://www.youtube.com/watch?v=gLbH5bAHCiY
    // called when the user clicks any emotion and takes to the comments page
    public void SaveEmotion(View view){

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



}
