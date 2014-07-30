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
		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(UNICODE_VALUE, newCharacter.getUnicodeValue()); // get title 
		values.put(CONFIDENCE, newCharacter.getConfidence()); // get author
		
		// 3. insert
		db.insert(CHARACTER_TABLE, // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
	}
	
	public List<CharacterResult> getCharacterResults(int newUnicodeValue) {
		
		
		// 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = 
        		"SELECT * FROM " + CHARACTER_TABLE + 
        		" WHERE " + UNICODE_VALUE + " = " + newUnicodeValue;
 
        // 2. build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        List<CharacterResult> characters = new ArrayList<CharacterResult>();
        
        // 4. build character object
        do {
            CharacterResult newCharacter = new CharacterResult(
            		cursor.getColumnIndex(ID),
            		cursor.getColumnIndex(UNICODE_VALUE), 
            		cursor.getColumnIndex(CONFIDENCE));
            
            characters.add(newCharacter);
        } while(cursor.moveToNext());
 
        // 5. return character
        return characters;
	}
	
	public List<CharacterResult> getAllCharacterResults()
	{
		// 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT * FROM " + CHARACTER_TABLE;
 
        // 2. build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        List<CharacterResult> characters = new ArrayList<CharacterResult>();
        
        // 4. build character object
        do {
            CharacterResult newCharacter = new CharacterResult(
            		cursor.getInt(cursor.getColumnIndex(ID)),
            		cursor.getInt(cursor.getColumnIndex(UNICODE_VALUE)), 
            		cursor.getInt(cursor.getColumnIndex(CONFIDENCE)));
            
            characters.add(newCharacter);
        } while(cursor.moveToNext());
 
        // 5. return character
        return characters;
	}
	
	public int getMaxConfidence(int newUnicodeValue) {
		
		// 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = 
        		"SELECT MAX (" + CONFIDENCE + ") FROM " + CHARACTER_TABLE + 
        		" WHERE " + UNICODE_VALUE + " = " + newUnicodeValue;
 
        // 2. build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        else
        	return 0;
        
        // 4. Get Confidence
        int maxConfidence = cursor.getInt(0);
         
        // 5. return confidence
        return maxConfidence;
	}
	
	public HashMap<Integer, Integer> getMaxScoreForAllCharacters()
	{
		// 1. Get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = 
        		"SELECT " + ID + ", " + UNICODE_VALUE + ", MAX(" + CONFIDENCE + ") " + 
        		" FROM " + CHARACTER_TABLE + " GROUP BY " + UNICODE_VALUE;
 
        // 2. Build query
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // 3. Exit if no results were found
        if (cursor == null || cursor.getCount() == 0) {
        	return null;
        }
        
        cursor.moveToFirst();
        
        // 4. Build character object
        HashMap<Integer, Integer>  characterHash = new HashMap<Integer, Integer>();
        do {
            characterHash.put(cursor.getInt(1), cursor.getInt(2));
        } while(cursor.moveToNext());
 
        // 5. Return character
        return characterHash;
	}
	
	public void clearData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(CHARACTER_TABLE, null, null);
	}

}
