package edu.cmu.rwsefe.vowl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import edu.cmu.rwsefe.vowl.model.UserSettings;

public class UserSettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private static final String TAG = UserSettingsFragment.class.getName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_settings);
		
		
		// Creating dynamic ListPreference
		List<String> scriptList = new ArrayList<String>();
		ListPreference scriptListPreference = (ListPreference)findPreference("script");
		
		if(scriptListPreference != null) {
			File projectFolder = new File(UserSettings.getInstance().getProjectPath());
			if(projectFolder.isDirectory()) {
				File[] characterPacks = projectFolder.listFiles();
				for (File characterPack : characterPacks) {
					if(characterPack.isDirectory()) {
						scriptList.add(characterPack.getName());
					}
				}
				
				// Initializing entries and values for list preference
				int numberOfScripts = scriptList.size();
				String entries[] = new String[numberOfScripts];
				String values[] = new String[numberOfScripts];
				
				for (int i = 0; i < scriptList.size(); i++) {
					entries[i] = scriptList.get(i);
					values[i] = scriptList.get(i).toUpperCase();
				}
				
				scriptListPreference.setEntries(entries);
				scriptListPreference.setEntryValues(values);
			}			
		}
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		if (key.contentEquals("script")) {
			UserSettings.getInstance().updateAttributes(sharedPreferences.getString(key, "latin"));
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();
	}
}
