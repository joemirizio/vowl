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
	
	private static final String DEFAULT_SCRIPT = "english";
	
	// Singleton Object
	private static UserSettings mInstance;
	
	
	private Context mContext;
	private SharedPreferences mSharedPreferences;	
	
	private String mLanguage;
	private String mScript;
	private String mLetters;
	private String mUppercaseLetters;
	private String mNumerals;
	private String mProjectPath;
	private String mFont;
	private String mJSON;
	
	public UserSettings() {}
	
	public void Initialize(Context context) {
		mContext = context;
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mLanguage = mSharedPreferences.getString("script", DEFAULT_SCRIPT);
		mProjectPath = mContext.getExternalFilesDir(null).getPath() + "/" + "projects";
		mJSON = getJSONString();
		updateAttributes(mLanguage);
	}
	
	public static UserSettings getInstance() {
		if (mInstance == null)
			mInstance = new UserSettings();
		return mInstance;
	}
	
	private String getJSONString() {
		
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
	
	public String getProjectPath() {
		return mProjectPath;
	}
	
	public String getFont() {
		return mFont;
	}
	
	public void updateAttributes(String language) {
		
		Resources resources = mContext.getResources();
		String[] characterSet;
		
		mScript = getLanguageScript(language);
		mFont = getFontFromScript(mScript);
				
		//TODO: use Resources.GetIdentifier to make this dynamic
		switch (mScript) {
			case "alphanumeric":
				characterSet = resources.getStringArray(R.array.latin);
				break;
			case "devnagari":
				characterSet = resources.getStringArray(R.array.devnagari);
				break;
			default:
				characterSet = resources.getStringArray(R.array.latin);
				break;
		}
		
		mLetters = characterSet[0];
		mUppercaseLetters = characterSet[1];
		mNumerals = characterSet[2];
	}
	
	public String getCharactersFromUnicodeMapFile(File file) {
		
		StringBuilder builder = new StringBuilder();
		
		Pattern unicodeMapPattern = Pattern.compile("(\\d+)=\\s(0x[0-9A-F]{4})");
	
		if (file != null) {
			try {
				String line;
				BufferedReader unicodeMapFileReader = new BufferedReader(new FileReader(file));
				while((line=unicodeMapFileReader.readLine())!=null) {
					Matcher matcher = unicodeMapPattern.matcher(line);
					if (matcher.matches()) {
						builder.append((char)Integer.parseInt(matcher.group(2)));
					}
				}
				unicodeMapFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return builder.toString();
	}
	
	public String[] getLanguageNames() {
		return getLanguageAttributeList("name");
	}
	
	public String[] getLanguageValues() {
		return getLanguageAttributeList("value");
	}
	
	private String getFontFromScript(String script) {
		String font = null;
		try {
	        JSONObject jsonObject = new JSONObject(mJSON);
	        JSONObject scriptHash = jsonObject.getJSONObject("scripts");
	        font = scriptHash.getString(script);
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return font;
	}
	
	public String getLanguageScript(String language) {
		
		String script = null;
		try {
	        JSONObject jsonObject = new JSONObject(mJSON);
	        JSONArray jsonArray = jsonObject.getJSONArray("languages");
	        for(int i = 0; i < jsonArray.length(); i++) {
	        	JSONObject jsonScript = jsonArray.getJSONObject(i);
	        	if(jsonScript.getString("value").equals(language)) {
	        		script = jsonScript.getString("script");
	        	}
	        }
			
		} catch (JSONException e) {
			e.printStackTrace();

		}
		
		return script;
	}
	
	private String[] getLanguageAttributeList(String attr) {
		
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
