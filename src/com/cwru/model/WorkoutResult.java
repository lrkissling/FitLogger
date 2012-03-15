package com.cwru.model;

public class WorkoutResult {
	public static final int SET_BASED_EXERCISE = 0;
	public static final int DISTANCE_BASED_EXERCISE = 1;
	public static final int TIME_BASED_EXERCISE = 2;
	public static final int INTERVAL_BASED_EXERCISE = 3;
	
	private int id;
	private int workoutId;
	private int exerciseId;
	private String date;
	private int mode;
	
	public WorkoutResult (int workoutId, int exerciseId) { 
		this.workoutId = workoutId;
		this.exerciseId = exerciseId;
	}

	
	public int getId() { return id; }
	public int getWorkoutId() { return workoutId; }
	public int getExerciseId() { return exerciseId; }
	public String getDate() { return date; }
	
	public void setWorkoutId(int workoutId) { this.workoutId = workoutId;  }
	public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }
	public void setDate(String date) { this.date = date; }
	public void setMode(int mode) { this.mode = mode; }
	
}