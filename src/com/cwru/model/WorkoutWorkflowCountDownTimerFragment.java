package com.cwru.model;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cwru.R;

public class WorkoutWorkflowCountDownTimerFragment extends Fragment{
	private Chronometer chrTimer;
	private CountDownTimer start1;
	private long startTime;
	private long countDown;
	private Exercise exercise;
	private Context context;
	private Handler handler;
	private long seconds;
	private long minutes;
	private boolean complete;
	private boolean stop;
	private View frameView;
	private long time;
	
	private TextView tvTimer;
	private TextView tvExerciseName;
	private Button btnStartStop;
	
	public WorkoutWorkflowCountDownTimerFragment (Exercise exercise, Context context) {
		this.exercise = exercise;
		this.context = context;
		complete = false;
		stop = true;
		String eType = exercise.getTimeType();
		if (eType.equals("seconds")) {
			seconds = exercise.getTime();
			minutes = seconds / 60;
			seconds = seconds % 60;
			
		} else if (eType.equals("minutes")) { 
			minutes = exercise.getTime();
			seconds = 0;
		} else if (eType.equals("hours")) {
			minutes = exercise.getTime()*60;
			seconds = 0;
		}
		
		time = minutes*60 + seconds;
		time = time * 1000;
		Log.d("STEVE", " TIME: " + time);
		
		start1 = new CountDownTimer(time, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				decreaseTime();
				tvTimer.setText(getFormatedTime());
				Log.d("STEVE", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		};

		
		// 			start1 = new CountDownTimer(100000, 1000) {

	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}	
		View view = (LinearLayout) inflater.inflate(R.layout.workout_workflow_countdown_timer, container, false);
		frameView = view;
		btnStartStop = (Button) view.findViewById(R.id.btnWorkoutWorkflowCountdownTimerStartPause);
		btnStartStop.setOnClickListener(updateTimer);
		tvExerciseName = (TextView) view.findViewById(R.id.tvWorkoutWorkflowCountdownTimerExerciseName);
		tvExerciseName.setText(exercise.getName());
		tvTimer = (TextView) view.findViewById(R.id.tvWorkoutWorkflowCountdownTimerTimer);
		tvTimer.setText(getFormatedTime());
		return view;
	}
	
	public void startCountdown(View view) {
		final long totalSeconds = minutes * 60 + seconds;
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (long i = 0; i <= totalSeconds; i++) {
					if (stop) {
						
					}
//					decreaseTime();
					final long value = i;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							decreaseTime();
							Log.d("STEVE", "VALue = " +(startTime - value));
							tvTimer.setText(getFormatedTime());
						}
					});
				}
			}
		};
		new Thread(runnable).start();
	}	
	
	View.OnClickListener updateTimer = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			/*
			if (stop) {
				stop = false;
				btnStartStop.setText("Pause");
				startCountdown(frameView);
			} else {
				stop = true;
				btnStartStop.setText("Start");
				startCountdown(frameView);
			}
			*/
			if (stop) {
				stop = false;
				btnStartStop.setText("Pause");
				start1.start();				
			}
			else {
				stop = true;
				btnStartStop.setText("Start");
				start1.cancel();
			}

		}
		
	};
	
	private String getFormatedTime() {
		String time = "";
		if (minutes < 10){ time += "0" + minutes + ":"; }
		else { time += minutes + ":";}
		if (seconds < 10) { time += "0" + seconds;}
		else { time += seconds;}
		return time;
	}
	
	private void decreaseTime() {
		// only update if stop is false
		Log.d("Steve", "------------------------------------------------------------------");
		Log.d("Steve", "Minutes: " + minutes + " | Seconds: " + seconds);
		if (!complete) {
			if (seconds == 0) {
				seconds = 60;
				minutes--;
				if (seconds == 0 && minutes == 0) {
					complete = true;
				}
			}
			seconds -= 1;		
			Log.d("Steve", "Minutes: " + minutes + " | Seconds: " + seconds);
		}
	}
}
