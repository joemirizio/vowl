package edu.cmu.rwsefe.vowl;

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

		ListPreference languageListPreference = (ListPreference)findPreference("script");
		
		if(languageListPreference != null) {
				        
	        languageListPreference.setEntries(UserSettings.getInstance().getLanguageNames());
	        languageListPreference.setEntryValues(UserSettings.getInstance().getLanguageValues());
		}
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		if (key.contentEquals("script")) {
			UserSettings.getInstance().updateLanguage(sharedPreferences.getString(key, "latin"));
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
