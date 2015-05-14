package com.example.glockenspiel;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class PlayerActivity extends Activity implements OnTouchListener{
	// Variables
	private MediaPlayer background_music;
	private Button a,b,c,d,e,f,g,c2, start_btn;
	private CharSequence playKey;
	public SoundPool mySoundPool;
	private boolean loaded = false;
	private int keya_sound, keyb_sound, keyc_sound, keyd_sound, keye_sound;
	private int keyf_sound, keyg_sound, keyc2_sound;
	private AudioManager audioManager;
	private float actualVolume, maxVolume, volume;
	private Sequence patterns;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		//play ambiance background music
        float leftVolume = (float)0.1;
        float rightVolume = (float)0.1;
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
		patterns = new Sequence(getApplicationContext(), "primer");
		// start = Button(); We still need to set this
		start_btn = (Button)findViewById(R.id.start_btn);
		setOnClickListener(start_btn);
	}

	
	/************************************
	 * Helper method to layout keyboard *
	 ************************************/
	private void populateKeys() {
		//initialize buttons
		c = (Button)findViewById(R.id.key_c);
		d = (Button)findViewById(R.id.key_d);
		e = (Button)findViewById(R.id.key_e);
		f = (Button)findViewById(R.id.key_f);
		g = (Button)findViewById(R.id.key_g);
		a = (Button)findViewById(R.id.key_a);
		b = (Button)findViewById(R.id.key_b);
		c2 = (Button)findViewById(R.id.key_c2);
		
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

	
	/****************************************
	 * Helper method to set onClickListener *
	 ****************************************/
	private void setOnClickListener(final Button button, final int key_player) {
		final boolean showToast = false; //toggle toast message
		playKey = null;
		button.setSoundEffectsEnabled(false);
		button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				if(loaded){
					mySoundPool.play(key_player, volume, volume, 1, 0, 1f);
					Layout label = button.getLayout();
					playKey = label.getText(); //<<----- key(s) get to played
							
					//display toast message when key pressed
					if(showToast)
					{
						Context context = getApplicationContext();
						Toast toast = Toast.makeText(context, playKey, Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
	}

	/*************************
	 * start sequence button *
	 *************************/
	private void setOnClickListener(final Button button){
		//play sequence
		if (patterns.getPatterns().size() > 0){
			for (Pattern p : patterns.getPatterns()) {
				play_pattern(p);
			}
		}
	}

	public void play_pattern(Pattern pattern){
		play(pattern);
		Pattern results = record();
		Boolean pass = compare_play_and_record(pattern, results);
		if (!pass){
			play_pattern(pattern);
		}
	}

	private void play(Pattern p) {
		String notes = p.getNotes();
		String rhythm = p.getRhythms();
		for (int i = 0; i < notes.length(); i++){
			String str = notes.substring(i);
			mySoundPool.play(keya_sound, volume, volume, 1, 0, 1f);
			try {
				Thread.sleep(Integer.parseInt(rhythm.substring(i)) * 500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}


	private Pattern record() {
		Pattern p = new Pattern(null, null);
		return p;
	}

	private boolean compare_play_and_record(Pattern p, Pattern r){
		return true;
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
