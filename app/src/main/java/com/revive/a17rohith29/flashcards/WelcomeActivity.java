package com.revive.a17rohith29.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class WelcomeActivity extends AppCompatActivity {

    private ArrayList<String> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        // initializing the topics list
        topics = new ArrayList<>();

        // creating the file if it does not exist
        try {
            PrintStream out = new PrintStream(openFileOutput("topics.txt", MODE_APPEND));
            out.close();
        } catch (FileNotFoundException e) {
            Log.wtf("Error: ", "Wrong File");
        }

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

        // adding event listners to the listview
        ListView list = (ListView) findViewById(R.id.topics);


        // long click delete by creating dialog box
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);

                String fileName = adapterView.getItemAtPosition(i).toString() + "Info.txt";

                builder.setTitle("Delete Topic");
                builder.setMessage("Are You Sure that You Want to delete Flashcard " + adapterView.getItemAtPosition(i).toString());

                builder.setNegativeButton("NO",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // code to run when OK is pressed
                        return;
                    }
                });

                class onClick implements DialogInterface.OnClickListener {
                    int i;
                    String fileName;

                    public onClick(int x, String file_name) {
                        i = x;
                        fileName = file_name;
                    }

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //
                        topics.remove(i);

                        Log.i("info", "onClick: ");
                        // put all info back onto the file
                        try {
                            PrintStream out = new PrintStream(openFileOutput("topics.txt", MODE_PRIVATE));

                            for(String s : topics) {
                                out.println(s);
                            }

                            out.close();
                        } catch (FileNotFoundException e) {
                            Log.e("Error: ", "Wrong File");
                        }

                        // creating file
                        try {
                            PrintStream out = new PrintStream(openFileOutput(fileName, MODE_PRIVATE));
                            out.close();
                        } catch (FileNotFoundException e) {
                            Log.wtf("Error: ", "Wrong File");
                        }

                        display();
                    }
                }

                builder.setPositiveButton("YES",  new onClick(i, fileName));



                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        // short click to jump to view activity
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Jumping to flashcard activity
                Intent intent = new Intent(WelcomeActivity.this, FlashCardActivity.class);
                String topic = adapterView.getItemAtPosition(i).toString().toUpperCase();
                intent.putExtra("topic", topic);
                startActivity(intent);
            }
        });

        // displaying the topic list
        display();
    }

    private void display() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, topics) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                tv.setTextColor(Color.rgb(0, 0, 255));

                tv.setGravity(Gravity.CENTER);

                // Return the view
                return view;

            }
        };

        ListView list = (ListView) findViewById(R.id.topics);
        list.setAdapter(adapter);
    }

    public void addTopic(View view) {
        // add new and return result
        // to make sure that there is no conflict
        Intent i = new Intent(this, AddTopicsActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String topic = data.getStringExtra("topic");
            topics.add(topic);

            Toast.makeText(this, "Topic " + topic + " has been Added!", Toast.LENGTH_SHORT).show();
            display();
        } catch (NullPointerException e){
        }
    }
}
