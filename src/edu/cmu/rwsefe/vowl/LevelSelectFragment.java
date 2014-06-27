package edu.cmu.rwsefe.vowl;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.canvas.*;

public class LevelSelectFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_level_select,
				container, false);

		// Start level select when nav button is clicked
		View button = (View) rootView.findViewById(R.id.navButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName("com.canvas", "com.canvas.Canvas1");  //Put canvas here
				startActivity(intent);
			}
		});

		return rootView;

	}
}