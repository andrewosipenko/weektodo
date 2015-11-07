package com.weektodo;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class TodoItemCrossListener extends SimpleOnGestureListener {
	private static final int SWIPE_MAX_OFF_PATH = 30;
	private static final int SWIPE_THRESHOLD_VELOCITY = 10;
	private static final int SWIPE_MIN_DISTANCE = 20;
	private MainActivity activity;
	private TodoItem todoItem;

	public TodoItemCrossListener(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float velocity = Math.abs(velocityX); 
		if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH || velocity < SWIPE_THRESHOLD_VELOCITY)
			return false;
		float dx = e1.getX() - e2.getX();
		if (dx > SWIPE_MIN_DISTANCE){
			activity.unCross(todoItem);
		}
		else if(-dx > SWIPE_MIN_DISTANCE){
			activity.cross(todoItem);
		}
		return false;
	}
	
	public void setTodoItem(TodoItem todoItem) {
		this.todoItem = todoItem;
	}
}
