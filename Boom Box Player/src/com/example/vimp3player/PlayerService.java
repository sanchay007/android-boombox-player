package com.example.vimp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class PlayerService extends Service implements OnCompletionListener {
	static MediaPlayer mp;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent.getStringExtra("msg").equals("lv")){
		if(mp!=null){
			mp.stop();
			//mp.release();
		}
		mp=MediaPlayer.create(this, Uri.parse(MainActivity.mySongs.get(MainActivity.position).toString()));
		MainActivity.sb1.setMax(mp.getDuration());
		mp.setOnCompletionListener(this);
		mp.start();
		MainActivity.play.setText("ll");
		}
		else if(intent.getStringExtra("msg").equals("play")){
			if(mp==null){
				mp=MediaPlayer.create(this, Uri.parse(MainActivity.mySongs.get(0).toString()));
				MainActivity.sb1.setMax(mp.getDuration());
				mp.setOnCompletionListener(this);
				mp.start();
				MainActivity.play.setText("ll");
			}
			else{
				if(mp.isPlaying()){
					mp.pause();
					MainActivity.play.setText(">");
				}
				else{
					mp.start();
					MainActivity.play.setText("ll");
				}
					
			}
		}
		else if(intent.getStringExtra("msg").equals("next")){
			if(mp!=null){
				mp.stop();
				//mp.release();
				MainActivity.position=(MainActivity.position+1)%(MainActivity.mySongs.size());
				mp=MediaPlayer.create(this, Uri.parse(MainActivity.mySongs.get(MainActivity.position).toString()));
				MainActivity.sb1.setMax(mp.getDuration());
				mp.setOnCompletionListener(this);
				mp.start();
			}
		}
		else if(intent.getStringExtra("msg").equals("prev")){
			if(mp!=null){
				mp.stop();
				//mp.release();
				if(MainActivity.position==0)
				  MainActivity.position=(MainActivity.mySongs.size()-1);
				else
					MainActivity.position=MainActivity.position-1;
				mp=MediaPlayer.create(this, Uri.parse(MainActivity.mySongs.get(MainActivity.position).toString()));
				MainActivity.sb1.setMax(mp.getDuration());
				mp.setOnCompletionListener(this);
				mp.start();
			}
		}
		else if(intent.getStringExtra("msg").equals("sb1")){
			
			if(intent.getBooleanExtra("fromUser", true))
				if(mp!=null)
					mp.seekTo(intent.getIntExtra("progress",0));
		}
		else if(intent.getStringExtra("msg").equals("stop")){
			mp.stop();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		mp.stop();
		//mp.release();
		MainActivity.position=(MainActivity.position+1)%(MainActivity.mySongs.size());
		mp=MediaPlayer.create(this, Uri.parse(MainActivity.mySongs.get(MainActivity.position).toString()));
		//MainActivity.sb1.setMax(mp.getDuration());
		mp.setOnCompletionListener(this);
		mp.start();
	}

}
