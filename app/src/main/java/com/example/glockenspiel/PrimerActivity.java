package com.example.glockenspiel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class PrimerActivity extends Activity {
	// Variables
	private MediaPlayer background_music, key_c_player, key_d_player, key_e_player, key_f_player, key_g_player, key_a_player, key_b_player, key_c2_player;
	private Button a,b,c,d,e,f,g,c2,start_btn;
	private CharSequence playKey;
	private Sequence patterns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_primer);

		//play ambiance background music
		float leftVolume = (float) 0.05;
		float rightVolume = (float) 0.05;
		background_music = MediaPlayer.create(PrimerActivity.this, R.raw.beach_ambience);
		background_music.setVolume(leftVolume, rightVolume);
		background_music.setLooping(true);
		background_music.start();

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
		key_c_player = MediaPlayer.create(PrimerActivity.this, R.raw.c1);
		setOnClickListener(c, key_c_player);
		key_d_player = MediaPlayer.create(PrimerActivity.this, R.raw.d);
		setOnClickListener(d, key_d_player);
		key_e_player = MediaPlayer.create(PrimerActivity.this, R.raw.e);
		setOnClickListener(e, key_e_player);
		key_f_player = MediaPlayer.create(PrimerActivity.this, R.raw.f);
		setOnClickListener(f, key_f_player);
		key_g_player = MediaPlayer.create(PrimerActivity.this, R.raw.g);
		setOnClickListener(g, key_g_player);
		key_a_player = MediaPlayer.create(PrimerActivity.this, R.raw.a);
		setOnClickListener(a, key_a_player);
		key_b_player = MediaPlayer.create(PrimerActivity.this, R.raw.b);
		setOnClickListener(b, key_b_player);
		key_c2_player = MediaPlayer.create(PrimerActivity.this, R.raw.c2);
		setOnClickListener(c2, key_c2_player);
	}


	/****************************************
	 * Helper method to set onClickListener *
	 ****************************************/
	private void setOnClickListener(final Button button, final MediaPlayer key_player) {
		final boolean showToast = false; //toggle toast message
		playKey = null;
		button.setSoundEffectsEnabled(false);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						key_player.start();
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
				}).start();
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
			key_a_player.start();
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
		getMenuInflater().inflate(R.menu.primer_, menu);
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
}
