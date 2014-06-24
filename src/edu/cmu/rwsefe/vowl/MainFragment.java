package edu.cmu.rwsefe.vowl;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MainFragment extends Fragment {
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    return inflater.inflate(R.layout.fragment_main, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
	
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}
	
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}
	
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
	
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
	
		public PlaceholderFragment() {
		}
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_navitem, container,
					false);
			/*TextView textView = (TextView) rootView
					.findViewById(R.id.navText);
	        Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "fonts/FredokaOne-Regular.ttf");
	        Spannable buttonText = new SpannableString(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));   
	        buttonText.setSpan(font, 0, buttonText.length(), 0);
	        
			//Button b = (Button) rootView.findViewById(R.id.menuItem);
			textView.setText(buttonText);*/
	
			return rootView;
		}
		
	}
}