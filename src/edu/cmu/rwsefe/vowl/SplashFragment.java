package edu.cmu.rwsefe.vowl;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class SplashFragment extends Fragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    return inflater.inflate(R.layout.fragment_splash, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
        // Play slide up animation for splash screen
        FragmentTransaction ft2 = this.getActivity().getFragmentManager().beginTransaction();
        ft2.setCustomAnimations(R.anim.slide_in_bottom, 0);
        ft2.hide(this);
        ft2.show(this);
        ft2.commit();
        
        // Remove click listener
        v.setOnClickListener(null);
	}
}