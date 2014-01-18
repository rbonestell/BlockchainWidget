package com.rbonestell.bcwidget;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectFrequencyActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_frequency);
		String[] frequencyOptions = new String[] { "1 minute", "5 minutes", "15 minutes", "30 minutes"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, frequencyOptions);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		String selectedTime = "";
		int selectedMins = 0;
		switch (position)
		{
			case 0:
				selectedTime = "1 minute";
				selectedMins = 1;
				break;
			case 2:
				selectedTime = "15 minutes";
				selectedMins = 15;
				break;
			case 3:
				selectedTime = "30 minutes";
				selectedMins = 30;
				break;
			case 1:
			default:
				selectedTime = "5 minutes";
				selectedMins = 5;
				break;
		}
	    editor.putInt("frequency", selectedMins);
	    editor.commit();
	    Toast.makeText(this, "Update frequency set\nto " + selectedTime, Toast.LENGTH_SHORT).show();
	    this.finish();
	}
	
}
