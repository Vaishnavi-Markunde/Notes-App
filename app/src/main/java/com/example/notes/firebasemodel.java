package com.example.notes;

import androidx.cardview.widget.CardView;

public class firebasemodel {

    private String HeadingText;
    private String NoteText;

    public firebasemodel(){

    }    public firebasemodel(String HeadingText,String NoteText){
         this.HeadingText = HeadingText; this.NoteText = NoteText;

    }
    public String getHeadingText() {
        return HeadingText;
    }

    public void setHeadingText(String headingText) {
        HeadingText = headingText;
    }

    public String getNoteText() {
        return NoteText;
    }

    public void setNoteText(String noteText) {
        NoteText = noteText;
    }



}
