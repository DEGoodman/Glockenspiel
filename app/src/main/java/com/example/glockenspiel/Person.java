package com.example.glockenspiel;

/**
 * Created by erik on 5/9/15.
 */
public class Person {
    String name;
    int score;

    public Person(String name){
        this.name = name;
        this.score = 0;
    }

    public void update (int s){
        this.score = s;
    }

    public Person load_person(String name){
        return this;
    }

    public void save_person(){

    }
}
