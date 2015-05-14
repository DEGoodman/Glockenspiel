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

                //parse each array for individual lists
                int[][] rhythms = new int[jRhythmArray.length()][jRhythmArray.getJSONArray(0).length()];
                String[][] notes = new String[jNotesArray.length()][jNotesArray.getJSONArray(0).length()];
                if (jRhythmArray != null) {
                    for (int j=0;j<jRhythmArray.length();j++) {
                        try {
                            for (int k = 0; k < jRhythmArray.getJSONArray(j).length(); k++){
                                rhythms[j][k] = jRhythmArray.getJSONArray(j).getInt(k);
                            }
                        } catch (JSONException e){ //do nothing
                            Log.d("JSONException", String.valueOf(jRhythmArray.get(j)));
                        };
                    }
                }
                if (jNotesArray != null) {
                    for (int j=0;j<jNotesArray.length();j++){
                        try {
                            for (int k = 0; k < jNotesArray.getJSONArray(j).length(); k++){
                                notes[j][k] = String.valueOf(jNotesArray.get(j));
                            }
                        } catch (JSONException e){ //do nothing
                        };
                    }
                }
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
