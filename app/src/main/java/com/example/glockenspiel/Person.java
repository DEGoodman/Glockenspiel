package com.example.glockenspiel;


import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
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
                    Log.d("loaded file, name:",line);
                    this.score = new Integer(bir.readLine());
                    Log.d("loaded file, score:",line);
                    this.level = bir.readLine();
                    Log.d("loaded file, level:",line);
                    this.pattern = new Integer(bir.readLine());
                    Log.d("loaded file, pattern:",line);
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

    protected void save_person(Context context) throws IOException {
        // to change file in-place, we are reading whole file, making changes, then writing whole file.
        File old = new File("persons.txt");
        FileInputStream fIn = context.openFileInput("persons.txt");
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader bir = new BufferedReader(isr);

        File temp = File.createTempFile("tempfile", ".tmp");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

        String line = null;
        try {
            while ( (line=bir.readLine()) != null) {
                if (line.compareTo(this.name)==0){ // we found a user!
                    bw.write(this.name);
                    this.score = new Integer(bir.readLine());
                    bw.write(this.score);
                    this.level = bir.readLine();
                    bw.write(this.level);
                    this.pattern = new Integer(bir.readLine());
                    bw.write(this.pattern);
                } else {
                    bw.write(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fIn.close();
        isr.close();
        bir.close();
        bw.close();

        // delete old file
        old.delete();
        //rename temp file to old filename
        boolean success = temp.renameTo(old);
    }
}
