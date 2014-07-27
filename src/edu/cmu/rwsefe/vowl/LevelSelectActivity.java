package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import edu.cmu.rwsefe.vowl.model.UserSettings;
import edu.cmu.rwsefe.vowl.ui.FlatButton;

public class LevelSelectActivity extends Activity {

	private LevelSelectFragment mlevelSelect;
	private String[] mCharacterSets;
	LinearLayout mCharacterSetsLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		
		Resources resources = getResources();
		mCharacterSetsLayout = (LinearLayout)findViewById(R.id.character_sets);
		mCharacterSets = UserSettings.getInstance().getLanguage().getCharacterSets();
		
		for(int i = 0; i < mCharacterSets.length; i++) {
			
			final int index = i;
			final FlatButton characterSetButton = new FlatButton(this, null);
			characterSetButton.setPadding(0, 10, 0, 10);
			characterSetButton.setText(mCharacterSets[i].substring(0, 1));
			characterSetButton.setTextColor(resources.getColor(R.color.white));
			characterSetButton.setTextSize(30);
			characterSetButton.setBaseColor(resources.getColor(R.color.purpleDark));
			characterSetButton.setShadowColor(resources.getColor(R.color.purpleLight));
			characterSetButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {

					characterSetButton.setSticky(true);
					ViewGroup characterSetsViewGroup = (ViewGroup) mCharacterSetsLayout;
					for (int i = 0; i < characterSetsViewGroup.getChildCount(); i++) {
						FlatButton localCharacterSet = (FlatButton) characterSetsViewGroup.getChildAt(i);
						// Make the active button sticky
						localCharacterSet.setSticky(localCharacterSet.getId() == view.getId());
						// Reset state of buttons
						localCharacterSet.setPressed(false);
					}
					
					mlevelSelect.setLevelCategory(mCharacterSets[index]);
				}
			});
			mCharacterSetsLayout.addView(characterSetButton);
		}
		
		mlevelSelect = (LevelSelectFragment) this.getFragmentManager().findFragmentById(R.id.level_select_fragment);
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
	
	/*
	public void onCharacterSetClicked(View view) {
		
		ViewGroup characterSets = (ViewGroup) findViewById(R.id.character_sets);
		for (int i = 0; i < characterSets.getChildCount(); i++) {
			FlatButton characterSet = (FlatButton) characterSets.getChildAt(i);
			// Make the active button sticky
			characterSet.setSticky(characterSet.getId() == view.getId());
			// Reset state of buttons
			characterSet.setPressed(false);
		}
		
		//mlevelSelect.setLevelCategory(mCharacterSets[view.getId()]);
		
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.level_alphabet_lower:
	        	mlevelSelect.setLevelCategory("abcdefghijklmnopqrstuvwxyz");
	            break;
	        case R.id.level_alphabet_upper:
	        	mlevelSelect.setLevelCategory("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	            break;
	        case R.id.level_numerals:
	        	mlevelSelect.setLevelCategory("0123456789");
	            break;
	    }
	}
	*/
	
	@Override
	public void onResume() {
		super.onResume();
		
		//Refresh stars
		mlevelSelect.updateLevelViews();
	}
	
}
