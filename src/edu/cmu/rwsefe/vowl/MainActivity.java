package edu.cmu.rwsefe.vowl;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.cmu.rwsefe.vowl.mode.play.critter.CritterCounterActivity;
import edu.cmu.rwsefe.vowl.model.UserSettings;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getName();

    private Fragment mainFragment;
    private Fragment splashFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UserSettings.getInstance().Initialize(getApplicationContext());

		setContentView(R.layout.activity_main);

        // Add the fragments to the 'fragment_container' FrameLayout
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        mainFragment = new MainFragment();
        splashFragment = new SplashFragment();
        ft.add(R.id.fragment_container, mainFragment);
        ft.add(R.id.fragment_container, splashFragment);
        ft.commit();
	}

	public void menuLearnOnClick(View v) {
		// Start level select when nav button is clicked
		Intent intent = new Intent(v.getContext(), LevelSelectActivity.class);
		startActivity(intent);
	}

	public void menuPracticeOnClick(View v) {
		// TODO Implement practice mode
		Toast.makeText(this, "Practice mode not implemented", Toast.LENGTH_SHORT).show();
	}

	public void menuPlayOnClick(View v) {
		Intent intent = new Intent(v.getContext(), CritterCounterActivity.class);
		startActivity(intent);
	}

	public void settingsOnClick(View v) {
		Intent intent = new Intent(v.getContext(), UserSettingsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		View v = this.findViewById(R.id.pager);
		Log.d(TAG, "Height: " + v.getHeight());
	}
}
