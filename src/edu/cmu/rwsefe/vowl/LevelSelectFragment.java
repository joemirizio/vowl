package edu.cmu.rwsefe.vowl;

import java.util.HashMap;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import edu.cmu.rwsefe.vowl.model.DatabaseHandler;
import edu.cmu.rwsefe.vowl.ui.FlatButtonRating;


public class LevelSelectFragment extends Fragment {

	final Random random = new Random();

	private LevelAdapter mLevelAdapter;
	private GridView mLevelGridView;
	private HashMap<Integer, Integer> mMaxScoreCharacters;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    View view = inflater.inflate(R.layout.fragment_level_select, container, false);

	    String initalLevel = getActivity().getResources().getString(R.string.latin_alphabet_lower);

	    mLevelGridView = (GridView) view.findViewById(R.id.levelGrid);
	    mLevelAdapter = new LevelAdapter(getActivity(), initalLevel);
	    mLevelGridView.setAdapter(mLevelAdapter);
	    
	    return view;
	}

	public void setLevelCategory(int levelCategoryId) {
		String levels = getActivity().getResources().getString(levelCategoryId);
		mLevelAdapter.setLevels(levels);
	}

	public void setLevelClickListener(OnItemClickListener clickListener) {
		mLevelGridView.setOnItemClickListener(clickListener);
	}

	public String getLevelFromGridPosition(int position) {
		return mLevelAdapter.getItem(position).toString();
	}
	
	public void updateLevelViews() {
		updateMaxScores();
		mLevelAdapter.notifyDataSetChanged();
	}
	
	public void updateMaxScores() {
    	DatabaseHandler db = new DatabaseHandler(getActivity());
    	mMaxScoreCharacters = db.getMaxScoreForAllCharacters();
    	db.close();
	}


	class LevelAdapter extends BaseAdapter {
	    private Context mContext;
	    private String mLevels;

	    final int colorWhite;
	    final int colorBlueLight;
	    final int colorBlueDark;

	    public LevelAdapter(Context c, String levels) {
	        mContext = c;
	        mLevels = levels;

	    	colorWhite = c.getResources().getColor(R.color.white);
	    	colorBlueLight = c.getResources().getColor(R.color.blueLight);
	    	colorBlueDark = c.getResources().getColor(R.color.blueDark);
	    }

	    public void setLevels(String levels) {
	    	mLevels = levels;
	    	notifyDataSetChanged();
	    }

	    public int getCount() {
	        return mLevels.length();
	    }

	    public Object getItem(int position) {
	        return mLevels.charAt(position);
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	String chr = "" + mLevels.charAt(position);
	    	
	    	Integer maxScore = mMaxScoreCharacters.get((int)chr.charAt(0));
	    	int starRating = 0;
	    	
	    	if (maxScore != null)
	    	
		    	// Confidence to star algorithm not full determined
		    	starRating = maxScore / 10;

	    	// Create and style button
	    	FlatButtonRating button = new FlatButtonRating(mContext, null);
			button.setText("" + chr);
			button.setTextSize(30);
			button.setTextColor(colorWhite);
			button.setBaseColor(colorBlueLight);
			button.setShadowColor(colorBlueDark);
			button.setHeight(300);
			button.setRating(starRating);

			// Let level select capture clicks
			button.setClickable(false);
			button.setFocusable(false);

			return button;
	    }
	}
}
