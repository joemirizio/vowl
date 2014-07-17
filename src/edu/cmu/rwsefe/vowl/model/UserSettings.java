package edu.cmu.rwsefe.vowl.model;

import edu.cmu.rwsefe.vowl.R;
import edu.cmu.rwsefe.vowl.R.array;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class UserSettings {
	
	private static final String DEFAULT_SCRIPT = "latin";
	
	// Singleton Object
	private static UserSettings mInstance;
	
	
	private Context mContext;
	private SharedPreferences mSharedPreferences;	
		
	private String mScript;
	private String mLetters;
	private String mUppercaseLetters;
	private String mNumerals;
	
	
	public UserSettings() {}
	
	public void Initialize(Context context) {
		mContext = context;
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mScript = mSharedPreferences.getString("script", DEFAULT_SCRIPT);
		updateAttributes(mScript);
	}
	
	public static UserSettings getInstance() {
		if (mInstance == null)
			mInstance = new UserSettings();
		return mInstance;
	}
	
	
	public String getScriptName() {
		return mScript;
	}
	
	public String getLetters() {
		return mLetters;
	}
	
	public String getUppercaseLetters() {
		return mUppercaseLetters;
	}
	
	public String getNumerals() {
		return mNumerals;
	}
	
	public void updateAttributes(String script) {
		
		Resources resources = mContext.getResources();
		String[] characterSet;
		
		
		//TODO: use Resources.GetIdentifier to make this dynamic
		switch (script) {
			case "latin":
				characterSet = resources.getStringArray(R.array.latin);
				mScript = script;
				break;
			case "devnagari":
				characterSet = resources.getStringArray(R.array.devnagari);
				mScript = script;
				break;
			default:
				characterSet = resources.getStringArray(R.array.latin);
				mScript = "latin";
				break;
		}
		
		mLetters = characterSet[0];
		mUppercaseLetters = characterSet[1];
		mNumerals = characterSet[2];
	}
}
