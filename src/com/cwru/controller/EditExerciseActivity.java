package com.cwru.controller;

import com.cwru.R;
import com.cwru.model.EditExerciseBankFragment;
import com.cwru.model.EditExerciseFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;

public class EditExerciseActivity extends FragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_exercise_tab);
		
		if (HomeScreen.isTablet) {
			EditExerciseBankFragment exBank = new EditExerciseBankFragment();
			EditExerciseFragment exExer = new EditExerciseFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.flEditExerciseLeftFrame, exBank);
			transaction.add(R.id.flEditExerciseRightFrame, exExer);
			transaction.commit();
			Log.d("STEVE", "TABLET NOT PHONE");
		} else {
			EditExerciseBankFragment exBank = new EditExerciseBankFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.flEditExerciseMainFrame, exBank);
			transaction.commit();
			Log.d("Fragment Commited", "FrAGMENT");
		}
	}
	
	public void onResume() {
		super.onResume();
		setContentView(R.layout.edit_exercise_tab);
		
		if (HomeScreen.isTablet) {
			EditExerciseBankFragment exBank = new EditExerciseBankFragment();
			EditExerciseFragment exExer = new EditExerciseFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.flEditExerciseLeftFrame, exBank);
			transaction.add(R.id.flEditExerciseRightFrame, exExer);
			transaction.commit();
			Log.d("STEVE", "TABLET NOT PHONE");
		} else {
			EditExerciseBankFragment exBank = new EditExerciseBankFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.flEditExerciseMainFrame, exBank);
			transaction.commit();
			Log.d("Fragment Commited", "FrAGMENT");
		}
	}
}
