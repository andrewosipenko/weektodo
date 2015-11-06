package com.weektodo.ui;

import com.weektodo.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.weektodo.MainActivity;
import com.weektodo.TodoItem;
import com.weektodo.TodoItemActionGestureListener;
import com.weektodo.TodoItemCrossListener;
import com.weektodo.db.AbstractDatabaseItemBuilder;

public class TodoItemBuilder extends AbstractDatabaseItemBuilder {
	private MainActivity context;

	public TodoItemBuilder(MainActivity context) {
		this.context = context;
	}

	@Override
	public TodoItem buildChainItemDataTarget() {
		TodoItemActionGestureListener gestureListener = new TodoItemActionGestureListener(
				context);
		final GestureDetector gestureDetector = new GestureDetector(
				gestureListener);
		View.OnTouchListener onTouchListener = new View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionEvent) {
				gestureDetector.onTouchEvent(motionEvent);
				return true;
			}
		};
		
		TodoItemCrossListener crossListener = new TodoItemCrossListener(
				context);
		final GestureDetector crossDetector = new GestureDetector(
				crossListener);
		View.OnTouchListener onTouchCrossListener = new View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionEvent) {
				crossDetector.onTouchEvent(motionEvent);
				return true;
			}
		};
		
		
		TodoItem res = new TodoItem(context, onTouchListener, onTouchCrossListener);
		gestureListener.setTodoItem(res);
		crossListener.setTodoItem(res);
		
		Log.d("ui", "Building TodoItem");
		return res;
	}



}
