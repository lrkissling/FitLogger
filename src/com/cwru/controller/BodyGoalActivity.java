package com.cwru.controller;

import com.cwru.R;
import com.cwru.fragments.BodyGoalBankFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class BodyGoalActivity extends FragmentActivity {
	private TextView appTitleBar;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.goal_information);
		
		
		BodyGoalBankFragment goalBank = new BodyGoalBankFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		// Tablet
		if (HomeScreen.isTablet) {
			transaction.add(R.id.flGoalLeftFrame, goalBank);
		
		// Phone
		} else {
			transaction.add(R.id.flGoalMainFrame, goalBank);
		}
		
		transaction.commit();
	}
	
	
}