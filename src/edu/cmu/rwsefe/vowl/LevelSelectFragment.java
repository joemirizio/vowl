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
import edu.cmu.rwsefe.vowl.LevelSelector.LevelSetListener;
import edu.cmu.rwsefe.vowl.model.DatabaseHandler;
import edu.cmu.rwsefe.vowl.ui.FlatButtonRating;


public class LevelSelectFragment extends Fragment {

	final Random random = new Random();

	private LevelAdapter mLevelAdapter;
	private GridView mLevelGridView;
	private LevelSelector mLevelSelector;
	private HashMap<Integer, Integer> mMaxScoreCharacters;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    View view = inflater.inflate(R.layout.fragment_level_select, container, false);

	    String initialLevel = getActivity().getResources().getString(R.string.latin_alphabet_lower);
	    mLevelSelector = new LevelSelector(initialLevel);

	    mLevelGridView = (GridView) view.findViewById(R.id.levelGrid);
	    mLevelAdapter = new LevelAdapter(getActivity(), mLevelSelector);
	    mLevelGridView.setAdapter(mLevelAdapter);

	    return view;
	}

	public void setLevelCategory(int levelCategoryId) {
		String levels = getActivity().getResources().getString(levelCategoryId);
		mLevelSelector.setLevels(levels);
	}

	public void setLevelClickListener(OnItemClickListener clickListener) {
		mLevelGridView.setOnItemClickListener(clickListener);
	}

	public LevelSelector getLevelSelector() {
		return mLevelSelector;
	}

	public String getLevelFromGridPosition(int position) {
		return mLevelSelector.getLevel(position);
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
	    private LevelSelector mLevelSelector;

	    final int colorWhite;
	    final int colorBlueLight;
	    final int colorBlueDark;

	    public LevelAdapter(Context c, LevelSelector levelSelector) {
	        mContext = c;
	        mLevelSelector = levelSelector;
	        mLevelSelector.setLevelSetListener(new LevelSetListener() {
	        	public void onLevelSet() {
	        		notifyDataSetChanged();
	        	}
	        });

	    	colorWhite = c.getResources().getColor(R.color.white);
	    	colorBlueLight = c.getResources().getColor(R.color.blueLight);
	    	colorBlueDark = c.getResources().getColor(R.color.blueDark);
	    }

	    public int getCount() {
	        return mLevelSelector.getLevelCount();
	    }

	    public Object getItem(int position) {
	        return mLevelSelector.getLevel(position);
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	String chr = "" + mLevelSelector.getLevel(position);

			// TODO Confidence to star algorithm not full determined
	    	int rating = 0;
	    	if (mMaxScoreCharacters != null) {
		    	Integer maxScore = mMaxScoreCharacters.get((int)chr.charAt(0));
	    		rating = (maxScore != null) ? maxScore / 10 : rating;
	    	}

	    	// Create and style button
	    	FlatButtonRating button = new FlatButtonRating(mContext, null);
			button.setText("" + chr);
			button.setTextSize(30);
			button.setTextColor(colorWhite);
			button.setBaseColor(colorBlueLight);
			button.setShadowColor(colorBlueDark);
			button.setHeight(300);
			button.setRating(rating);

			// Let level select capture clicks
			button.setClickable(false);
			button.setFocusable(false);

			return button;
	    }
	}
}
