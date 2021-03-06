package com.cwru.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cwru.R;
import com.cwru.dao.DbAdapter;
import com.cwru.model.WorkoutResult;
import com.cwru.utils.DateConverter;
import com.cwru.utils.GraphBuilder;

/**
 * Activity to display analytics for user workouts.  Utilizes a web view to present information to the user.
 * Activity falls under the workout analytics tab under the analytics module.  
 * @author lkissling
 *
 */
public class WorkoutAnalyticsActivity extends Activity {
	DbAdapter mDbHelper;
	
	WebView view;
	RadioGroup group;
	RadioButton month;
	RadioButton threeMonths;
	RadioButton year;
	
	String monthHTML;
	String threeMonthHTML;
	String yearHTML;
	
	
	@Override
	/**
	 * onCreate callback.  Sets the layout and webview for the activity.  
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DbAdapter(this);
		
		setContentView(R.layout.workout_analytics);
//		findViewById(R.id.llWorkoutAnalytics).setBackgroundColor(0xFFFFFFFF);
		
		
		view = (WebView) findViewById(R.id.wvWorkoutAnalytics);
		view.getSettings().setJavaScriptEnabled(true);
		view.setBackgroundColor(0xFFFFFFFF);
				
		group = (RadioGroup) findViewById(R.id.rgWorkoutAnalyticsSwitches);
		group.setOnCheckedChangeListener(groupListener);
		
		month = (RadioButton) findViewById(R.id.rbMonth);
		threeMonths = (RadioButton) findViewById(R.id.rbThreeMonths);
		year = (RadioButton) findViewById(R.id.rbYear);
		
		ArrayList<WorkoutResult> resultList = mDbHelper.getAllWorkoutResults();	
		
		long current = System.currentTimeMillis();
		long decrWeek = 7 * 24 * 60 * 60 * 1000;
		long decrMonth = 30L * 24L * 60L * 60L * 1000L;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		int now = Integer.parseInt(sdf.format(date));
		
		HashMap<Integer, Double> monthMap = new LinkedHashMap<Integer, Double>();
		HashMap<Integer, Double> threeMonthMap = new LinkedHashMap<Integer, Double>();
		HashMap<Integer, Double> yearMap = new LinkedHashMap<Integer, Double>();
		
		ArrayList<Integer> weekList = new ArrayList<Integer>();
		ArrayList<Integer> monthList = new ArrayList<Integer>();
		
		for (int i = 12; i > 0; i--) {
			Date d = new Date(current - i * decrWeek);
			
			int j = Integer.parseInt(sdf.format(d));
			if (i <= 4) {
				monthMap.put(j, 0.0);
			}
			threeMonthMap.put(j, 0.0);
			weekList.add(j);
			
			d = new Date(current - ((long) i) * decrMonth);
			j = Integer.parseInt(sdf.format(d));
			yearMap.put(j, 0.0);
			monthList.add(j);
		}
		
		DateConverter dc = new DateConverter();
		
		for (int i = 0; i < resultList.size(); i++) {
			WorkoutResult r = resultList.get(i);
			if (i == 0 
					|| r.getWorkoutId() == resultList.get(i - 1).getWorkoutId()
					&& !r.getDate().equals(resultList.get(i - 1).getDate())) {
				int rDate = dc.stringDateToInt(r.getDate());
				if (rDate > now - 1000) {
					for (int j = 11; j >= 0; j--) {
						if (rDate > weekList.get(j) && (j == 11 || rDate <= weekList.get(j + 1))) {
							if (j > 7) {
								monthMap.put(weekList.get(j), monthMap.get(weekList.get(j)) + 1);
							}
							threeMonthMap.put(weekList.get(j), threeMonthMap.get(weekList.get(j)) + 1);
						}
						
						if (rDate > monthList.get(j) && (j == 11 || rDate <= monthList.get(j + 1))) {
							yearMap.put(monthList.get(j), yearMap.get(monthList.get(j)) + 1);
						}
					}
				}
			}
		}
		
		
//		monthMap = mDbHelper.getWorkoutResultsForPastWeeks(4);
//		threeMonthMap = mDbHelper.getWorkResultsForPastWeeks(12);
//		yearMap = mDbHelper.getWorkoutResultsForPastWeeks(52);
		
		
//		for (int i = 1; i < 60; i++) {
//			monthMap.put(Integer.toString(i), i%5);
//			threeMonthMap.put(Integer.toString(i), i%10);
//			yearMap.put(Integer.toString(i), i%15);
//		}
		
		GraphBuilder gb = new GraphBuilder();
		
		monthHTML = gb.buildHTML(monthMap, "Workout frequency by week for the past month");
		threeMonthHTML = gb.buildHTML(threeMonthMap, "Workout frequency by week for the past three months.");
		yearHTML = gb.buildHTML(yearMap, "Workout frequency by month for the past year.");
		
		if (HomeScreen.isTablet) {
			view.loadUrl("file:///android_asset/js/loading_tablet.html");
		} else {
			view.loadUrl("file:///android_asset/js/loading_phone.html");
		}
//		month.setChecked(true);
		
//		view.loadDataWithBaseURL("file:///android_asset/js/", monthHTML, "text/html", "utf-8", null);
	}
	
	/**
	 * Listener to handle the onchecked changed events for the radio buttons that exist int the view
	 */
	RadioGroup.OnCheckedChangeListener groupListener = new RadioGroup.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			
			case R.id.rbMonth:
				view.loadDataWithBaseURL("file:///android_asset/js/", monthHTML, "text/html", "utf-8", null);
				break;
			case R.id.rbThreeMonths:
				view.loadDataWithBaseURL("file:///android_asset/js/", threeMonthHTML, "text/html", "utf-8", null);
				break;
			case R.id.rbYear:
				view.loadDataWithBaseURL("file:///android_asset/js/", yearHTML, "text/html", "utf-8", null);
				break;
			}
		}
	};
	
}