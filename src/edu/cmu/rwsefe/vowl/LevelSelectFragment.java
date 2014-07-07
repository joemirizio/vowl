package edu.cmu.rwsefe.vowl;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import edu.cmu.rwsefe.vowl.ui.FlatButtonRating;


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
	    
	    return view;
	}
	
	protected void generateLevelButtons(View view, String labels) {
		final char[] characters = labels.toCharArray();
		final int colorWhite = this.getResources().getColor(R.color.white);
		final int colorBlueLight = this.getResources().getColor(R.color.blueLight);
		final int colorBlueDark = this.getResources().getColor(R.color.blueDark);

		final int buttonsPerRow = 3;
		final int padding = 10;
		
		TableLayout layout = (TableLayout) view;//.findViewById(R.id.levelTable);
		layout.removeAllViews();
			
		TableRow row = new TableRow(layout.getContext());
		for(char chr : characters) {
			// Create button
			FlatButtonRating button = new FlatButtonRating(layout.getContext(), null);
			button.setText("" + chr);
			button.setTextSize(30);
			button.setTextColor(colorWhite);
			button.setBaseColor(colorBlueLight);
			button.setShadowColor(colorBlueDark);
			TableRow.LayoutParams buttonLayout = new TableRow.LayoutParams(0, 300);
			buttonLayout.setMargins(padding, padding, padding, padding);
			button.setLayoutParams(buttonLayout);
			
			row.addView(button);
			
			// Move to next row if current row is full
			if (row.getChildCount() == buttonsPerRow) {
				layout.addView(row);
				row = new TableRow(layout.getContext());
			}
		}
		
		// Add last row if not empty
		if (row.getChildCount() > 0) {
			layout.addView(row);
		}
	}
}