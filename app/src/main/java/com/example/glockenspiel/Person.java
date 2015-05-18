package com.example.glockenspiel;


import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by erik on 5/9/15.
 */
public class Person {
    String name;
    int score;
    String level;
    int pattern;

    public Person(String name){
        this.name = name;
        this.score = 0;
        this.level = "Primer";
        this.pattern = 0;
    }

    protected void update_score (int s){
        this.score = s;
    }

    protected void update_pattern (int p){
        this.pattern = p;
    }

    protected void update_level (String l){
        this.level = l;
    }

    protected Person load_person(Context context) throws FileNotFoundException {
        FileInputStream fIn = context.openFileInput("persons.txt");
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader bir = new BufferedReader(isr);

        String line = null;
        try {
            while ( (line=bir.readLine()) != null) {
                if (line.compareTo(this.name)==0){ // we found a user!
                    this.score = new Integer(bir.readLine());
                    this.level = bir.readLine();
                    this.pattern = new Integer(bir.readLine());
                    fIn.close();
                    isr.close();
                    bir.close();
                    return this;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this; // if user not found, use new user
    }

    protected void save_person() throws FileNotFoundException {
        FileInputStream fIn = openFileOutput("persons.txt");
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader bir = new BufferedReader(isr);
        FileWriter fr = new FileWriter(fIn);
        BufferedWriter br  = new BufferedWriter(fr);


        String line = null;
        try {
            while ( (line=bir.readLine()) != null) {
                if (line.compareTo(this.name)==0){ // we found a user!


                    fIn.close();
                    isr.close();
                    bir.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
