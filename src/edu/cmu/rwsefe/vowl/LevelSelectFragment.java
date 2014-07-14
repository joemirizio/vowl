package edu.cmu.rwsefe.vowl;

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
import edu.cmu.rwsefe.vowl.model.ScoreKeeper;
import edu.cmu.rwsefe.vowl.ui.FlatButtonRating;


public class LevelSelectFragment extends Fragment {

	final Random random = new Random();

	private LevelAdapter mLevelAdapter;
	private GridView mLevelGridView;
	private LevelSelector mLevelSelector;
	private ScoreKeeper mScoreKeeper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    View view = inflater.inflate(R.layout.fragment_level_select, container, false);

	    String initialLevel = getActivity().getResources().getString(R.string.latin_alphabet_lower);
	    mLevelSelector = new LevelSelector(initialLevel);
	    
	    mScoreKeeper = new ScoreKeeper(getActivity());

	    mLevelGridView = (GridView) view.findViewById(R.id.levelGrid);
	    mLevelAdapter = new LevelAdapter(getActivity(), mLevelSelector, mScoreKeeper);
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
		mScoreKeeper.updateScores();
		mLevelAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onDestroy() {
		mScoreKeeper.close();
		super.onDestroy();
	}


	class LevelAdapter extends BaseAdapter {
	    private Context mContext;
	    private LevelSelector mLevelSelector;
	    private ScoreKeeper mScoreKeeper;

	    final int colorWhite;
	    final int colorBlueLight;
	    final int colorBlueDark;

	    public LevelAdapter(Context c, LevelSelector levelSelector, ScoreKeeper scoreKeeper) {
	        mContext = c;
	        mLevelSelector = levelSelector;
	        mScoreKeeper = scoreKeeper;
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
	    	String character = "" + mLevelSelector.getLevel(position);

	    	int rating = mScoreKeeper.getScoreRating(character);

	    	// Create and style button
	    	FlatButtonRating button = new FlatButtonRating(mContext, null);
			button.setText("" + character);
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
