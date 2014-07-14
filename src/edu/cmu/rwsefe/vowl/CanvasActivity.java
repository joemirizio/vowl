package edu.cmu.rwsefe.vowl;

import java.io.IOException;
import java.util.HashMap;
import java.util.MissingResourceException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RatingBar;

import com.canvas.AssetInstaller;

import edu.cmu.rwsefe.vowl.CanvasView.ScoreEventListener;

public class CanvasActivity extends Activity {

	public final static String BUNDLE_CHARACTER = "character";
	
	private CanvasView mCanvasView;
	private RatingBar mRatingBar;
	private String mCharacter;
	private int mConfidence;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Restore any saved state 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas);
		
		// Get character from bundle
		Bundle bundle = getIntent().getExtras();
		mCharacter = bundle.getString(BUNDLE_CHARACTER);
		if(mCharacter == null) {
			throw new MissingResourceException("Resource 'character' is missing from bundle", CanvasActivity.class.getName(), BUNDLE_CHARACTER);
		}
		
		// Install LipiTK components
		AssetInstaller assetInstaller = new AssetInstaller(getApplicationContext(), "projects");
		try {
			assetInstaller.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set score listener for canvas view
		mCanvasView = (CanvasView) this.findViewById(R.id.canvas);
		mCanvasView.setOutlineCharacter(mCharacter);
		mCanvasView.setScoreEventListener(new ScoreEventListener() {
			public void onScore() {
				processDialogBox();
			}
		});
		
		mRatingBar = (RatingBar) this.findViewById(R.id.canvasRatingBar);
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
			Integer confidenceFromHash = characters.get(mCharacter);
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