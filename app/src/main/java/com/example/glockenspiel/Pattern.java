package com.example.glockenspiel;

import java.util.ArrayList;

/**
 * Created by erik on 5/8/15.
 */
public class Pattern extends ArrayList {
    private int[][] rhythm;
    private String[][] notes;

    public Pattern (int[][] nums, String[][] notes) {
        this.rhythm = nums;
        this.notes = notes;
    }
    public void setRhythm(int[][] rhythm) {
        this.rhythm = rhythm;
    }
//    public void addRythym(Integer num){
//        this.rhythm.add(num);
//    }
    public int[][] getRhythms(){
        return rhythm;
    }


    public void setNotes(String[][] notes) {
        this.notes = notes;
    }

//    public void addNote(String note){
//        this.notes.add(note);
//    }

    public String[][] getNotes(){
        return notes;
    }
}
