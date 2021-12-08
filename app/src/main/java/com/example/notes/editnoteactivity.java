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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {
    Intent data;
    EditText editnewheading,editnewnote;
    FloatingActionButton savenewbtn;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);

        View backgroundImage = findViewById(R.id.editnotebg);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);

        data = getIntent();
        editnewheading = findViewById(R.id.editnewheading);
        editnewnote = findViewById(R.id.editnewNote);
        savenewbtn=findViewById(R.id.savenewButton);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth= firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();


        String title = data.getStringExtra("title");
        String note =data.getStringExtra("content");

        editnewheading.setText(title);
        editnewnote.setText(note);

        savenewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newTitle = editnewheading.getText().toString();
                String newNote = editnewnote.getText().toString();

                if(newTitle.isEmpty()||newNote.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Blank Fields not allowed", Toast.LENGTH_SHORT).show();

                }
                else{

                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(
                            firebaseUser.getUid()).collection("MyNote").document(data.getStringExtra("id"));
                    Map<String,String> note = new HashMap<>();
                    note.put("HeadingText",newTitle);
                    note.put("NoteText",newNote);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note updates successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,Home.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update Note!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

               // editnewheading.setText();
            }
        });



    }
}