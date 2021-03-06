package com.example.glockenspiel;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BeginnerActivity extends Activity {
	// Variables
	private MediaPlayer background_music, key_c_player, key_d_player, key_e_player, key_f_player, key_g_player, key_a_player, key_b_player, key_c2_player;
	private Button a,b,c,d,e,f,g,c2;
	private CharSequence playKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beginner);
		
		//play ambiance background music
        float leftVolume = (float)0.05;
        float rightVolume = (float)0.05;
        background_music = MediaPlayer.create(BeginnerActivity.this, R.raw.beach_ambience);
        background_music.setVolume(leftVolume, rightVolume);
        background_music.setLooping(true);
        background_music.start();
        
        //set up keyboard
        //populateKeys(); //<<----- initialize keys function
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
		key_c_player = MediaPlayer.create(BeginnerActivity.this, R.raw.c1);
		setOnClickListener(c, key_c_player);
		key_d_player = MediaPlayer.create(BeginnerActivity.this, R.raw.d);
		setOnClickListener(d, key_d_player);
		key_e_player = MediaPlayer.create(BeginnerActivity.this, R.raw.e);
		setOnClickListener(e, key_e_player);
		key_f_player = MediaPlayer.create(BeginnerActivity.this, R.raw.f);
		setOnClickListener(f, key_f_player);
		key_g_player = MediaPlayer.create(BeginnerActivity.this, R.raw.g);
		setOnClickListener(g, key_g_player);
		key_a_player = MediaPlayer.create(BeginnerActivity.this, R.raw.a);
		setOnClickListener(a, key_a_player);
		key_b_player = MediaPlayer.create(BeginnerActivity.this, R.raw.b);
		setOnClickListener(b, key_b_player);
		key_c2_player = MediaPlayer.create(BeginnerActivity.this, R.raw.c2);
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
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beginner, menu);
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
