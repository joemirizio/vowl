package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class LevelSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    if (!checked) { return; }
	    
	    LevelSelectFragment levelSelect = 
	    		(LevelSelectFragment) this.getFragmentManager().findFragmentById(R.id.level_select_fragment);
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.level_alphabet_upper:
	        	levelSelect.generateLevelButtons(levelSelect.getView(), LevelSelectFragment.LATIN_UPPER_ALPHABET);
	            break;
	        case R.id.level_alphabet_lower:
	        	levelSelect.generateLevelButtons(levelSelect.getView(), LevelSelectFragment.LATIN_LOWER_ALPHABET);
	            break;
	        case R.id.level_number:
	        	levelSelect.generateLevelButtons(levelSelect.getView(), LevelSelectFragment.LATIN_NUMBERS);
	            break;
	    }
	}
}
