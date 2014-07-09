package edu.cmu.rwsefe.vowl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.canvas.AssetInstaller;

import edu.cmu.rwsefe.vowl.model.CharacterResult;
import edu.cmu.rwsefe.vowl.model.DatabaseHandler;

public class CanvasActivity extends Activity {

	private int width;
	private int height;
	private CanvasView canvasView;
	private String letter;
	private int confidence;
	
	//Layouts
	private LinearLayout main;
	private LinearLayout topLayout;
	private LinearLayout horizontalTopLayout;
	private LinearLayout drawable;
	private LinearLayout bottomLayout;

	//Constants
	private static final int MAX_STARS = 5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Restore any saved state 
		super.onCreate(savedInstanceState);
		
		// Possible to modify based on Joe's implementation
		Bundle bundle = getIntent().getExtras();
		String intentLetter = bundle.getString("letter");
		//String intentLetter = "a";
		if(intentLetter != null) {
			letter = intentLetter;
		}
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		width = point.x;
		height = point.y;
		
		AssetInstaller assetInstaller = new AssetInstaller(getApplicationContext(), "projects");
		try {
			assetInstaller.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		canvasView = new CanvasView(this, this);
		main = new LinearLayout(this);
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT));
		
		
		drawable = new LinearLayout(this);
		drawable.setOrientation(LinearLayout.VERTICAL);
		drawable.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 
				height * 3 / 4));
		//drawable.setBackgroundColor(0xFFFFCC99);
		
		Resources resource = getResources();
		Drawable notebook = resource.getDrawable(R.drawable.notebook_paper_hi);
		drawable.setBackgroundDrawable(notebook);

		drawable.addView(canvasView);

		topLayout = new LinearLayout(this);
		topLayout.setOrientation(LinearLayout.VERTICAL);
		topLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				height/8));
		topLayout.setGravity(Gravity.TOP);
				
		bottomLayout = new LinearLayout(this);
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				(height/8)));
		bottomLayout.setGravity(Gravity.BOTTOM);

		main.addView(topLayout);
		main.addView(drawable);
		main.addView(bottomLayout);
		setContentView(main);

	}
	
	class ProgressdialogClass extends AsyncTask<Void, Void, String> {
		private ProgressDialog dialog;
		
		@Override
		protected String doInBackground(Void... unsued) {
			canvasView.addStroke();
			return null;
		}
		@Override
		protected void onPostExecute(String sResponse) {
			dialog.dismiss();
			processResults();
			
		}
		@Override
		protected void onPreExecute(){
			dialog = ProgressDialog.show(CanvasActivity.this, "Processing","Please wait...", true);
		}
	}
	
	public void processDialogBox(){
		ProgressdialogClass dialogBox=new ProgressdialogClass();
		dialogBox.execute();
	}
	
	public void processResults() {
		HashMap<String,Integer> characters = canvasView.getCharacterResults();
		Integer confidenceFromHash = characters.get(letter);
		if (confidenceFromHash != null) {
			confidence = confidenceFromHash;
		}
		else {
			confidence = 0;
		}
		
		//startActivity(showResults);
		
		RatingBar rb = new RatingBar(CanvasActivity.this);
		rb.setIsIndicator(true);
		rb.setNumStars(MAX_STARS);
		
		int rating = confidence / 10;
		
		rb.setRating(rating);
		topLayout.addView(rb);
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		CharacterResult newCharacter = new CharacterResult((int) letter.charAt(0), confidence);
		db.addCharacterResult(newCharacter);
		Log.d("CanvasActivityDB", "ADDING " + newCharacter.getUnicodeValue() + " : " + newCharacter.getConfidence());
		
		List<CharacterResult> results = db.getAllCharacterResults();
		
		for(CharacterResult result: results) {
			Log.d("CanvasActivityDB", result.getUnicodeValue() + " : " + result.getConfidence());
		}
		
		db.close();
	}
	
	public String getLetter() {
		return letter;
	}
	
	public int getConfidence() {
		return confidence;
	}
}