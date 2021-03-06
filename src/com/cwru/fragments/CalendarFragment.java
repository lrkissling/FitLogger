package com.cwru.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cwru.R;
import com.cwru.dao.DbAdapter;
import com.cwru.model.DayOfWeekAdapter;

/**
 * 
 * @author sacrilley
 *
 */
public class CalendarFragment extends Fragment {
	private Button btnPrevMonth;
	private Button btnNextMonth;
	private TextView tvMonth;
	private GridView gvCalendar;
	private GridCellAdapter adapter;
	private GridView gvDaysOfWeek;
	private boolean returnDate;
	private String dateWhereClause;
	private static returnDateListener listenerReturnDate;
	private static goToDayEventsListener listenerGoToDayEvents;
	private DayOfWeekAdapter dowAdapter;
	private Context context;
	
	private Calendar calendar;
	
	private static String [] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August",
										   "September","October","November","December" };
	
	/**
	 * 
	 * @param context
	 * @param returnDate
	 */
	public CalendarFragment(Context context, boolean returnDate) {
		this.context = context;
		calendar = Calendar.getInstance();
		this.returnDate = returnDate;
		// Get Current Month
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		dateWhereClause = "where ";
	}
	
	/**
	 * 
	 * @param context
	 * @param returnDate
	 * @param calendar
	 */
	public CalendarFragment(Context context, boolean returnDate, Calendar calendar) { 
		this.context = context;
		this.calendar = calendar;
		this.returnDate = returnDate;
		dateWhereClause = "where ";
	}
	
	@Override
	/**
	 * 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}	
		View view = (LinearLayout) inflater.inflate(R.layout.calendar_view, container, false);
		
		btnPrevMonth = (Button) view.findViewById(R.id.btnCalendarViewPrevMonth);
		btnNextMonth = (Button) view.findViewById(R.id.btnCalendarViewNextMonth);
		tvMonth = (TextView) view.findViewById(R.id.tvCalendarViewCurrentMonth);
		gvCalendar = (GridView) view.findViewById(R.id.gvCalendarViewCalendar);
		gvCalendar.setBackgroundColor(Color.LTGRAY);
		gvDaysOfWeek = (GridView) view.findViewById(R.id.gvCalendarViewDaysOfWeekHeading);

		
		dowAdapter = new DayOfWeekAdapter(this.getActivity());
		gvDaysOfWeek.setAdapter(dowAdapter);
		
		tvMonth.setText(monthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
		adapter = new GridCellAdapter(this.getActivity(), R.id.btnCalendarDayGridCell);
		adapter.notifyDataSetChanged();
		gvCalendar.setAdapter(adapter);
		btnPrevMonth.setOnClickListener(prevMonthListener);
		btnNextMonth.setOnClickListener(nextMonthListener);
		
		return view;
	}
	
	/**
	 * 
	 */
	View.OnClickListener nextMonthListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// set to next month
			Log.d("Steve", "Calendar before " + calendar.get(Calendar.MONTH));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			// calendar already incremented in the getcalendararray
			//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			Log.d("Steve", "Calendar incremented to " + calendar.get(Calendar.MONTH));
			FragmentTransaction transaction = CalendarFragment.this.getFragmentManager().beginTransaction();
			CalendarFragment calendarFragment = new CalendarFragment(context, returnDate, calendar);
			transaction.replace(R.id.flCalendarMainFrame, calendarFragment);
			transaction.commit();
		}
	};
	
	/**
	 * 
	 */
	View.OnClickListener prevMonthListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// set to next month
			Log.d("Steve", "Calendar before " + calendar.get(Calendar.MONTH));
			// calendar already incremented in the getcalendararray
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1);
			Log.d("Steve", "Calendar incremented to " + calendar.get(Calendar.MONTH));
			FragmentTransaction transaction = CalendarFragment.this.getFragmentManager().beginTransaction();
			CalendarFragment calendarFragment = new CalendarFragment(context, returnDate, calendar);
			transaction.replace(R.id.flCalendarMainFrame, calendarFragment);
			transaction.commit();
		}
	};
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getCalendarArray() {
		ArrayList<String> dates = new ArrayList<String>();
		
		// Set Day to first of month
		int month = calendar.get(Calendar.MONTH);
		int dom = calendar.get(Calendar.DAY_OF_MONTH);
		Log.d("STEVE", "GET CALENAR ARRAY MONTH: " + month + " | YEAR: " + calendar.get(Calendar.YEAR));

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// Decrement until we hit sunday
		while (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		}
		
		Log.d("Steve", "Get Calendar ARRRRRRRRRRRRRRRRRRRRRRRAAAAAAAAAAAAAAAAAAAYYYYYYYYYYYYY");
		Log.d("Steve", "month: " + month + " | calendar month: " + calendar.get(Calendar.MONTH));
		
		// Add prior month days
		// Date String ==> DD-COLOR-MM-YY
		if (month != calendar.get(Calendar.MONTH)) {
			while (calendar.get(Calendar.MONTH) != month) {
				dates.add((calendar.get(Calendar.MONTH)+1) + "-GREY-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR));
				dateWhereClause += "date = '" + getDateInYearMonthForm(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) + "' or ";
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
			}
		}
		// reset month
		month = calendar.get(Calendar.MONTH);
		// Add everything to array until we hit next month
		while (calendar.get(Calendar.MONTH) == month) {
			dates.add((calendar.get(Calendar.MONTH)+1) + "-WHITE-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR));
			dateWhereClause += "date = '" + getDateInYearMonthForm(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) + "' or ";
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		}
		// Finish adding with dates of next month until we get to sunday
		while (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
			dates.add((calendar.get(Calendar.MONTH)+1) + "-GREY-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR));
			dateWhereClause += "date = '" + getDateInYearMonthForm(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) + "' or ";
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		}
		
		if (calendar.get(Calendar.MONTH) != month) {
			calendar.set(Calendar.MONTH, month);
		}
		
		return dates;
		
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getDateInYearMonthForm(int year, int month, int day) {
		month += 1;
		String date = year + "/";
		if (month < 10) { date += "0" + month + "/"; }
		else { date += month + "/"; }
		if (day < 10) { date += "0" + day; }
		else { date += day; }
		
		return date;
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getDateInYearMonthFormCorrectMonth(int year, int month, int day) {
		String date = year + "/";
		if (month < 10) { date += "0" + month + "/"; }
		else { date += month + "/"; }
		if (day < 10) { date += "0" + day; }
		else { date += day; }
		
		return date;
	}

	/**
	 * 
	 * @author sacrilley
	 *
	 */
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private List<String> list;
		private Button gridcell;
		private final Context context;
		private Hashtable<String, Boolean> workoutDateHash;
		private DbAdapter mDbHelper;
		
		/**
		 * 
		 * @param context
		 * @param textViewResourceId
		 */
		public GridCellAdapter(Context context, int textViewResourceId)
		{
			super();
			mDbHelper = new DbAdapter(context);
			this.context = context;
			this.list = getCalendarArray();
			this.workoutDateHash = mDbHelper.getWorkoutDatesForCalendar(dateWhereClause.substring(0, dateWhereClause.length() - 3));
			Log.d("STEVE", "NEW ADAPTER FOR CALENDAR ----------------------------------------------------");

		}

		/**
		 * 
		 */
		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		/**
		 * 
		 */
		public int getCount() {
			return list.size();
		}

		@Override 
		/**
		 * 
		 */
		public long getItemId(int position) {
			return position;
		}
		
		/**
		 * 
		 */
		public void updateList() { 
			this.list = getCalendarArray();
		}

		@Override 
		/**
		 * 
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false); 
			}
			// Get Reference to the day grid cell
			gridcell = (Button) row.findViewById(R.id.btnCalendarDayGridCell);
			gridcell.setOnClickListener(this);
			// Color past and next motnths gray
			// Date String ==> DD-COLOR-MM-YY
			String[] day_color = list.get(position).split("-");
			String month = day_color[0];
			String day = day_color[2];
			String year = day_color[3];
			int m = Integer.parseInt(month);
			int d = Integer.parseInt(day);
			int y = Integer.parseInt(year);
			String hashDate = getDateInYearMonthFormCorrectMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
			if (workoutDateHash.containsKey(hashDate)) {
				// past block
				gridcell.setBackgroundResource(R.drawable.calendar_bg_red);
			}
			gridcell.setTag(formatDate(month, day, year));
			Log.d("STEVE", "GRIDCEL TAG: " + gridcell.getTag());
			gridcell.setText(day);

			Calendar cal = Calendar.getInstance();
			if ((cal.get(Calendar.MONTH) + 1) == m && cal.get(Calendar.DAY_OF_MONTH) == d && cal.get(Calendar.YEAR) == y) {
				gridcell.setTextColor(Color.YELLOW);
			}
			else {
				if (day_color[1].equals("GREY")) {
					gridcell.setTextColor(Color.LTGRAY);
				} else if (day_color[1].equals("WHITE")) {
					gridcell.setTextColor(Color.WHITE);
				}
			}

			return row;
		}
		
		@Override
		/**
		 * 
		 */
		public void onClick(View view) {
			String dateSelected = (String) view.getTag();
			if (returnDate) {
				listenerReturnDate.returnSelectedDate(dateSelected);
			}
			else {
				// Date selected is in form mm/dd/yyyy
				String dateToGo = convertMMDDYYYYtoYYYYMMDD(dateSelected);
				Log.d("Steve", "Date Selected: " + dateSelected);
				listenerGoToDayEvents.goToDayEvents(dateToGo);
			}
		}
	}
	
	/**
	 * 
	 * @author sacrilley
	 *
	 */
	public interface returnDateListener {
		void returnSelectedDate(String dateSelected);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public static void setGetDateListener(returnDateListener listener) {
		CalendarFragment.listenerReturnDate = listener;
	}
	
	/**
	 * 
	 * @author sacrilley
	 *
	 */
	public interface goToDayEventsListener {
		void goToDayEvents(String date);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public static void setGotToDayEventsListener( goToDayEventsListener listener) {
		CalendarFragment.listenerGoToDayEvents = listener;
	}
	
	/**
	 * 
	 * @param month
	 * @param day
	 * @param year
	 * @return
	 */
	public String formatDate(String month, String day, String year) {
		int m = Integer.parseInt(month);
		int d = Integer.parseInt(day);
		int y = Integer.parseInt(year);
		
		String date = "";
		if ( m < 10) { date += "0" + m + "/";}
		else { date += m + "/"; }
		if (d < 10) { date += "0" + d + "/"; }
		else { date += d + "/"; }
		date += y;
		//Log.d("Steve", "Format Date: " + date);
		return date;		
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public String convertMMDDYYYYtoYYYYMMDD(String date) {
		String [] dateSplit = date.split("/");
		String dateOut = dateSplit[2] + "/" + dateSplit[0] + "/" + dateSplit[1];
		return dateOut;
	}
}
