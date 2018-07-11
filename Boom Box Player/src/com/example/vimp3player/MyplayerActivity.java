package com.example.vimp3player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyplayerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myplayer);
		
		
		Thread thread = new Thread()
		{	
			public void run()
			{
				try
				{
					sleep(100);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					Button b;
					b=(Button)findViewById(R.id.b1);
					b.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(MyplayerActivity.this,MainActivity.class);
							startActivity(i);
						}
					});
				}
			}
			
		};
		thread.start();
	}
}
