package com.example.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Random;

public class Home extends Activity {
    private DatabaseReference mDatabase;

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;

    FloatingActionButton btn;
    private long pressedTime;


    StaggeredGridLayoutManager layoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        View backgroundImage = findViewById(R.id.homebg);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();

        btn = findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Note.class));
            }
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                .collection("MyNote").orderBy("HeadingText", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @SuppressLint("ResourceType")
            @Override
            protected void onBindViewHolder(NoteViewHolder noteViewHolder, int i, firebasemodel firebasemodel) {
                int n = getRandomno();
                int darkcode = getRandomColor(n);
                int lightcode = getLightColor(n);

                noteViewHolder.itemView.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(lightcode));
                noteViewHolder.title.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(darkcode));

                noteViewHolder.title.setText(firebasemodel.getHeadingText());
                noteViewHolder.note.setText(firebasemodel.getNoteText());

                ImageButton edit = noteViewHolder.itemView.findViewById(R.id.edit);
                ImageButton delete=  noteViewHolder.itemView.findViewById(R.id.delete);


                String id = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Home.this,notedetail.class);
                        intent.putExtra("title", firebasemodel.getHeadingText());
                        intent.putExtra("content",firebasemodel.getNoteText());
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this,editnoteactivity.class);
                        intent.putExtra("title", firebasemodel.getHeadingText());
                        intent.putExtra("content",firebasemodel.getNoteText());
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(Home.this, "Delete clicked", Toast.LENGTH_SHORT).show();
                       DocumentReference documentReference =firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                          .collection("MyNote").document(id);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Home.this, "Deleted Succesfully !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Home.this, "Delete unsuccessful !", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }


        };


        recyclerView = findViewById(R.id.rec);

        recyclerView.hasFixedSize();

        btn = findViewById(R.id.addButton);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(noteAdapter);



    }

    private int getLightColor(int n) {
        int arr[] ={R.color.pink,R.color.red,R.color.blue,R.color.green,R.color.violet,R.color.orange};

       return arr[n];

    }
    private int getRandomno(){
        Random r = new Random();
        int n = r.nextInt(5);
        return n;
    }

    private int getRandomColor(int n) {
        int arr[] = {R.color.dpink,R.color.dred,R.color.dblue,R.color.dgreen,R.color.dviolet,R.color.dorange};
        return arr[n];

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
            //TextView
            TextView title ;
            TextView note ;
            LinearLayout layout;
            public NoteViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.savedHeading);
                note = itemView.findViewById(R.id.savedNote);
                layout = itemView.findViewById(R.id.laynote);

            }
        }







    @Override
        protected void onStart() {
            super.onStart();
            noteAdapter.startListening();
        }

    @Override protected void onStop()
    {
        super.onStop();
        noteAdapter.stopListening();
    }


       // recyclerView.addItemDecoration(new Note(note));
        //recyclerView.add

    public void onBackPressed() {


        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }





}
