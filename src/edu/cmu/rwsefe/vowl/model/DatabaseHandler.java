package edu.cmu.rwsefe.vowl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "VowlDB";
	
	private static final String CHARACTER_TABLE = "characters";
	
	//Column names
	private static final String ID = "id";
	private static final String UNICODE_VALUE = "unicode_value";
	private static final String CONFIDENCE = "confidence";	
	
	private static final String CREATE_CHARACTER_TABLE = 
			"CREATE TABLE " + CHARACTER_TABLE + " ( "
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ UNICODE_VALUE + " INTEGER, "
			+ CONFIDENCE + " INTEGER )";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CHARACTER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE);
        this.onCreate(db);
	}
	
	public void addCharacterResult(CharacterResult newCharacter) {
		
		// get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(UNICODE_VALUE, newCharacter.getUnicodeValue());
		values.put(CONFIDENCE, newCharacter.getConfidence());
		
		// insert
		db.insert(CHARACTER_TABLE,
		        null,
		        values); 
	}
	
	public List<CharacterResult> getCharacterResults(int newUnicodeValue) {
		
		
		// get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = 
        		"SELECT * FROM " + CHARACTER_TABLE + 
        		" WHERE " + UNICODE_VALUE + " = " + newUnicodeValue;
 
        // build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        List<CharacterResult> characters = new ArrayList<CharacterResult>();
        
        // build character object
        do {
            CharacterResult newCharacter = new CharacterResult(
            		cursor.getColumnIndex(ID),
            		cursor.getColumnIndex(UNICODE_VALUE), 
            		cursor.getColumnIndex(CONFIDENCE));
            
            characters.add(newCharacter);
        } while(cursor.moveToNext());
        
        // Close cursor
        cursor.close();
 
        // return characters
        return characters;
	}
	
	public List<CharacterResult> getAllCharacterResults()
	{
		// get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT * FROM " + CHARACTER_TABLE;
 
        // build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        List<CharacterResult> characters = new ArrayList<CharacterResult>();
        
        // build character object
        do {
            CharacterResult newCharacter = new CharacterResult(
            		cursor.getInt(cursor.getColumnIndex(ID)),
            		cursor.getInt(cursor.getColumnIndex(UNICODE_VALUE)), 
            		cursor.getInt(cursor.getColumnIndex(CONFIDENCE)));
            
            characters.add(newCharacter);
        } while(cursor.moveToNext());
        
        // Close cursor
        cursor.close();
 
        // return characters
        return characters;
	}
	
	public int getMaxConfidence(int newUnicodeValue) {
		
		// get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = 
        		"SELECT MAX (" + CONFIDENCE + ") FROM " + CHARACTER_TABLE + 
        		" WHERE " + UNICODE_VALUE + " = " + newUnicodeValue;
 
        // build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        else
        	return 0;
        
        // Get Confidence
        int maxConfidence = cursor.getInt(0);
        
        // Close cursor
        cursor.close();
         
        // return confidence
        return maxConfidence;
	}
	
	public HashMap<Integer, Integer> getMaxScoreForAllCharacters()
	{
		// Get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = 
        		"SELECT " + ID + ", " + UNICODE_VALUE + ", MAX(" + CONFIDENCE + ") " + 
        		" FROM " + CHARACTER_TABLE + " GROUP BY " + UNICODE_VALUE;
 
        // Build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // Exit if no results were found
        if (cursor == null || cursor.getCount() == 0) {
        	return null;
        }
        
        cursor.moveToFirst();
        
        // Build character object
        HashMap<Integer, Integer>  characterHash = new HashMap<Integer, Integer>();
        do {
            characterHash.put(cursor.getInt(1), cursor.getInt(2));
        } while(cursor.moveToNext());
 
        // Close cursor
        cursor.close();
        
        // Return character hash
        return characterHash;
	}
	
	public void clearData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(CHARACTER_TABLE, null, null);
	}

}
