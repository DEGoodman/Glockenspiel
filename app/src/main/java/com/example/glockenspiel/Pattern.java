package com.example.glockenspiel;

import java.util.ArrayList;
import java.util.LinkedList;

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

    public int[][] getRhythms(){
        return rhythm;
    }

    public LinkedList getRSequence(int i){
        LinkedList seq = new LinkedList();
        for (int j = 0; j < this.rhythm[0].length; j++){
            seq.add(this.rhythm[i][j]);
        }
        return seq;
    }

    public String[][] getNotes(){
        return notes;
    }

    public LinkedList getNSequence(int i){
        LinkedList<String> seq = new LinkedList();
        for (int j = 0; j < this.notes[0].length; j++){
            seq.add(this.notes[i][j]);
        }
        return seq;
    }
}
