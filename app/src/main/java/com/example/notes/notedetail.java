package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetail extends AppCompatActivity {

    FloatingActionButton editbtn;
    TextView detailheading,detailnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);



        View backgroundImage = findViewById(R.id.detailbg);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);


        detailheading = findViewById(R.id.detailheading);
        detailnote= findViewById(R.id.detailnote);
        editbtn = findViewById(R.id.editButton);

        Intent data = getIntent();


        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notedetail.this,editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("id",data.getStringExtra("id"));
                startActivity(intent);

            }
        });

        detailnote.setText(data.getStringExtra("title"));
        detailheading.setText(data.getStringExtra("content"));








    }
    @Override
    public void onBackPressed(){

        finish();
        Intent intent = new Intent(notedetail.this, Home.class);
        startActivity(intent);
    }



}