package com.example.glockenspiel;

import java.util.ArrayList;

/**
 * Created by erik on 5/8/15.
 */
public class Pattern extends ArrayList {
    private ArrayList<Integer> rhythm;
    private ArrayList<String> notes;
    public int length;

    public Pattern (ArrayList<Integer> nums, ArrayList<String> notes) {
        this.rhythm = nums;
        this.notes = notes;
    }

    public void setRhythm(ArrayList<Integer> rhythm) {
        this.rhythm = rhythm;
    }

    public void addRythym(Integer num){
        this.rhythm.add(num);
    }

    public String getRhythms(){
        return String.valueOf(rhythm);
    }


    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public void addNote(String note){
        this.notes.add(note);
    }

    public String getNotes(){
        return String.valueOf(notes);
    }
}
