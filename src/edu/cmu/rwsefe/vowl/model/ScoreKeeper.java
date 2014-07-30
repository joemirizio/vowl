package edu.cmu.rwsefe.vowl.model;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class ScoreKeeper {

	private static final String TAG = ScoreKeeper.class.getName();
	private DatabaseHandler mDbHandler;
	private HashMap<Integer, Integer> mCharacterScores;
	
	public ScoreKeeper(Context c) {
		mDbHandler = new DatabaseHandler(c);
		updateScores();
	}
	
	public void updateScores() {
    	mCharacterScores = mDbHandler.getMaxScoreForAllCharacters();
	}
	
	public void saveScore(String character, int rating) {
		CharacterResult newCharacter = new CharacterResult(
				stringCharacterToInt(character), rating);
		mDbHandler.addCharacterResult(newCharacter);
		Log.d(TAG, "ADDING " + newCharacter.getUnicodeValue() + " : " + 
				newCharacter.getConfidence());
		
		List<CharacterResult> results = mDbHandler.getAllCharacterResults();
	}
	
	public int getScoreRating(String character) {
    	
    	int chr = stringCharacterToInt(character);
    	int rating = (mCharacterScores != null && mCharacterScores.containsKey(chr)) ?
    			mCharacterScores.get(chr) : 0;
    	return rating;
	}
	
	private int stringCharacterToInt(String character) {
		return (int)character.charAt(0);
	}
	
	public void clearScores() {
		mDbHandler.clearData();
	}
	
	public void close() {
		mDbHandler.close();
	}
	
}
