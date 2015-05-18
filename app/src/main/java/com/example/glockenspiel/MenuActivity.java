package com.example.glockenspiel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;


public class MenuActivity extends Activity implements AdapterView.OnItemSelectedListener{
	private Button play_btn;
	Spinner level_spinner;
    EditText player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //* http://techblogon.com/android-sharedpreferences-example-code/ *//

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Reference player name (edit text)
        player = (EditText)findViewById(R.id.player_name);

        // Reference spinner (drop down list)
        level_spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        level_spinner.setAdapter(adapter);
        level_spinner.setOnItemSelectedListener(this);
        
        play_btn = (Button)findViewById(R.id.play_btn);
        play_btn.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Reference to player activity
						Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                        intent.putExtra("name", player.getText().toString());
                        intent.putExtra("selected", level_spinner.getSelectedItem().toString());
						startActivityForResult(intent, 0);
					}
				});
    }
    
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
    	TextView level = (TextView)view;
    }
    
    @Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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


