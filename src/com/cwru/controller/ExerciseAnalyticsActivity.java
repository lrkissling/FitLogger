package com.cwru.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.cwru.R;
import com.cwru.fragments.ExerciseAnalyticsBankFragment;

public class ExerciseAnalyticsActivity extends FragmentActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.analytics_information);
		
		ExerciseAnalyticsBankFragment bank = new ExerciseAnalyticsBankFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		// Tablet
		if (HomeScreen.isTablet) {
			transaction.add(R.id.flAnalyticsLeftFrame, bank);
		
		// Phone
		} else {
			transaction.add(R.id.flAnalyticsMainFrame, bank);
		}
		
		transaction.commit();
	}
	
	
}