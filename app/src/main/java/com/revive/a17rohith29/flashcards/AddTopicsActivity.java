package com.revive.a17rohith29.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class AddTopicsActivity extends AppCompatActivity {

    private ArrayList<String> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_topics);

        topics = new ArrayList<>();
        // getting all data from file and storing it in topics
        try {
            Scanner scan = new Scanner(openFileInput("topics.txt"));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                topics.add(line);
            }

            scan.close();
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "Wrong File");
        }
    }

    public void add(View view) {
        EditText txt = (EditText) findViewById(R.id.topicText);
        String topic = txt.getText().toString().toUpperCase();

        if (topics.contains(topic))
            Toast.makeText(this, "Topic " + topic + " is Already Available.", Toast.LENGTH_SHORT).show();
        else if (topic.equals(""))
            Toast.makeText(this, "Please enter a Topic.", Toast.LENGTH_SHORT).show();
        else if (topic.equals("topics"))
            Toast.makeText(this, "topics is a reserved word. Please Enter Another name.", Toast.LENGTH_SHORT).show();
        else {
            // adding topics
            try {
                PrintStream out = new PrintStream(openFileOutput("topics.txt", MODE_APPEND));
                out.append(topic + "\n");
                out.close();
            } catch (FileNotFoundException e) {
                Log.e("Error: ", "Wrong File");
            }

            Intent i = new Intent();
            i.putExtra("topic", topic);
            setResult(1, i);

            finish();
        }

    }

    public void back(View view) {
        finish();
    }
}
