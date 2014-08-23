package com.gpif.sshrc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gpif.utils.WOL;

public class MainActivity extends Activity {

	protected String ip;
	protected String mac;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.ip = sharedPrefs.getString("ip_address", "-1");
		this.mac = sharedPrefs.getString("mac_address", "-1");
		
		Button pon = (Button) findViewById(R.id.button_pon);
		
		pon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new RunWol().execute(ip,mac);
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
		    Intent intent = new Intent(this, SettingActivity.class);
		    startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class RunWol extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			WOL wol = new WOL(params[0],params[1]);
			try {
				wol.send();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
}
