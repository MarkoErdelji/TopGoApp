package com.example.uberapp_tim6.tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransition {
	
	public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int viewId)
	{
		FragmentTransaction transaction = activity.getSupportFragmentManager()
			.beginTransaction()
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.replace(viewId, newFragment);
		if(addToBackstack) transaction.addToBackStack(null);
		transaction.commit();
	}
}