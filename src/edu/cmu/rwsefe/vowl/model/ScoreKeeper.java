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
	
	public void saveScore(String character, int confidence) {
		CharacterResult newCharacter = new CharacterResult((int) character.charAt(0), confidence);
		mDbHandler.addCharacterResult(newCharacter);
		Log.d(TAG, "ADDING " + newCharacter.getUnicodeValue() + " : " + newCharacter.getConfidence());
		
		List<CharacterResult> results = mDbHandler.getAllCharacterResults();
	}
	
	// TODO Confidence to star algorithm not full determined
	public int getScoreRating(String character) {
    	int rating = 0;
    	if (mCharacterScores != null) {
	    	Integer maxScore = mCharacterScores.get((int)character.charAt(0));
    		rating = (maxScore != null) ? maxScore / 10 : rating;
    	}
    	return rating;
	}
	
	public void close() {
		mDbHandler.close();
	}
	
}
