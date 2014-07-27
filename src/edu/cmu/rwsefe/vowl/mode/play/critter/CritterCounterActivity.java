package edu.cmu.rwsefe.vowl.mode.play.critter;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import edu.cmu.rwsefe.vowl.R;

public class CritterCounterActivity extends Activity {
	public static final int MAX_CRITTER_COUNT = 10;

	private CritterCanvas mCanvas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_critter_counter);
		
		mCanvas = (CritterCanvas) findViewById(R.id.play_critter_canvas);
		generateCritters(mCanvas);
	}
	
	private void generateCritters(CritterCanvas canvas) {
		Random r = new Random();
		int critterCount = r.nextInt(MAX_CRITTER_COUNT);
		//canvas.generateRandomCritters(critterCount);
	}
}
