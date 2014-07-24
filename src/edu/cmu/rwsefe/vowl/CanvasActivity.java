package edu.cmu.rwsefe.vowl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import com.canvas.AssetInstaller;

import edu.cmu.rwsefe.vowl.CanvasView.ScoreEventListener;
import edu.cmu.rwsefe.vowl.LevelSelector.LevelChangeListener;
import edu.cmu.rwsefe.vowl.model.ScoreKeeper;

public class CanvasActivity extends Activity {

	private final String TAG = CanvasActivity.class.getName();
	public final static String BUNDLE_LEVELS = "levels";
	public final static String BUNDLE_LEVEL_INDEX = "levelIndex";
	
	private CanvasView mCanvasView;
	private RatingBar mRatingBar;
	private LevelSelector mLevelSelector;
	private ScoreKeeper mScoreKeeper;
	private int mConfidence;
	private TextToSpeech mTextToSpeech;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Restore any saved state 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas);
		
		// Get character from bundle
		Bundle bundle = getIntent().getExtras();
		String levels = bundle.getString(BUNDLE_LEVELS);
		int levelIndex = bundle.getInt(BUNDLE_LEVEL_INDEX, -1);
		if (levels == null) {
			throw new MissingResourceException("Resource is missing from bundle", 
					CanvasActivity.class.getName(), BUNDLE_LEVELS);
		}
		if (levelIndex < 0) {
			throw new MissingResourceException("Resource is missing from bundle", 
					CanvasActivity.class.getName(), BUNDLE_LEVEL_INDEX);
		}
		
		// Initialize LevelSelector and set LevelChangeListener
		mLevelSelector = new LevelSelector(levels, levelIndex);
		mLevelSelector.setLevelChangeListener(new LevelChangeListener() {
			@Override
			public void onLevelChange(int index) {
				CanvasActivity.this.onLevelChange(index);
			}
		});
		
		mScoreKeeper = new ScoreKeeper(this);
		
		// Set score listener for canvas view
		mCanvasView = (CanvasView) this.findViewById(R.id.canvas);
		mCanvasView.setScoreEventListener(new ScoreEventListener() {
			@Override
			public void onScore() {
				processDialogBox();
			}
		});
		
		mRatingBar = (RatingBar) this.findViewById(R.id.canvasRatingBar);
		
		mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mTextToSpeech.setLanguage(Locale.US);
		            if (result == TextToSpeech.LANG_MISSING_DATA || 
		            		result == TextToSpeech.LANG_NOT_SUPPORTED) {
		                Log.e("TTS", "This Language is not supported");
		            }
			    } else { 
			    	Log.e("TTS", "Initilization Failed!");
			    }
			}
		});
		
		// Initialize view for level
		onLevelChange(mLevelSelector.getLevelIndex());
	}
	
	@Override
	public void onDestroy() {
		mTextToSpeech.shutdown();
		mScoreKeeper.close();
		super.onDestroy();
	}
	
	public void onClickLevelPrev(View v) {
		mLevelSelector.prevLevel();
	}
	
	public void onClickLevelNext(View v) {
		mLevelSelector.nextLevel();
	}
	
	public void onClickLevelSpeak(View v) {
		mTextToSpeech.speak(mLevelSelector.getLevel(), TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public void onLevelChange(int index) {
		mCanvasView.initializeStroke();
		mCanvasView.setOutlineCharacter(mLevelSelector.getLevel());
		// Set rating from database
		mScoreKeeper.updateScores();
		String character = mLevelSelector.getLevel();
		mRatingBar.setRating(mScoreKeeper.getScoreRating(character));
	}
	
	public void processDialogBox(){
		ProgressdialogClass dialogBox = new ProgressdialogClass();
		dialogBox.execute();
	}
	
	class ProgressdialogClass extends AsyncTask<Void, Void, String> {
		private ProgressDialog dialog;
		
		@Override
		protected String doInBackground(Void... unsued) {
			mCanvasView.addStroke();
			return null;
		}
		@Override
		protected void onPostExecute(String sResponse) {
			dialog.dismiss();
			
			String character = mLevelSelector.getLevel();
			HashMap<String,Integer> characters = mCanvasView.getCharacterResults();
			Integer confidenceFromHash = characters.get(character);
			if (confidenceFromHash != null) {
				mConfidence = confidenceFromHash;
			}
			else {
				mConfidence = 0;
			}
			
			int rating = mConfidence / 10;
			mRatingBar.setRating(rating);

			// Save confidence score to database
			mScoreKeeper.saveScore(character, mConfidence);
		}
		@Override
		protected void onPreExecute(){
			dialog = ProgressDialog.show(CanvasActivity.this, "Processing", "Please wait...", true);
		}
	}
}