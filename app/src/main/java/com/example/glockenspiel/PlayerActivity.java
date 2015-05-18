package com.example.glockenspiel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.io.File;

public class PlayerActivity extends Activity implements OnTouchListener {
    // Variables
    private MediaPlayer background_music;
    private Button a, b, c, d, e, f, g, c2, start_btn;
    private CharSequence playKey;
    public SoundPool mySoundPool;
    private boolean loaded = false;
    private int keya_sound, keyb_sound, keyc_sound, keyd_sound, keye_sound;
    private int keyf_sound, keyg_sound, keyc2_sound;
    private AudioManager audioManager;
    private float actualVolume, maxVolume, volume;
    private Sequence patterns;
    private boolean startRecord = false;
    private AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    private String value;
    public static final Object monitor = new Object();
    public static boolean monitorState = false;
    private boolean loop;
    private LinkedList<int[]> indices;
    private File data;
    private Context context;
    protected Person person;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context = this;

        // Global data handler
        try {
            data = new File(context.getFilesDir(), "persons.txt");
        } catch (Exception e){
            final String NEWFILE = new String("File for saving User data");
            FileOutputStream fOut = null;
            try {
                fOut = openFileOutput("persons.txt",MODE_PRIVATE);
                fOut.write(NEWFILE.getBytes());
                fOut.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        // get level info
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("selected");
        }

        alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Selected Level");
        alertDialogBuilder.setMessage(value);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //get user info
        person = new Person(extras.getString("name"));

        try {
            person.load_person(context);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        //play ambiance background music
        float leftVolume = (float) 0.1;
        float rightVolume = (float) 0.1;
        background_music = MediaPlayer.create(PlayerActivity.this, R.raw.beach_ambience);
        background_music.setVolume(leftVolume, rightVolume);
        background_music.setLooping(true);
        background_music.start();

        // Initialize SoundPool
        mySoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mySoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool mySoundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        // Getting the user sound settings
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;

        // Load audio files
        keya_sound = mySoundPool.load(this, R.raw.a, 1);
        keyb_sound = mySoundPool.load(this, R.raw.b, 1);
        keyc_sound = mySoundPool.load(this, R.raw.c1, 1);
        keyd_sound = mySoundPool.load(this, R.raw.d, 1);
        keye_sound = mySoundPool.load(this, R.raw.e, 1);
        keyf_sound = mySoundPool.load(this, R.raw.f, 1);
        keyg_sound = mySoundPool.load(this, R.raw.g, 1);
        keyc2_sound = mySoundPool.load(this, R.raw.c2, 1);

        //set up keyboard
        populateKeys(); //<<----- initialize keys function

        //load sequence
        patterns = new Sequence(context, value);
        // start = Button(); We still need to set this
        start_btn = (Button) findViewById(R.id.start_btn);
    }


    /**
     * *********************************
     * Helper method to layout keyboard *
     * **********************************
     */
    private void populateKeys() {
        //initialize buttons
        c = (Button) findViewById(R.id.key_c);
        d = (Button) findViewById(R.id.key_d);
        e = (Button) findViewById(R.id.key_e);
        f = (Button) findViewById(R.id.key_f);
        g = (Button) findViewById(R.id.key_g);
        a = (Button) findViewById(R.id.key_a);
        b = (Button) findViewById(R.id.key_b);
        c2 = (Button) findViewById(R.id.key_c2);

        //assign each sound to key
        setOnClickListener(c, keyc_sound);
        setOnClickListener(d, keyd_sound);
        setOnClickListener(e, keye_sound);
        setOnClickListener(f, keyf_sound);
        setOnClickListener(g, keyg_sound);
        setOnClickListener(a, keya_sound);
        setOnClickListener(b, keyb_sound);
        setOnClickListener(c2, keyc2_sound);
    }


    /**
     * *************************************
     * Helper method to set onClickListener *
     * **************************************
     */
    private void setOnClickListener(final Button button, final int key_player) {
        final boolean showToast = false; //toggle toast message
        playKey = null;
        button.setSoundEffectsEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (loaded) {
                    mySoundPool.play(key_player, volume, volume, 1, 0, 1f);
                    Layout label = button.getLayout();
                    playKey = label.getText(); //<<----- key(s) get to played

                    //display toast message when key pressed
                    if (showToast) {
//                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, playKey, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    /******************
     * start sequence *
     ******************/
    public void startSequence(View view) {

        //* fix this
        //play sequence
//      if (patterns.getPatterns().size() > 0){
//            for (Pattern p: patterns.getPatterns())
//                play_pattern(p);
//      } else {
//            alertDialogBuilder.setTitle("No sequences to play!");
//            alertDialogBuilder.setMessage("Something went wrong. Try another level.");
//            alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        }

        patternIndex(patterns.getPatterns().get(0));
    }

    private void patternIndex(Pattern p) {
        String[][] notes = p.getNotes();
        int[][] rhythm = p.getRhythms();
        // check length of the inner arrays
        if (notes[0].length != rhythm[0].length) {
            throw new RuntimeException("Arrays are not equal length!");
        }
        indices = new LinkedList();

        /* 'rhythm' controls outer loop.
         * Each 'rhythm' cycles through all notes before rhythm changes
         */
        for (int k = 0; k < rhythm.length; k++) {
            // outer 'notes' loop
            for (int i = 0; i < notes.length; i++) {
                int[] arr = new int[2];
                arr[0] = k;
                arr[1] = i;
                indices.add(arr);
            }
        }
        // if 'Primer', loop through note sequences in order
        if (value.compareTo("Primer") == 0) {
            playPattern(indices.pop(), p);
        }
    }

    public void playPattern(final int[] index, final Pattern p) {
        String playData = String.valueOf(index[0]) + String.valueOf(index[1]);
        Log.d("current play data: ", playData);
        try {
            person.save_person(this);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        LinkedList notes = p.getNSequence(index[1]);
        LinkedList rhythm = p.getRSequence(index[0]);
        // inner 'notes' loop
        for (int j = 0; j <= notes.size(); j++) {
            String n = String.valueOf(notes.pop());
            Log.d("key: ", n);
            int key = 0;
            switch (n) {
                case "C":
                    key = keyc_sound;
                    break;
                case "D":
                    key = keyd_sound;
                    break;
                case "E":
                    key = keye_sound;
                    break;
                case "F":
                    key = keyf_sound;
                    break;
                case "G":
                    key = keyg_sound;
                    break;
                case "A":
                    key = keya_sound;
                    break;
                case "B":
                    key = keyb_sound;
                    break;
                case "C2":
                    key = keyc2_sound;
                    break;
                default:
                    continue;
            }
            // loop for rhythm
            mySoundPool.play(key, volume, volume, 1, 0, 1f);
            int r = (Integer) rhythm.get(j);
            Log.d("rhythm: ", String.valueOf(r));
            try {
                Thread.sleep(r * 500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        Pattern results = record();
        Boolean pass = compare_play_and_record(p, results);
        if (!pass) {
            alertDialogBuilder.setTitle("Pattern not passed.");
            alertDialogBuilder
                    .setMessage("Would you like to try again?")
                    .setPositiveButton("Ok, one more time", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, restart activity
                            alertDialog.dismiss();
                            playPattern(index, p);
                            return;
                        }
                    })
                    .setNegativeButton("No thanks, let me move on", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, continue on
                            alertDialog.dismiss();
                            // TODO: check if more indices exist. If not, goto next pattern
                            // If no more patterns, level is complete!
                            playPattern(indices.pop(), p);
                        }
                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            alertDialogBuilder.setTitle("Congratulations, you passed!");
            alertDialogBuilder
                    .setMessage("Would you like replay this level?")
                    .setPositiveButton("Ok, one more time", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, restart activity
                            alertDialog.dismiss();
                            playPattern(index, p);
                            return;
                        }
                    })
                    .setNegativeButton("Bring on the next challenge!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, continue on
                            alertDialog.dismiss();
                            // TODO: check if more indices exist. If not, goto next pattern
                            // If no more patterns, level is complete!
                            playPattern(indices.pop(), p);
                        }
                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    private Pattern record() {
        Pattern p = new Pattern(null, null);
        return p;
    }

    private boolean compare_play_and_record(Pattern p, Pattern r) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        return false;
    }
}
