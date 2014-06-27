package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class LevelSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
//		setContentView(R.layout.activity_main);
	}
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_navitem, container,
				false);
		
		// Start level select when nav button is clicked
		View button = (View) rootView.findViewById(R.id.nav_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), LevelSelectActivity.class);
//				Intent intent = new Intent(v.getContext(), SplashFragment.class);
				startActivity(intent);
			}
		});

		return rootView;
	}
	
	
	
	
	
	
}
