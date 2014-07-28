package edu.cmu.rwsefe.vowl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

public class Script {
	
	protected String[] mCharacterSets;
	protected String mFont;
	protected String mScriptValue;
	protected String mShapeRecognizer;
	
	public Script(String language) {
		mScriptValue = getScriptFromLanguage(language);
		mFont = readFontFromJSON(mScriptValue);
		String characters = getCharacterFromUnicodeMapFile(mScriptValue);
		mCharacterSets = getCharacterSetsFromString(characters);
		mShapeRecognizer = getShapeRecognizer(mScriptValue);
	}
	
	public String[] getCharacterSets() {
		return mCharacterSets;
	}
	
	public String getFont() {
		return mFont;
	}
	
	public String getScriptValue()
	{
		return mScriptValue;
	}
	
	public String getShapeRecognizer() {
		return mShapeRecognizer;
	}
	
	private static String getScriptFromLanguage(String language) {
		
		String script = null;
		try {
	        JSONObject jsonObject = new JSONObject(UserSettings.getInstance().getJSONString());
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
	
	private static String getCharacterFromUnicodeMapFile(String scriptValue) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		// Pattern of shape to unicode character mapping in LipiTK configuration file
		Pattern unicodeMapPattern = Pattern.compile("(\\d+)\\s*=\\s*0X([a-fA-F0-9]{4})");
		
		// Get Path to the List of unicode characters supported by script in LipiTK
		File unicodeMapFile = new File(UserSettings.getInstance().getProjectPath() + "/" + scriptValue + "/config/" + "unicodeMapfile_" + scriptValue + ".ini");		
	
		try {
			
			// Open unicode map file for reading
			BufferedReader unicodeMapFileReader = new BufferedReader(new FileReader(unicodeMapFile));
			String line;
			
			// Iterate through each line in unicode map file
			while((line=unicodeMapFileReader.readLine())!=null) {
				
				Matcher matcher = unicodeMapPattern.matcher(line);
				
				// Check if line matches shape to unicode map pattern
				if (matcher.matches()) {
					
					// Get raw hex string for unicode value
					String unicodeString = matcher.group(2);
					
					// Convert hex string to char
					Character unicodeChar = (char)Integer.parseInt(unicodeString, 16);
					
					// Add character to return string
					stringBuilder.append(unicodeChar);
				}
			}
			unicodeMapFileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return stringBuilder.toString();
	}
	
	private static String[] getCharacterSetsFromString(String characters) {
		
		// Create StringBuilder SparseArray to store the different types of character sets
		SparseArray <StringBuilder> stringBuilderSet = new SparseArray<StringBuilder>();
		
		// Iterate though entire list of characters
		
		for (Character unicodeChar : characters.toCharArray()) {
			
			int unicodeCharType;
			if (Character.isLetterOrDigit(unicodeChar)) {
				unicodeCharType = Character.getType(unicodeChar);
			}
			else {
				unicodeCharType = Character.UNASSIGNED;
			}
			
			// Get character type
			
			// Append character to string which contains characters of the same type
			if (stringBuilderSet.get(unicodeCharType) == null) {
				stringBuilderSet.put(unicodeCharType, (new StringBuilder()).append(unicodeChar));
			}
			else {
				stringBuilderSet.put(unicodeCharType, stringBuilderSet.get(unicodeCharType).append(unicodeChar));
			}
		}

		// Convert String Builders to array of strings
		String[] characterSets = new String[stringBuilderSet.size()];
		for (int i = 0; i < characterSets.length; i++) {
			characterSets[i] = stringBuilderSet.valueAt(i).toString();
		}
		
		return characterSets;
	}
	
	private static String readFontFromJSON(String scriptValue) {
		String font = null;
		try {
	        JSONObject jsonObject = new JSONObject(UserSettings.getInstance().getJSONString());
	        JSONObject scriptHash = jsonObject.getJSONObject("scripts");
	        font = scriptHash.getString(scriptValue);
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		if (font == null) {
			font = "FredokaOne-Regular.ttf";
		}
		
		return font;
	}
	
	private static String getShapeRecognizer(String scriptValue) {
		
		String shapeRecString = null;
		
		// Pattern of shape to unicode character mapping in LipiTK configuration file
		Pattern lipiEnginePattern = Pattern.compile("(SHAPEREC_\\w+)\\s*=\\s*(\\w+)\\(\\w+\\)");
		
		String fileName = UserSettings.getInstance().getProjectPath() + "/lipiengine.cfg";
		
		File lipiEngineFile = new File(UserSettings.getInstance().getProjectPath() + "/lipiengine.cfg");
		
		try {
			
			// Open unicode map file for reading
			BufferedReader lipiEngineFileReader = new BufferedReader(new FileReader(lipiEngineFile));
			String line;
			
			// Iterate through each line in unicode map file
			while((line=lipiEngineFileReader.readLine())!=null) {
				
				Matcher matcher = lipiEnginePattern.matcher(line);
				
				// Check if line matches shape to unicode map pattern
				if (matcher.matches()) {
					
					if(scriptValue.equals(matcher.group(2))) {
						shapeRecString = matcher.group(1);
					}
				}
			}
			
			lipiEngineFileReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (shapeRecString == null) {
			throw new MissingResourceException("Shape Recognizer is missing from lipiEngine config", 
					Script.class.getName(), "Shape Recognizer");
		}
		
		return shapeRecString;
	}

}
