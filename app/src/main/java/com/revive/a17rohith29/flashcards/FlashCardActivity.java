package com.revive.a17rohith29.flashcards;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class FlashCardActivity extends AppCompatActivity {

    private int index;
    private String topic;
    private String fileName;
    private ArrayList<String> front;
    private ArrayList<String> back;
    boolean frontPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        //
        frontPage = true;

        back = new ArrayList<>();
        front = new ArrayList<>();

        // get the topic
        Intent i = getIntent();
        topic = i.getStringExtra("topic");
        fileName = topic + "Info.txt";
        index = 0;

        // creating file
        try {
            PrintStream out = new PrintStream(openFileOutput(fileName, MODE_APPEND));
            out.close();
        } catch (FileNotFoundException e) {
            Log.wtf("Error: ", "Wrong File");
        }

        // put text in heading
        ((TextView) findViewById(R.id.mainTopic)).setText(topic);

        read();
        display();
    }

    public void back(View view) {
        finish();
    }

    public void next(View view) {
        index = (index + 1) % front.size();

        frontPage = true;
        display();
    }

    public void add(View view) {
        Intent i = new Intent(this, addFlash.class);
        i.putExtra("filename", fileName);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            boolean b = data.getBooleanExtra("added", true);
            read();
            display();
        }
        catch (Exception e){
            Log.wtf("why", "fail Exception");
        }
    }

    private void display() {
        // displays current index
        TextView container = (TextView) findViewById(R.id.container);

        if (index + 1 > front.size()) {
            container.setText("");
        } else {
            if (frontPage) {
                container.setBackgroundColor(Color.rgb(135,206,250));
                container.setText(front.get(index));
            } else {
                container.setBackgroundColor(Color.BLUE);
                container.setText(back.get(index));
            }
        }
    }

    private void read() {
        // first clearing the arraylist
        front.clear();
        back.clear();

        // getting all data from file and storing it in topics
        try {
            Scanner scan = new Scanner(openFileInput(fileName));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] info = line.split("->");

                if (info.length == 2) {
                    front.add(info[0]);
                    back.add(info[1]);
                }
            }

            scan.close();
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "Wrong File");
        }
    }

    public void turn(View view) {
        if (index + 1 <= front.size()) {
            frontPage = !frontPage;
            display();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        read();
        display();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putBoolean("frontPage", frontPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt("index");
        frontPage = savedInstanceState.getBoolean("frontPage");
    }
}
