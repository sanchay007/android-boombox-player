package com.example.vimp3player;
//hello
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

public class MainActivity extends Activity {
	ListView lv;
	static Button play,next,prev;
	static SeekBar sb1,sb2;
	static ArrayList<File> mySongs;
	static int position;
	static Thread t1,t2;
	AudioManager am;
	static NotificationManager nm;
	 static Notification.Builder nb;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv=(ListView)findViewById(R.id.listView1);
		play=(Button)findViewById(R.id.button2);
		next=(Button)findViewById(R.id.button3);
		prev=(Button)findViewById(R.id.button1);
		sb1=(SeekBar)findViewById(R.id.seekBar1);
		sb2=(SeekBar)findViewById(R.id.seekBar2);
		am=(AudioManager)getSystemService(AUDIO_SERVICE);
		nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		mySongs=new ArrayList<File>();
		File root=Environment.getExternalStorageDirectory();
		findSongs(root);
		String[] items=new String[mySongs.size()];
		for(int i=0;i<mySongs.size();i++)
			items[i]=mySongs.get(i).getName();
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
		lv.setAdapter(adapter);
		
		t1=new Thread(){
			public void run(){
				super.run();
				while(true){
					if(PlayerService.mp==null)
						sb1.setProgress(0);
					else{
						sb1.setMax(PlayerService.mp.getDuration());
						sb1.setProgress(PlayerService.mp.getCurrentPosition());		
					}    
				}
			}
		};
		t1.start();
		
		int max=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		MainActivity.sb2.setMax(max);
		t2=new Thread(){
			public void run() {
				super.run();
				while(true){
					int current=am.getStreamVolume(AudioManager.STREAM_MUSIC);
					MainActivity.sb2.setProgress(current);
					}
			};
		};
		t2.start();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				position=arg2;
				Intent i=new Intent(MainActivity.this,PlayerService.class);
				i.putExtra("msg","lv");
				startService(i);
				
			}
		});
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,PlayerService.class);
				i.putExtra("msg","play");
				startService(i);
			}
		});
        next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,PlayerService.class);
				i.putExtra("msg","next");
				startService(i);
			}
		});
        prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,PlayerService.class);
				i.putExtra("msg","prev");
				startService(i);
			}
		});
        sb1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() { 
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,PlayerService.class);
				i.putExtra("msg","sb1");
				i.putExtra("progress",progress);
				i.putExtra("fromUser",fromUser);
				startService(i);
			}
		});
        sb2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			}
		});
        if(PlayerService.mp!=null&&PlayerService.mp.isPlaying())
        	play.setText("ll");
	}
	public void findSongs(File file){
		File[] files=file.listFiles();
		for(File f:files){
			if(f.isDirectory()&& !f.isHidden())
				findSongs(f);
			else{
				if(f.getName().endsWith(".mp3"))
					mySongs.add(f);
			}
		}
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(PlayerService.mp!=null&&PlayerService.mp.isPlaying()){
             nb=new Notification.Builder(this);
             nb.setSmallIcon(R.drawable.ic_launcher);
             nb.setAutoCancel(true);
             nb.setContentTitle(mySongs.get(position).getName().toString());
     		
     		Intent i=new Intent(this,MainActivity.class);
     		PendingIntent pi=PendingIntent.getActivity(this, (int)System.currentTimeMillis(), i, 0);
     		
     		Intent i1=new Intent(this,PlayerService.class);
     		
     		PendingIntent pi1=PendingIntent.getService(this, 1, i1, 0);
     		i1.putExtra("msg", "prev");
     		Intent i2=new Intent(this,PlayerService.class);
     		i2.putExtra("msg", "next");
     		PendingIntent pi2=PendingIntent.getService(this, 1, i2, 0);
     		Intent i3=new Intent(this,PlayerService.class);
     		i3.putExtra("msg", "stop");
     		PendingIntent pi3=PendingIntent.getService(this, 1, i3, 0);
     		
     		nb.addAction(android.R.drawable.alert_light_frame, "l<", pi1);
     		nb.addAction(android.R.drawable.alert_light_frame, "stop", pi3);
     		nb.addAction(android.R.drawable.alert_light_frame, ">l", pi2);
     		
     		nb.setContentIntent(pi);
     		Notification n=nb.build();
     		nm.notify((int)System.currentTimeMillis(), n);
     	
		}
		super.onPause();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
