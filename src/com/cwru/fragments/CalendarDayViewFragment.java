package com.cwru.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cwru.R;
import com.cwru.controller.HomeScreen;
import com.cwru.dao.DbAdapter;
import com.cwru.fragments.WorkoutListingFragment.onWorkoutListingClickListener;

public class CalendarDayViewFragment extends ListFragment {
	private DbAdapter mDbHelper;
	private String date;
	private Context context;
	private static onWorkoutListingClickListener listener;
	private static scheduleWorkoutListener workoutScheduleListener;
	
	
	public CalendarDayViewFragment(Context context, String date) {
		this.date = date;
		this.context = context;
		mDbHelper = new DbAdapter(context);
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}		
		Log.d("Steve", "WorkoutListingFragment");
		View view = (LinearLayout) inflater.inflate(R.layout.calendar_day_listing, container, false);
		// Set Adapter
		// Get List content
		//String [] workouts = getWorkoutList();
		ArrayList<String> workouts = new ArrayList<String>();
		if (!dateInPast()) {
			Log.d("Steve", "Date IN Future");
			workouts.add("Schedule a workout...");
		}
		else {
			Log.d("Steve", "Date in PAST");
		}
		ArrayList<String> workoutsForDate = mDbHelper.getWorkoutsForDate(date);
		if (workoutsForDate.size() == 0 && workouts.size() == 0) {
			workouts.add("No Workouts Scheduled for Day");
		} else {
			workouts.addAll(workoutsForDate);
		}
		this.setListAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, workouts));

		return view;
	}
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		String workoutName = (String) getListAdapter().getItem(position);
		Log.d("Steve", "Workout Name: " + workoutName);
		// schedule workout
		if (workoutName.equals("No Workouts Scheduled for Day")) {
			// Do Nothing
		}
		else if(position == 0) {
			workoutScheduleListener.scheduleWorkout(date);
		}
		else {
			if (!dateInPast()) {
				listener.onWorkoutListingListenerClick(workoutName);
			}
			else {
				int workoutId = mDbHelper.getWorkoutIdFromName(workoutName);
				WorkoutSummaryFragment workoutSummary = new WorkoutSummaryFragment(context,workoutId, date);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.flCalendarMainFrame, workoutSummary);
				transaction.commit();
			}
		}
	}
	
	private boolean dateInPast() {
		String [] dateSplit = date.split("/");
		int y = Integer.parseInt(dateSplit[0]);
		int m = Integer.parseInt(dateSplit[1]);
		int d = Integer.parseInt(dateSplit[2]);
		
		Log.d("Steve", "Y: " + y + " | M: " + m + " | D: " + d);
		
		
		Calendar calendar = Calendar.getInstance();
		int calY = calendar.get(Calendar.YEAR);
		int calM = calendar.get(Calendar.MONTH) + 1;
		int calD = calendar.get(Calendar.DAY_OF_MONTH);
		Log.d("Steve", "Y: " + calY + " | M: " + calM + " | D: " + calD);
		// past year
		if (calY > y) { return true; }
		// same year past month
		else if (calY == y && calM > m) { return true; }
		// same year same month past day
		else if (calY == y && calM == m && calD > d) { return true; }
		
		// in future
		return false;		
	}
	
	public static void setOnWorkoutListingClickListener(onWorkoutListingClickListener listener) {
		CalendarDayViewFragment.listener = listener;
	}
	
	public interface scheduleWorkoutListener {
		void scheduleWorkout(String date);
	}
	
	public static void setScheduleWorkout(scheduleWorkoutListener listener) {
		CalendarDayViewFragment.workoutScheduleListener = listener;
	}
}