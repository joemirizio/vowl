package edu.cmu.rwsefe.vowl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import edu.cmu.rwsefe.vowl.R;

public class UserSettings {
	
	public static final String DEFAULT_LANGUAGE = "english";
	
	// Singleton Object
	private static UserSettings mInstance;
	
	
	private Context mContext;
	private SharedPreferences mSharedPreferences;	
	
	private String mProjectPath;
	private String mJSON;
	private Language mLanguage;
	
	public UserSettings() {}
	
	public void Initialize(Context context) {
		mContext = context;
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mProjectPath = mContext.getFilesDir().getPath() + "/" + "projects";
		mJSON = getJSONString();
		String languageValue = mSharedPreferences.getString("script", DEFAULT_LANGUAGE);
		mLanguage = new Language(languageValue);
	}
	
	public static UserSettings getInstance() {
		if (mInstance == null)
			mInstance = new UserSettings();
		return mInstance;
	}
	
	public String getJSONString() {
		
		String json = null;
		try {
			AssetManager assetManager = mContext.getAssets();
			InputStream inputStream = assetManager.open("language.json");
			int size = inputStream.available();
	        byte[] buffer = new byte[size];
	        inputStream.read(buffer);
	        inputStream.close();
	        json = new String(buffer, "UTF-8");	
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return json;
	}
	
	public Language getLanguage() {
		return mLanguage;
	}
	
	public String getProjectPath() {
		return mProjectPath;
	}
	
	public void updateLanguage(String languageValue) {
		
		mLanguage = new Language(languageValue);
	}
	
	public String[] getLanguageNames() {
		return getLanguageList("name");
	}
	
	public String[] getLanguageValues() {
		return getLanguageList("value");
	}
			
	private String[] getLanguageList(String attr) {
		
		String[] list = null;
		try {
	        JSONObject jsonObject = new JSONObject(mJSON);
	        JSONArray jsonArray = jsonObject.getJSONArray("languages");
	        list = new String[jsonArray.length()];
	        for(int i = 0; i < jsonArray.length(); i++) {
	        	JSONObject jsonScript = jsonArray.getJSONObject(i);
	        	list[i] = jsonScript.getString(attr);
	        }
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return list;
	}
}
