package edu.cmu.rwsefe.vowl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class LevelSelectActivity extends Activity {

	private LevelSelectFragment mlevelSelect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		
		mlevelSelect = (LevelSelectFragment) this.getFragmentManager().findFragmentById(R.id.level_select_fragment);
		mlevelSelect.setLevelClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	String level = mlevelSelect.getLevelFromGridPosition(position);
	        	// TODO Remove and launch canvas
	        	Toast.makeText(LevelSelectActivity.this, "" + level, Toast.LENGTH_SHORT).show();
	        }
		});
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    if (!checked) { return; }
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.level_alphabet_upper:
	        	mlevelSelect.setLevelCategory(R.string.latin_alphabet_upper);
	            break;
	        case R.id.level_alphabet_lower:
	        	mlevelSelect.setLevelCategory(R.string.latin_alphabet_lower);
	            break;
	        case R.id.level_numerals:
	        	mlevelSelect.setLevelCategory(R.string.latin_numerals);
	            break;
	    }
	}
}
