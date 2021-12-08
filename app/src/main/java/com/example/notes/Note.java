package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Note extends AppCompatActivity {
    EditText heading,note;
  //  String headingText, noteText;
    FloatingActionButton savebtn;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public Note(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);

        View backgroundImage = findViewById(R.id.notebg);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);

        heading = findViewById(R.id.editheading);
        note = findViewById(R.id.editNote);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth= firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();


        savebtn = findViewById(R.id.saveButton);

        savebtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String headingText = heading.getText().toString();
                String noteText = note.getText().toString();



                if(headingText.isEmpty()||noteText.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Blank Fields not allowed", Toast.LENGTH_SHORT).show();
                   // Toast.makeText(this,"Signed in successfully! ",Toast.LENGTH_SHORT).show();

                }
                else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("MyNote").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("HeadingText",headingText);
                    note.put("NoteText",noteText);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note added successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Note.this,Home.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to create Note!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //database = FirebaseDatabase.getInstance();
                   // DatabaseReference myRef = database.getReference("notes");

                  //  myRef.setValue(new Note(headingText,noteText));



                }
            }
        });

       // public Note(String)

    }
}