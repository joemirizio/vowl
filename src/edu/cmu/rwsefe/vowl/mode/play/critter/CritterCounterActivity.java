package edu.cmu.rwsefe.vowl.mode.play.critter;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;
import edu.cmu.rwsefe.vowl.CanvasView;
import edu.cmu.rwsefe.vowl.CanvasView.ScoreEventListener;
import edu.cmu.rwsefe.vowl.R;

public class CritterCounterActivity extends Activity {
	private static final String TAG = CritterCounterActivity.class.getName();
	public static final int MAX_CRITTER_COUNT = 10;
	public static final int MAX_GAMES = 5;

	private final Random mRandomGen = new Random();
	
	private RatingBar mRatingBar;
	private CritterCanvas mCritterCanvas;
	private CanvasView mWritingCanvas;
	
	private int mScore = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_critter_counter);
		
		mRatingBar = (RatingBar) findViewById(R.id.canvasRatingBar);
		
		mCritterCanvas = (CritterCanvas) findViewById(R.id.playCritterCanvas);
		
		mWritingCanvas = (CanvasView) findViewById(R.id.playCritterWriting);
		mWritingCanvas.setScoreEventListener(new ScoreEventListener() {
			@Override
			public void onScore() {
				checkAnswer();
			}
		});
	}
	
	private void checkAnswer() {
		// TODO Remove this hack to process results
		mWritingCanvas.addStroke();
		
		if (mWritingCanvas.getCharacterResults().containsKey(
				"" + mCritterCanvas.getCritterCount())) {
			mScore++;
		}
		mRatingBar.setRating(mRatingBar.getRating() + 1);
		if (mRatingBar.getRating() == MAX_GAMES) {
			Toast.makeText(this, 
					"Congratulations, you got " + mScore + " correct!", 
					Toast.LENGTH_LONG).show();;
			finish();
		} else {
			generateCritters();
			mWritingCanvas.initializeStroke();
		}
	}
	
	private void generateCritters() {
		int critterCount = mRandomGen.nextInt(MAX_CRITTER_COUNT);
		mCritterCanvas.generateRandomCritters(critterCount);
	}
}
