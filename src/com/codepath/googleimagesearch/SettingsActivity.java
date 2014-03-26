package com.codepath.googleimagesearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SettingsActivity extends Activity {

	private Spinner spSize, spColour, spType;
	private Button btnSave, btnReset;
	private EditText etSite;
	private CheckBox cbBoxSafe;
	
	SharedPreferences sharedPref;
	Context context;
	public static final String SHARED_PREFERENCES = "SearchParams";
	public static final String KEY_SIZE = "size";
	public static final String KEY_COLOUR = "colour";
	public static final String KEY_ITEM = "item";
	public static final String KEY_SITE = "site";
	public static final String KEY_SAFE = "safe";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);		
		setUp();
	}

	private void setUp() {
		spSize = (Spinner) findViewById(R.id.spSize);
		spColour = (Spinner) findViewById(R.id.spColour);
		spType = (Spinner) findViewById(R.id.spImageType);
		etSite = (EditText) findViewById(R.id.etSiteName);
		cbBoxSafe = (CheckBox)findViewById(R.id.cbChildSafety);
		btnSave = (Button) findViewById(R.id.btnSaveSettings);		
		btnReset = (Button) findViewById(R.id.btnReset);	
		
		
		String selectedSize = getIntent().getStringExtra(KEY_SIZE);
		String selectedColour = getIntent().getStringExtra(KEY_COLOUR);
		String selectedItem = getIntent().getStringExtra(KEY_ITEM);
		String siteValue = getIntent().getStringExtra(KEY_SITE);
		boolean safe = getIntent().getBooleanExtra(KEY_SAFE, true);
		
		if(!selectedSize.equals("")){
			SelectSpinnerItemByValue(spSize, selectedSize);
		}
		if(!selectedColour.equals("")){
			SelectSpinnerItemByValue(spColour, selectedColour);
		}
		if(!selectedItem.equals("")){
			SelectSpinnerItemByValue(spType, selectedItem);
		}

		etSite.setText(siteValue);
		cbBoxSafe.setChecked(safe);
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				/*				
				sharedPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);				
				Editor editor = sharedPref.edit();
				editor.putInt(KEY_SIZE, spSize.getSelectedItemPosition());
				editor.putInt(KEY_COLOUR, spColour.getSelectedItemPosition());
				editor.putInt(KEY_ITEM, spType.getSelectedItemPosition());
				editor.commit();
				*/
				
				// Setup the result of this intent
				Intent data = new Intent();
				data.putExtra(KEY_SIZE, (String)spSize.getSelectedItem());
				data.putExtra(KEY_COLOUR, (String)spColour.getSelectedItem());
				data.putExtra(KEY_ITEM, (String)spType.getSelectedItem());
				data.putExtra(KEY_SITE, (String)etSite.getText().toString());
				data.putExtra(KEY_SAFE, cbBoxSafe.isChecked());
				
				setResult(RESULT_OK, data);
				finish();
				
			}
		});
			
		btnReset.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				spSize.setSelection(0);
				spColour.setSelection(0);
				spType.setSelection(0);
				etSite.setText("");
				cbBoxSafe.setChecked(true);
			}
		});
		

	
		
	

	}
	public static void SelectSpinnerItemByValue(Spinner spnr, String value)
	{
		SpinnerAdapter adapter = (SpinnerAdapter) spnr.getAdapter();
	    for (int position = 0; position < adapter.getCount(); position++)
	    {
	        if(adapter.getItem(position).equals(value))
	        {
	            spnr.setSelection(position);
	            return;
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
