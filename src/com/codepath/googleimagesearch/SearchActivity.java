package com.codepath.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	String size, colour, item_type, site_name;
	boolean childMode;
	
	public static final int SETTINGS_REQUEST = 123;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adaptor, View parent, int position,
					long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				
				ImageResult imageResult = imageResults.get(position);				
				i.putExtra("url", imageResult.getFullUrl());
				startActivity(i);
			}
			
		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	            LoadGoogleImages(page); 

	        }

			private void LoadGoogleImages(int page) {
				LoadMoreImages(page);
			}
	        });
	}

	private void setupViews() {
		etQuery = (EditText)findViewById(R.id.etQuery);
		gvResults = (GridView)findViewById(R.id.gvResults);
		btnSearch = (Button)findViewById(R.id.btnSearch);		
		
		size = "";
		colour= "";
		item_type= "";
		site_name= "";
		childMode = true;

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void OnImageSearch(View v){
		
		// I was playing around with SharedPreferences for persistence
		/*
		SharedPreferences sharedpreferences;
		
		 sharedpreferences = getSharedPreferences(SettingsActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);

		    if (sharedpreferences.contains(SettingsActivity.KEY_SIZE))
		    {
		    	String showVal = sharedpreferences.getString(SettingsActivity.KEY_SIZE, "");
		    		Toast.makeText(this, sharedpreferences.getString(SettingsActivity.KEY_SIZE, ""), 
		    				Toast.LENGTH_SHORT).show();
		    }
		    */
		imageResults.clear();
		LoadMoreImages(0);
	}
	
	private void LoadMoreImages(int start){
		
		AsyncHttpClient client = new AsyncHttpClient();

		String searchString = GenerateSearchString(start);
		
		client.get(searchString, 
		new JsonHttpResponseHandler(){
			public void onSuccess(JSONObject response){
				JSONArray imageJsonResults = null;
				try{
					imageJsonResults = response.getJSONObject(
							"responseData").getJSONArray("results");
										
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));

				}
				catch(Exception e){
					
				}
				finally{
					
				}
			}
		}
		
				);
	}
	
	private String GenerateSearchString(int start) {
		String query = etQuery.getText().toString();
		
		String siteString = "";
		if(site_name!=null && !site_name.isEmpty() && !colour.equals("Select Size"))
			siteString = "&as_sitesearch=" + site_name.toLowerCase(); 
		
		String colourString = "";
		if(colour!=null && !colour.isEmpty() && !colour.equals("Select Color"))
			colourString = "&imgcolor=" + colour.toLowerCase();
		
		String sizeString = "";
		if(size!=null && !size.isEmpty() && !colour.equals("Select Type"))
			sizeString = "&imgsz=" + size.toLowerCase();
				
		String typeString = "";
		if(item_type!=null && !item_type.isEmpty()){
			typeString = "&imgtype=";
			if(item_type.equals("Clip Art"))
				typeString = typeString + "clipart";
			else if(item_type.equals("Line Art"))
				typeString = typeString + "lineart";
			else
				typeString = typeString + item_type.toLowerCase();
		}
		
		String safeMode = "";
		if(childMode)
		{
			safeMode = "&safe=active";
		}
		
		return "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + 
				"start=" + start + 
				"&v=1.0" +
				"&q=" + Uri.encode(query) + 
				siteString + colourString + sizeString + typeString + safeMode;
	}

	public void onSettingsPress(MenuItem mi){
		
		//Toast.makeText(this, "Settings Launched", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, SettingsActivity.class);
		i.putExtra(SettingsActivity.KEY_SIZE, size);
		i.putExtra(SettingsActivity.KEY_ITEM, item_type);
		i.putExtra(SettingsActivity.KEY_COLOUR, colour);
		i.putExtra(SettingsActivity.KEY_SITE, site_name);
		i.putExtra(SettingsActivity.KEY_SAFE, childMode);
		startActivityForResult(i, SETTINGS_REQUEST);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == SETTINGS_REQUEST){
			if(resultCode == RESULT_OK){
				size = data.getStringExtra(SettingsActivity.KEY_SIZE);
				colour = data.getStringExtra(SettingsActivity.KEY_COLOUR);
				item_type = data.getStringExtra(SettingsActivity.KEY_ITEM);
				site_name = data.getStringExtra(SettingsActivity.KEY_SITE);
				childMode = data.getBooleanExtra(SettingsActivity.KEY_SAFE, true);
			}	
		}
	}
	
	

}
