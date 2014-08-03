package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import edu.cmu.rwsefe.vowl.model.ScoreKeeper;
import edu.cmu.rwsefe.vowl.model.UserSettings;

public class UserSettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private static final String TAG = UserSettingsFragment.class.getName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_settings);

		ListPreference languageListPreference = (ListPreference)findPreference("language");
		
		if(languageListPreference != null) {
				        
	        languageListPreference.setEntries(UserSettings.getInstance().getLanguageNames());
	        languageListPreference.setEntryValues(UserSettings.getInstance().getLanguageValues());
		}
		
		Preference clearData = findPreference("clear_data");
		final Activity activity = this.getActivity();
		clearData.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ScoreKeeper scoreKeeper = new ScoreKeeper(activity);
				scoreKeeper.clearScores();
				scoreKeeper.close();
				Toast.makeText(activity, "User Data Cleared", Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		if (key.contentEquals("language")) {
			UserSettings.getInstance().updateLanguage(sharedPreferences.getString(key, UserSettings.DEFAULT_LANGUAGE));
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
