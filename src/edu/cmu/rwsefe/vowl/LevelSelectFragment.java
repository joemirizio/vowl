package edu.cmu.rwsefe.vowl;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


public class LevelSelectFragment extends Fragment {
	
	public static final String LATIN_UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LATIN_LOWER_ALPHABET = LATIN_UPPER_ALPHABET.toLowerCase();
	public static final String LATIN_NUMBERS = "0123456789";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    View view = inflater.inflate(R.layout.fragment_level_select, container, false);
	    
	    String labels = LATIN_LOWER_ALPHABET;
	    generateLevelButtons(view, labels);
	    
	    final Button button = (Button) view.findViewById(R.id.navButton);
		button.setOnClickListener(new OnClickListener() {
			
		    String buttonText = (String) button.getText();

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), CanvasActivity.class);
				intent.putExtra("letter", buttonText);
				startActivity(intent);
			}	

		});
	    
	    return view;
	}
	
	protected void generateLevelButtons(View view, String labels) {
		String[] characters = labels.split("");
		int characterCount = characters.length;
		int characterIndex = 1;
		int colorWhite = this.getResources().getColor(R.color.white);
		TableLayout layout = (TableLayout) view.findViewById(R.id.levelTable);
		
		// TODO: Make level button builder dynamic
		for (int rowIndex = 0; rowIndex < layout.getChildCount(); rowIndex++) {
			TableRow row = (TableRow) layout.getChildAt(rowIndex);
			for (int buttonIndex = 0; buttonIndex < row.getChildCount(); buttonIndex++) {
				Button button = (Button) row.getChildAt(buttonIndex);
				button.setTextSize(36);
				button.setTextColor(colorWhite);
				button.setText(characters[characterIndex++]);
			}
		}
		
		/*TableRow row = new TableRow(layout.getContext());
		for(String chr : characters) {
			if (row.getChildCount() == 3) {
				layout.addView(row);
				row = new TableRow(layout.getContext());
			}
		}*/
	}
}