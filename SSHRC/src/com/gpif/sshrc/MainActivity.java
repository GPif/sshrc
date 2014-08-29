package com.gpif.sshrc;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

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
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class MainActivity extends Activity {

	protected String ip;
	protected String mac;
	
	protected String user;
	protected String password;
	protected String host;
	
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

		this.user = sharedPrefs.getString("ssh_user", "-1");
		this.password = sharedPrefs.getString("ssh_password", "-1");		
		this.host = sharedPrefs.getString("ssh_host", "-1");		
		
		Button poff = (Button) findViewById(R.id.button_pof);
		
		poff.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new RunSSHCmd().execute(user, password, host);
				
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
	
	private class RunSSHCmd extends AsyncTask<String, Void, Void> {
		@Override	
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				executeRemoteCommand(params[0], params[1], params[2], 22);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public String executeRemoteCommand(
	            String username,
	            String password,
	            String hostname,
	            int port) throws Exception {    

				JSch jsch = new JSch();
				Session session = jsch.getSession(username, hostname, 22);
				session.setPassword(password);
				
				// Avoid asking for key confirmation
				Properties prop = new Properties();
				prop.put("StrictHostKeyChecking", "no");
				System.out.println("Send ssh command");
				session.setConfig(prop);
				
				session.connect();
				
				// SSH Channel
				ChannelExec channelssh = (ChannelExec)
				                session.openChannel("exec");      
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				channelssh.setOutputStream(baos);
				
				// Execute command
				channelssh.setCommand("echo "+password+" | sudo -S poweroff");
				channelssh.connect();        
				channelssh.disconnect();
				
				return baos.toString();
			}

		
	 }
	
}
