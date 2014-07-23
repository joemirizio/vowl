package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import edu.cmu.rwsefe.vowl.model.UserSettings;
import edu.cmu.rwsefe.vowl.ui.FlatButton;

public class LevelSelectActivity extends Activity {

	private LevelSelectFragment mlevelSelect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		
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
	
	public void onCharacterSetClicked(View view) {
		
		ViewGroup characterSets = (ViewGroup) findViewById(R.id.character_sets);
		for (int i = 0; i < characterSets.getChildCount(); i++) {
			FlatButton characterSet = (FlatButton) characterSets.getChildAt(i);
			// Make the active button sticky
			characterSet.setSticky(characterSet.getId() == view.getId());
			// Reset state of buttons
			characterSet.setPressed(false);
		}
		
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.level_alphabet_lower:
	        	mlevelSelect.setLevelCategory(UserSettings.getInstance().getLetters());
	            break;
	        case R.id.level_alphabet_upper:
	        	mlevelSelect.setLevelCategory(UserSettings.getInstance().getUppercaseLetters());
	            break;
	        case R.id.level_numerals:
	        	mlevelSelect.setLevelCategory(UserSettings.getInstance().getNumerals());
	            break;
	    }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//Refresh stars
		mlevelSelect.updateLevelViews();
	}
	
}
