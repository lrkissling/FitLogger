package com.cwru.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.cwru.R;
import com.cwru.controller.HomeScreen;
import com.cwru.controller.WorkoutExerciseListing;
import com.cwru.dao.DbAdapter;

public class EditWorkoutInformation extends Fragment {
	private EditText etWorkoutName;
	private Spinner spnWorkoutType;
	private Spinner spnRepeatWeeks;
	private CheckBox sunday;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox saturday;
	private DbAdapter mDbHelper;
	private String initialWorkoutName;
	private String initialWorkoutType;
	private String initialWorkoutRepeats;
	private int initialSunday;
	private int initialMonday;
	private int initialTuesday;
	private int initialWednesday;
	private int initialThursday;
	private int initialFriday;
	private int initialSaturday;

	public EditWorkoutInformation(String workoutName, Activity activity) { 
		initialWorkoutName = workoutName;
		/* Get all parameters */
		mDbHelper = new DbAdapter(activity);
		Workout workout = mDbHelper.getWorkoutFromName(initialWorkoutName);
		initialWorkoutType = workout.getType();
		initialWorkoutRepeats = workout.getRepeatWeeks();
		initialSunday = workout.getRepeatSunday();
		initialMonday = workout.getRepeatMonday();
		initialTuesday = workout.getRepeatTuesday();
		initialWednesday = workout.getRepeatWednesday();
		initialThursday = workout.getRepeatThursday();
		initialFriday = workout.getRepeatFriday();
		initialSaturday = workout.getRepeatSaturday();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}		
		View view = (ScrollView) inflater.inflate(R.layout.create_workout_information, container, false);		
		
		/* Grab UI features */
		etWorkoutName = (EditText) view.findViewById(R.id.etWorkoutName);
		spnWorkoutType = (Spinner) view.findViewById(R.id.spnWorkoutType);
	    spnRepeatWeeks = (Spinner) view.findViewById(R.id.spnWorkoutRepeatsWeek);
		sunday = new CheckBox(this.getActivity());
		monday = new CheckBox(this.getActivity());
		tuesday = new CheckBox(this.getActivity());
		wednesday = new CheckBox(this.getActivity());
		thursday = new CheckBox(this.getActivity());
		friday = new CheckBox(this.getActivity());
		saturday = new CheckBox(this.getActivity());
		
		/* Set data for spinners type and repeat weeks */
		ArrayAdapter<CharSequence> adapterWorkoutTypes = ArrayAdapter.createFromResource(
	            this.getActivity(), R.array.workoutTypes, android.R.layout.simple_spinner_item);
	    adapterWorkoutTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spnWorkoutType.setAdapter(adapterWorkoutTypes);
	    
	    ArrayAdapter<CharSequence> adapterRepeatWeeks = ArrayAdapter.createFromResource(this.getActivity(), R.array.workoutRepeatWeeks, android.R.layout.simple_spinner_item);
	    adapterRepeatWeeks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spnRepeatWeeks.setAdapter(adapterRepeatWeeks);
	    
	    /* Set initial values */
		etWorkoutName.setText(initialWorkoutName);

		spnWorkoutType.setSelection(adapterWorkoutTypes.getPosition(initialWorkoutType));
		Log.d("Workout Repeats", initialWorkoutRepeats);
		Log.d("Workout Repeats", adapterWorkoutTypes.getPosition(initialWorkoutRepeats) + " position");
		
		spnRepeatWeeks.setSelection(adapterRepeatWeeks.getPosition(initialWorkoutRepeats));
		/* Set check boxes */

		if (initialSunday == 0) { sunday.setChecked(true); }
		else { sunday.setChecked(false); }
		if (initialMonday == 0) { monday.setChecked(true); }
		else { monday.setChecked(false); }
		if ( initialTuesday == 0) { tuesday.setChecked(true); }
		else { tuesday.setChecked(false); }		
		if (initialWednesday == 0) { wednesday.setChecked(true); }
		else { wednesday.setChecked(false); }
		if (initialThursday == 0) { thursday.setChecked(true); }
		else { thursday.setChecked(false); }
		if (initialFriday == 0) { friday.setChecked(true); }
		else { friday.setChecked(false); }
		if (initialSaturday == 0) { saturday.setChecked(true); }
		else { saturday.setChecked(false); }
		
		
		Configuration c = this.getResources().getConfiguration();
		/* Lanscape View */
		if (c.orientation == Configuration.ORIENTATION_LANDSCAPE || HomeScreen.isTablet) {
			Log.d("Steve", "Horizontal1");
			/* Place Checkboxes all in one row */
			TableLayout tl = (TableLayout)  view.findViewById(R.id.tlCreateWorkoutInformationCheckBoxTable);
			TableRow tr = new TableRow(this.getActivity());
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			sunday.setText("S");
			sunday.setTag("chkRepeatSunday");
			tr.addView(sunday);

			monday.setText("M");
			monday.setTag("chkRepeatMonday");
			tr.addView(monday);
			
			tuesday = new CheckBox(this.getActivity());
			tuesday.setText("T");
			tuesday.setTag("chkRepeatTuesday");
			tr.addView(tuesday);
			
			wednesday.setText("W");
			wednesday.setTag("chkRepeatWednesday");
			tr.addView(wednesday);

			thursday.setText("R");
			thursday.setTag("chkRepeatThursday");
			tr.addView(thursday);
			
			friday.setText("F");
			friday.setTag("chkRepeatFriday");
			tr.addView(friday);
			
			saturday.setText("S");
			saturday.setTag("chkRepeatSaturday");
			tr.addView(saturday);

			tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		} 
		/* Portrait View */
		else {
			/* Place Checkboxes in two rows */
			Log.d("STEVE", "PORTRAIT VIEW ");
			LinearLayout l1 = new LinearLayout(this.getActivity());
			l1.setOrientation(LinearLayout.HORIZONTAL);
			l1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			LinearLayout l2 = new LinearLayout(this.getActivity());
			l2.setOrientation(LinearLayout.HORIZONTAL);
			l2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			sunday.setText("S");
			sunday.setTag("chkRepeatSunday");
			l1.addView(sunday);

			monday.setText("M");
			monday.setTag("chkRepeatMonday");
			l1.addView(monday);
			
			tuesday = new CheckBox(this.getActivity());
			tuesday.setText("T");
			tuesday.setTag("chkRepeatTuesday");
			l1.addView(tuesday);
			
			wednesday.setText("W");
			wednesday.setTag("chkRepeatWednesday");
			l2.addView(wednesday);

			thursday.setText("R");
			thursday.setTag("chkRepeatThursday");
			l2.addView(thursday);
			
			friday.setText("F");
			friday.setTag("chkRepeatFriday");
			l2.addView(friday);
			
			saturday.setText("S");
			saturday.setTag("chkRepeatSaturday");
			l2.addView(saturday);
			
			LinearLayout llcontainer = (LinearLayout) view.findViewById(R.id.llCreateWorkoutInformationContainer);
			llcontainer.addView(l1);
			llcontainer.addView(l2);
			
		}
		
		// Add Buttons
		// Button to Create Workout
		Button button = new Button(this.getActivity());
		button.setText("Update Workout");
		button.setOnClickListener(createWorkoutListener);
		
		Button button1 = new Button(this.getActivity());
		button.setText("Update Exercises and Exercise Ordering");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/* Launch intent to allow exercises to be added to workout and the sequence to be set */
				Intent intent = new Intent(EditWorkoutInformation.this.getActivity(), WorkoutExerciseListing.class);
				intent.putExtra("WorkoutName", initialWorkoutName);
				startActivity(intent);		
			}
		});
		
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.llCreateWorkoutInformationContainer);
		ll.addView(button);
		return view;
	}
	
	private boolean validateWorkoutName(String workoutName) {
		return mDbHelper.workoutNameExist(workoutName);
	}
	
	/**
	 * Create Workout Button Click Listener
	 */
	View.OnClickListener createWorkoutListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Get Workout Name
			String workoutName = etWorkoutName.getText().toString();
			/* if name exists already
			 * alert error, must have unique name
			 */
			if (validateWorkoutName(workoutName)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(EditWorkoutInformation.this.getActivity());
				builder.setMessage("Error: " + workoutName + " already exists as a workout name.  Please select a unique name for the workout");
				builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
			else {
				// Get Workout Type
				String workoutType = (String) spnWorkoutType.getSelectedItem();
				// Get Repeat Weeks 
				String workoutRepeatWeeks = (String) spnRepeatWeeks.getSelectedItem();
				
				String repeatDays = "";
				
				/* Set Default 1 = Not Checked, 0 = Checked.  0 - True 1 - False */
				int repeatSunday = 1;
				int repeatMonday = 1;
				int repeatTuesday = 1;
				int repeatWednesday = 1;
				int repeatThursday = 1;
				int repeatFriday = 1;
				int repeatSaturday = 1;
							
				if (sunday.isChecked()) { repeatSunday = 0; }
				if (monday.isChecked()) { repeatMonday = 0; }
				if (tuesday.isChecked()) { repeatTuesday = 0; }
				if (wednesday.isChecked()) { repeatWednesday = 0; }
				if (thursday.isChecked()) { repeatThursday = 0; }
				if (friday.isChecked()) { repeatFriday = 0; }
				if (saturday.isChecked()) { repeatSaturday = 0; }
				
				String exerciseSequence = "";
				Workout workoutToCreate = new Workout(workoutName, workoutType,exerciseSequence, workoutRepeatWeeks, 
													  repeatSunday, repeatMonday, repeatTuesday, 
													  repeatWednesday, repeatThursday, repeatFriday, repeatSaturday);
				Log.d("Button", "Create Workout Clicked");
				Log.d("Workout Name", workoutName);
				Log.d("Workout Type", workoutType);
				Log.d("Workout Repeat Weeks", workoutRepeatWeeks);
				
				/** TODO
				 * Add workout repeat information to insert command
				 */
				/* Create Workout in the Database */
				mDbHelper.createWorkout(workoutToCreate);
				
				/* Launch intent to allow exercises to be added to workout and the sequence to be set */
				Intent intent = new Intent(EditWorkoutInformation.this.getActivity(), WorkoutExerciseListing.class);
				intent.putExtra("WorkoutName", workoutName);
				startActivity(intent);		
			}
		}
	};
	
}
