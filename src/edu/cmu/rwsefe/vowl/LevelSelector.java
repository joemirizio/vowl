package edu.cmu.rwsefe.vowl;

import java.util.EventListener;

import android.util.Log;


public class LevelSelector {
	private final String TAG = LevelSelector.class.getName();
	
	String mLevels;
	int mCurrentIndex;
	
	LevelSetListener mLevelSetListener;
	LevelChangeListener mLevelChangeListener;
	
	public LevelSelector(String levels) {
		this(levels, 0);
	}
	
	public LevelSelector(String levels, int index) {
		mLevels = levels;
		mCurrentIndex = index;
	}

	public int getLevelCount() {
		return mLevels.length();
	}
	
	public String getLevel() {
		return getLevel(mCurrentIndex);
	}
	public String getLevel(int index) {
		return "" + mLevels.charAt(index);
	}

	public String getLevels() {
		return mLevels;
	}
	
	public int getLevelIndex() {
		return mCurrentIndex;
	}
	
	public String nextLevel() {
		return setLevelIndex(wrapLevelIndex(mCurrentIndex + 1));
	}
	
	public String prevLevel() {
		return setLevelIndex(wrapLevelIndex(mCurrentIndex - 1));
	}
	
	public String retryLevel() {
		return setLevelIndex(wrapLevelIndex(mCurrentIndex));
	}
	
	private int wrapLevelIndex(int index) {
		if (index < 0) {
			return mLevels.length() - 1;
		} else if (index >= mLevels.length()) {
			return 0;
		}
		return index;
	}

	public void setLevels(String levels) {
		this.mLevels = levels;
		this.mCurrentIndex = 0;
		if (mLevelSetListener != null) {
			mLevelSetListener.onLevelSet();
		}
	}
	
	public String setLevelIndex(int index) {
		if (index < 0 || index >= mLevels.length()) {
			throw new IndexOutOfBoundsException();
		}
		mCurrentIndex = index;
		if (mLevelChangeListener != null) {
			mLevelChangeListener.onLevelChange(mCurrentIndex);
		}
		return getLevel();
	}
	
	public void setLevelSetListener(LevelSetListener levelSetListener) {
		mLevelSetListener = levelSetListener;
	}
	public void setLevelChangeListener(LevelChangeListener levelChangeListener) {
		mLevelChangeListener = levelChangeListener;
	}
	
	
	public interface LevelSetListener extends EventListener {
		public void onLevelSet();
	}
	
	public interface LevelChangeListener extends EventListener {
		public void onLevelChange(int index);
	}
}
