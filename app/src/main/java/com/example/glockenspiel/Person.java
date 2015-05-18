package com.example.glockenspiel;


import android.content.Context;


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

    protected Person load_person(String name, Context context){
        return this;
    }

    protected void save_person(Context context){

    }
}
