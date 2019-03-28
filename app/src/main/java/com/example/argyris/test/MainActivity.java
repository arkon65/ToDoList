package com.example.argyris.test;

//Libraries assigned for the project
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


//Our main class
public class MainActivity extends AppCompatActivity {

//Variables necessary for the firebase function, globally declared
    FirebaseDatabase database;
    DatabaseReference myRef;




    private ArrayAdapter<String> adapter; //Variable declared for the list adapter
    private ArrayList<String> arrayList; // Variable declared for the list
    private ListView list; //Variable declared for the listView assignment

    Button mButton;//Variable for the registration button assignment
    EditText mEdit;//Variable for the edit text assignment
    public String getText; //Variable that gets the input text from the editText
    String value;//Variable that gets user input and puts it in the list
    boolean flag = true; //Flag variable that controls



    @Override
    protected void onCreate(Bundle savedInstanceState) {//class constructor

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Sets layout for the current activity
        setTitle("Welcome to MyToDoList"); //sets app title

        final Context context = this; //assigns context to "this" for later use
        final String android_id = Build.SERIAL; //gets phones serial number for authentication
        database = FirebaseDatabase.getInstance(); //gets Firebase instance
        myRef = database.getReference(android_id);//gets Firebase reference


        mButton = (Button) findViewById(R.id.buttonRegister); //assigns button to the variable
        mEdit = (EditText) findViewById(R.id.entryText);//assigns editText to this variable
        list = (ListView) findViewById(R.id.listView);//assigns listView to this variable
        arrayList = new ArrayList<String>();//creates arraylist





        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.listlayout, arrayList);//assigns adapter to our list and our specific layout, with which we change the list's content structure
        list.setAdapter(adapter); //assigns adapter to listView

        myRef.addValueEventListener(new ValueEventListener() { //event listener, which does something when data is added in the database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//gets dataSnapshot, which includes our data
               for (DataSnapshot ps : dataSnapshot.getChildren()) {// loop, with which we show all user's entries in the database
                   if (flag == false) {//if this has happened once, the loop does not happen
                       break;
                   }
                   value = ps.getValue(String.class);//gets snapshot's data
                   arrayList.add("+  " + value);//enters data into our array

                   adapter.notifyDataSetChanged(); //notifies adapter and enters data in our list



               }
               flag = false;//makes flag false, after first loop completes

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Oops...")
                        .setMessage("Something went wrong with your connection, please try again")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) { Toast.makeText(MainActivity.this, "OK Pressed", Toast.LENGTH_SHORT).show();
                                    }});

                builder.create().show();
            }
        });


        mButton.setOnClickListener(new View.OnClickListener(){//click listener for our register button

        @Override
        public void onClick(View v) {//when button is clicked, content of method is executed

            getText = mEdit.getText().toString(); //draws user input from editText
            if (getText.equals("")) {//checks if input is empty, in which case it shows a pop up window and does not allow data entry in our database

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Empty Entry")
                        .setMessage("Maybe you should add something before pressing this")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                });

                builder.create().show();
            } else { //if entry is correct, data is registered
                myRef.push().setValue(getText);
                arrayList.add("+  " + getText);
                mEdit.setText("");
                adapter.notifyDataSetChanged();
            }
        }
        });

        }
    }

