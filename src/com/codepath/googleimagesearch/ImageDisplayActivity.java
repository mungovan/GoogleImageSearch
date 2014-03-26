package com.codepath.googleimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		String url = getIntent().getStringExtra("url");
		SmartImageView ivImage = (SmartImageView)findViewById(R.id.ivResult);
		ivImage.setImageUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}

	public void onSharePhoto(MenuItem mi){
		
		//Share the image with Facebook
		
		Intent i = new Intent(this, ShareOnFacebook.class);
		
	}
	
}
