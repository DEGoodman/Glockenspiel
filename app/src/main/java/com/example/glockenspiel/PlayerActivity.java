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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.io.File;
import java.util.Random;

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
    private boolean record;
    Calendar calendar = Calendar.getInstance();
    ArrayList<String> rec_notes;
    ArrayList<Long> rec_rhythm;
    int[] temp_index;
    Pattern temp_p;
    private LinkedList<Pattern> patternSet;
    LinkedList notes;
    LinkedList rhythm;
    private int num_notes;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context = this;
        record = false;

        // read/write data file
        try {
            // catches IOException below
            final String TESTSTRING = new String("Hello Android");

       /* We have to use the openFileOutput()-method
       * the ActivityContext provides, to
       * protect your file from others and
       * This is done for security-reasons.
       * We chose MODE_WORLD_READABLE, because
       *  we have nothing to hide in our file */
            FileOutputStream fOut = openFileOutput("samplefile.txt",MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(TESTSTRING);

       /* ensure that everything is
        * really written out and close */
            osw.flush();
            osw.close();

        //Reading the file back...

       /* We have to use the openFileInput()-method
        * the ActivityContext provides.
        * Again for security reasons with
        * openFileInput(...) */

            FileInputStream fIn = openFileInput("samplefile.txt");
            InputStreamReader isr = new InputStreamReader(fIn);

        /* Prepare a char-Array that will
         * hold the chars we read back in. */
            char[] inputBuffer = new char[TESTSTRING.length()];

            // Fill the Buffer with data from the file
            isr.read(inputBuffer);

            // Transform the chars to a String
            String readString = new String(inputBuffer);

            // Check if we read back the same chars that we had written out
            boolean isTheSame = TESTSTRING.equals(readString);

            Log.i("File Reading stuff", "success = " + isTheSame);

        } catch (IOException ioe)
        {ioe.printStackTrace();}

        //check for data file
        File f= new File("persons.txt");
        if(!f.exists()) {
            try {
                final String STARTSTRING = new String("File for saving User data\n");
                FileOutputStream fOut = openFileOutput("persons.txt",MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                osw.write(STARTSTRING);
                osw.flush();
                osw.close();
            } catch (IOException e1) {
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
        person = new Person(extras.getString("name"), value);
        Log.d("new person: ", person.print_person());

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
                if (record) {
                    Long now = calendar.getTimeInMillis();
                    addNoteTime(button, now);
                    rec_helper();
                }
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
    patternSet = new LinkedList<Pattern>();
        //* fix this
        //play sequence
      if (patterns.getPatterns().size() > 0){
            for (Pattern p: patterns.getPatterns())
                patternSet.add(p);
          patternIndex(patternSet.pop());

      } else {
            alertDialogBuilder.setTitle("No sequences to play!");
            alertDialogBuilder.setMessage("Something went wrong. Try another level.");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    private void patternIndex(Pattern p) {
        person.index = 0; // reset player index for new pattern
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
        // the below plays weird. It just works better in order
//        // if 'Primer', loop through note sequences in order
//        if (value.compareTo("Primer") == 0) {
//            if (person.index != 0) {
//                int i = person.index;
//                while (i > 0) {
//                    indices.pop();
//                    i--;
//                }
//            }
//            playPattern(indices.pop(), p);
//        } else { // randomize play order
//            Random v = new Random();
//            int i = v.nextInt(indices.size()); // select random value from existing index values
//            int[] ind = indices.get(i); // assign int[] to temp value
//            indices.remove(i); // remove this int[] from LinkedList
//            playPattern(ind, p); // play this value
//        }
        if (person.index != 0) {
            int i = person.index;
            while (i > 0) {
                indices.pop();
                i--;
            }
        }
        playPattern(indices.pop(), p);
    }

    public void playPattern( int[] index, Pattern p) {
        person.index += 1; //increment index
        temp_index = index;
        temp_p = p;
        String playData = String.valueOf(index[0]) + String.valueOf(index[1]);
        rec_notes = new ArrayList<>();
        rec_rhythm = new ArrayList<>();
        person.save_person(this);
        notes = p.getNSequence(index[1]);
        Log.d("notes:", String.valueOf(notes));
        rhythm = p.getRSequence(index[0]);
        Log.d("rhythm:", String.valueOf(rhythm));
        // inner 'notes' loop
        for (int j = 0; j < notes.size(); j++) {
            String n = String.valueOf(notes.get(j));
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
        record = true;
        rec_helper();
    }

    private void progress(int result, final int[] index, final Pattern p) {
        if (result < 70) {
            alertDialogBuilder.setTitle("Pattern not passed. You achieved " + result + "% accuracy.");
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
                            if (indices.size() > 0){
                                playPattern(indices.pop(), p);
                            } else {
                                person.pattern += 1;
                                patternIndex(patternSet.pop());
                            }
                        }
                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            alertDialogBuilder.setTitle("Congratulations, you passed with " + result + "% accuracy!");
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
                            if (indices.size() > 0){
                                playPattern(indices.pop(), p);
                            } else {
                                person.pattern += 1;
                                patternIndex(patternSet.pop());
                            }
                        }
                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void rec_helper(){
        Log.d("Record is true","Now in rec_helper");
        if(notes.size() == rec_notes.size()){
            Log.d("sizes are equal", "now check 'em");
            int result = compare();
            progress(result, temp_index, temp_p);
        }
    }

    private int compare() {
        // TODO:
        // check rec_notes vs. temp_index
        // diff in timestamps in rec_rhythm vs temp_p...?
        Log.i("recorded notes: ", String.valueOf(rec_notes));
        Log.i("recorded times: ", String.valueOf(rec_rhythm));
        Log.d("Comparison checks out", "Move to next pattern");
        record = false;
        return 75;
    }

    private void addNoteTime(Button button, Long now) {
        Log.d("now we are in","addNoteTime");
        int id = button.getId();
        Log.d("hit key: ", String.valueOf(button.getId()));
        Log.d("time: ", String.valueOf(now));
        rec_notes.add(String.valueOf(button.getId()));
        rec_rhythm.add(now);
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
