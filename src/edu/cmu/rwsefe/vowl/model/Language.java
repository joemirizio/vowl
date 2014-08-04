package edu.cmu.rwsefe.vowl.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Language extends Script {
	
	private String mNativeName;
	private String mLanguageValue;
	
	public Language(String languageValue) {
		
		super(languageValue);
		mLanguageValue = languageValue;
		mNativeName = getNativeNameFromJSON(mLanguageValue);
	}
	
	public String getNativeName() {
		return mNativeName;
	}
	
	public String getLanguageValue() {
		return mLanguageValue;
	}

	private static String getNativeNameFromJSON(String languageValue) {
		String script = null;
		try {
	        JSONObject jsonObject = new JSONObject(UserSettings.getInstance().getJSONString());
	        JSONArray jsonArray = jsonObject.getJSONArray("languages");
	        for(int i = 0; i < jsonArray.length(); i++) {
	        	JSONObject jsonScript = jsonArray.getJSONObject(i);
	        	if(jsonScript.getString("value").equals(languageValue)) {
	        		script = jsonScript.getString("name");
	        	}
	        }
			
		} catch (JSONException e) {
			e.printStackTrace();

		}
		
		return script;
	}

}
