package edu.cmu.rwsefe.vowl;

import java.util.Locale;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";

    private Fragment mainFragment;
    private Fragment splashFragment;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
        
        // Add the fragments to the 'fragment_container' FrameLayout
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        mainFragment = new MainFragment();
        splashFragment = new SplashFragment();
        ft.add(R.id.fragment_container, mainFragment);
        ft.add(R.id.fragment_container, splashFragment, "a");
        ft.commit();
	}
}
