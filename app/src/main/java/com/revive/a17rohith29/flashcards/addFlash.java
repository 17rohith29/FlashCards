package com.revive.a17rohith29.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class addFlash extends AppCompatActivity {

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flash);

        Intent i = getIntent();
        fileName = i.getStringExtra("filename");
    }

    public void cancel(View view) {
        Intent i = new Intent();
        setResult(1, i);
        i.putExtra("added", false);
        finish();
    }

    public void add(View view) {

        String front = ((EditText) findViewById(R.id.front)).getText().toString();
        String back = ((EditText) findViewById(R.id.back)).getText().toString();

        // add part
        // adding info
        if (!front.equals("") && !back.equals("")) {
            try {
                PrintStream out = new PrintStream(openFileOutput(fileName, MODE_APPEND));
                out.append(front + "->" + back + '\n');
                out.close();
            } catch (FileNotFoundException e) {
                Log.wtf("Error: ", "Wrong File");
            }

            Intent i = new Intent();
            i.putExtra("added", true);
            setResult(1, i);
            finish();
        }
        else {
            Toast.makeText(this, "Please enter the information", Toast.LENGTH_SHORT).show();
        }
    }
}
