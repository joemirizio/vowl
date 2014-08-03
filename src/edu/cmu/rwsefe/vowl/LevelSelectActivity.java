package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import edu.cmu.rwsefe.vowl.model.UserSettings;
import edu.cmu.rwsefe.vowl.ui.FlatButton;

public class LevelSelectActivity extends Activity {
	private final String TAG = LevelSelectActivity.class.getName();
	
	private LevelSelectFragment mlevelSelect;
	private String[] mCharacterSets;
	LinearLayout mCharacterSetsLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		
		mCharacterSetsLayout = (LinearLayout)findViewById(R.id.character_sets);
		mCharacterSets = UserSettings.getInstance().getLanguage().getCharacterSets();
		
		// Initialize character set buttons
		for(int chrSetIndex = 0; chrSetIndex < mCharacterSets.length; chrSetIndex++) {
			// Create button
			final String characterSet = mCharacterSets[chrSetIndex];
			FlatButton characterSetButton = createCharacterSetButton(characterSet);
			characterSetButton.setId(chrSetIndex);
			
			// Set corners and initial state for first and last buttons
			int cornerRadius = 50;
			if (chrSetIndex == 0) {
				characterSetButton.setSticky(true);
				characterSetButton.setCornerRadius(cornerRadius, 0, 0, cornerRadius);
			} else if (chrSetIndex == (mCharacterSets.length - 1)) {
				characterSetButton.setCornerRadius(0, cornerRadius, cornerRadius, 0);
			}

			// Set click listener
			characterSetButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(TAG, "Clicked " + view.getId());
					for (int i = 0; i < mCharacterSetsLayout.getChildCount(); i++) {
						FlatButton localCharacterSet = (FlatButton) mCharacterSetsLayout.getChildAt(i);
						// Make the active button sticky
						localCharacterSet.setSticky(localCharacterSet.getId() == view.getId());
						// Reset state of buttons
						localCharacterSet.setPressed(false);
					}
					mlevelSelect.setLevelCategory(characterSet);
				}
			});
			
			mCharacterSetsLayout.addView(characterSetButton);
		}
				
		// Initialize level selection
		mlevelSelect = (LevelSelectFragment) this.getFragmentManager().findFragmentById(
				R.id.level_select_fragment);
		mlevelSelect.setLevelClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	LevelSelector levelSelector = mlevelSelect.getLevelSelector();
	        	levelSelector.setLevelIndex(position);
	        	
	        	// Start canvas activity
	        	Intent intent = new Intent(v.getContext(), CanvasActivity.class);
	        	intent.putExtra(CanvasActivity.BUNDLE_LEVELS, levelSelector.getLevels());
	        	intent.putExtra(CanvasActivity.BUNDLE_LEVEL_INDEX, levelSelector.getLevelIndex());
	        	startActivity(intent);
	        }
		});
	}
	
	public FlatButton createCharacterSetButton(String characterSet) {
		FlatButton characterSetButton = new FlatButton(this, null);
		
		LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonLayout.weight = 1;
		characterSetButton.setLayoutParams(buttonLayout);
		characterSetButton.setCornerRadius(0);
		characterSetButton.setPadding(0, 10, 0, 40);
		characterSetButton.setText(characterSet.substring(0, 1));
		characterSetButton.setTextColor(getResources().getColor(R.color.white));
		characterSetButton.setTextSize(30);
		characterSetButton.setCustomFont(UserSettings.getInstance().getLanguage().getFont());
		characterSetButton.setBaseColor(getResources().getColor(R.color.purpleLight));
		characterSetButton.setShadowColor(getResources().getColor(R.color.purpleDark));
		
		return characterSetButton;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//Refresh stars
		mlevelSelect.updateLevelViews();
	}
	
}
