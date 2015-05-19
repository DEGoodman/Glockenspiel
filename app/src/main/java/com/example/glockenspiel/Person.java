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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Created by erik on 5/9/15.
 */
public class Person {
    String name;
    int score;
    String level;
    int pattern;
    int index;

    public Person(String name, String level){
        this.name = name;
        if (name.compareTo("") == 0) {
            this.name = "temp";
        }
        this.score = 0;
        this.level = level;
        this.pattern = 0;
        this.index = 0;
    }

    protected void update_score (int s) { this.score = s; }

    protected void update_pattern (int p){
        this.pattern = p;
    }

    protected void update_level (String l){
        this.level = l;
    }

    protected void update_index (int i){this.index = i;}

    protected Person load_person(Context context) throws FileNotFoundException {
        FileInputStream fIn = context.openFileInput("persons.txt");
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader bir = new BufferedReader(isr);

        String line = null;
        try {
            while ( (line=bir.readLine()) != null) {
                if (line.compareTo(this.name)==0){ // we found the user!
                    Log.i("loaded file, name:",line);
                    this.score = new Integer(bir.readLine());
                    Log.i("loaded file, score:",line);
                    this.level = bir.readLine();
                    Log.i("loaded file, level:",line);
                    this.pattern = new Integer(bir.readLine());
                    Log.i("loaded file, pattern:",line);
                    this.index = new Integer(bir.readLine());
                    Log.i("loaded file, index:",line);
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

    protected void save_person(Context context) {
        boolean has_user = false;
        // to change file in-place, we are reading whole file, making changes, then writing whole file.
        File old = new File("persons.txt");
        try {
            FileInputStream fIn = context.openFileInput("persons.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bir = new BufferedReader(isr);

            // read 'keeper' data into temp file
            File temp = File.createTempFile("tempfile", ".tmp");
            FileOutputStream fOut = context.openFileOutput("tempfile.tmp", context.MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            String line = null;
            while ( (line=bir.readLine()) != null) {
                Log.i("Current line:", line);
                if (line.compareTo(this.name) == 0) { // we found the user!
                    Log.i("Hey, I found ", line);
                    has_user = true;
                    osw.write(this.name + "\n");
                    this.score = new Integer(bir.readLine());
                    osw.write(this.score + "\n");
                    this.level = bir.readLine();
                    osw.write(this.level + "\n");
                    this.pattern = new Integer(bir.readLine());
                    osw.write(this.pattern + "\n");
                    this.index = new Integer(bir.readLine());
                    osw.write(this.index + "\n");
                } else {
                    osw.write(line);
                }
            }
            // to save a user not in file
            if (!has_user){
                osw.write(this.name + "\n");
                osw.write(this.score + "\n");
                osw.write(this.level + "\n");
                osw.write(this.pattern + "\n");
                osw.write(this.index + "\n");
            }
            // delete old file
            old.delete();
            //rename temp file to old filename
            boolean success = temp.renameTo(old);

            //cleanup
            fIn.close();
            isr.close();
            bir.close();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String print_person(){
        String p = "";
        p += "name: " + this.name + "\n";
        p += "score: " + this.score + "\n";
        p += "level: " + this.level + "\n";
        p += "index: " + this.index + "\n";
        return p;
    }
}