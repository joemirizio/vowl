package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class UserSettingsActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
		  FragmentManager fragmentManager = getFragmentManager();
		  FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		  fragmentTransaction.replace(android.R.id.content, userSettingsFragment);
		  fragmentTransaction.commit();
	}

}
