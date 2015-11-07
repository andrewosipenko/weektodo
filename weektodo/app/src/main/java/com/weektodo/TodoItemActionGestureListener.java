package com.weektodo;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class TodoItemActionGestureListener extends SimpleOnGestureListener {
	private static final int SHORT_SWIPE_MIN_DISTANCE = 20;
	private static final int LONG_SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_MAX_OFF_PATH = 20;
	private static final int SHORT_SWIPE_THRESHOLD_VELOCITY = 10;
	private static final int LONG_SWIPE_THRESHOLD_VELOCITY = 1500;
	private MainActivity activity;
	private TodoItem todoItem;

	public TodoItemActionGestureListener(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
			return false;
		// right to left swipe
		float dy = e1.getY() - e2.getY();
		float velocity = Math.abs(velocityY); 
		if (dy > SHORT_SWIPE_MIN_DISTANCE
				&& velocity > SHORT_SWIPE_THRESHOLD_VELOCITY) {
			if (dy > LONG_SWIPE_MIN_DISTANCE && velocity > LONG_SWIPE_THRESHOLD_VELOCITY) {
				activity.planALotEarlier(todoItem);
			} else {
				activity.planEarlier(todoItem);
			}
		} else if (-dy > SHORT_SWIPE_MIN_DISTANCE
				&& velocity > SHORT_SWIPE_THRESHOLD_VELOCITY) {
			if (-dy > LONG_SWIPE_MIN_DISTANCE && velocity > LONG_SWIPE_THRESHOLD_VELOCITY) {
				activity.planALotLater(todoItem);
			} else {
				activity.planLater(todoItem);
			}
		}
		return false;
	}

	public void setTodoItem(TodoItem todoItem) {
		this.todoItem = todoItem;
	}
}
