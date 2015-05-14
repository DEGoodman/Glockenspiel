package com.example.glockenspiel;

import java.util.ArrayList;

/**
 * Created by erik on 5/8/15.
 */
public class Pattern {
    private String rhythm;
    private String notes;
    public int length;

    public Pattern (String nums, String notes) {
        this.rhythm = nums;
        this.notes = notes;
    }

    public void setRhythm(String rhythm) {
        this.rhythm = rhythm;
    }

    public void addRythym(String num){
        this.rhythm += num;
    }

    public String getRhythms(){
        return rhythm;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addNote(String note){
        this.notes += note;
    }

    public String getNotes(){
        return notes;
    }
}
