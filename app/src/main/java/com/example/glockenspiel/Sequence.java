package com.example.glockenspiel;

/**
 * Created by erik on 5/8/15.
 */
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Sequence extends ArrayList {
    private ArrayList<Pattern> patterns;

    public Sequence(Context context, String seq_name) {
        this.patterns = new ArrayList<Pattern>();
        ;
        //get json
        AssetManager assetManager = context.getAssets();
        InputStream input;
        String json = "";
        try {
            input = assetManager.open(seq_name + ".json");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            json = new String(buffer);  //byte buffer into a string
        } catch (IOException e) {
            e.printStackTrace();
        }

        // sequence deserialization
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jSequence = jObject.getJSONArray("sequence");
            // sequentially get JSON data
            Log.d("begin array deserial.", "beginning first loop");
            for (int i = 0; i < jSequence.length(); i++) {
                JSONObject jRealObject = jSequence.getJSONObject(i);
                JSONArray jRhythmArray = jRealObject.getJSONArray("rhythm");
                JSONArray jNotesArray = jRealObject.getJSONArray("notes");

                String rhythms = jRhythmArray.toString();
                String notes = jNotesArray.toString();
                Pattern pat = new Pattern(rhythms, notes);
                patterns.add(pat);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Pattern> getPatterns(){
        return this.patterns;
    }

}
