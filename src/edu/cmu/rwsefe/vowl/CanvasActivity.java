package edu.cmu.rwsefe.vowl;

import java.io.IOException;
import java.util.HashMap;
import java.util.MissingResourceException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;

import com.canvas.AssetInstaller;

import edu.cmu.rwsefe.vowl.CanvasView.ScoreEventListener;
import edu.cmu.rwsefe.vowl.LevelSelector.LevelChangeListener;

public class CanvasActivity extends Activity {

	private final String TAG = CanvasActivity.class.getName();
	public final static String BUNDLE_LEVELS = "levels";
	public final static String BUNDLE_LEVEL_INDEX = "levelIndex";
	
	private CanvasView mCanvasView;
	private RatingBar mRatingBar;
	private LevelSelector mLevelSelector;
	private int mConfidence;
	
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
		
		// Install LipiTK components
		AssetInstaller assetInstaller = new AssetInstaller(getApplicationContext(), "projects");
		try {
			assetInstaller.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set score listener for canvas view
		mCanvasView = (CanvasView) this.findViewById(R.id.canvas);
		mCanvasView.setOutlineCharacter(mLevelSelector.getLevel());
		mCanvasView.setScoreEventListener(new ScoreEventListener() {
			@Override
			public void onScore() {
				processDialogBox();
			}
		});
		
		// Set level navigation OnClickListeners
		Button levelNavPrev = (Button) this.findViewById(R.id.canvasLevelNavPrev);
		Button levelNavNext = (Button) this.findViewById(R.id.canvasLevelNavNext);
		levelNavPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { Log.d(TAG, "PREV LEVEL"); mLevelSelector.prevLevel(); }
		});
		levelNavNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { Log.d(TAG, "NEXT LEVEL"); mLevelSelector.nextLevel(); }
		});
		
		
		mRatingBar = (RatingBar) this.findViewById(R.id.canvasRatingBar);
	}
	
	public void onLevelChange(int index) {
		Log.d(TAG, "ChangedLevel");
		mCanvasView.initializeStroke();
		mCanvasView.setOutlineCharacter(mLevelSelector.getLevel());
	}
	
	public void processDialogBox(){
		ProgressdialogClass dialogBox=new ProgressdialogClass();
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
			HashMap<String,Integer> characters = mCanvasView.getCharacterResults();
			Integer confidenceFromHash = characters.get(mLevelSelector.getLevel());
			if (confidenceFromHash != null) {
				mConfidence = confidenceFromHash;
			}
			else {
				mConfidence = 0;
			}
			
			int rating = mConfidence / 10;
			mRatingBar.setRating(rating);
		}
		@Override
		protected void onPreExecute(){
			dialog = ProgressDialog.show(CanvasActivity.this, "Processing","Please wait...", true);
		}
	}
}