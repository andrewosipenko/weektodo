package com.weektodo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class GestureTransparentScrollView extends ScrollView {

	public GestureTransparentScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public GestureTransparentScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GestureTransparentScrollView(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		View child = getChildAt(0);
		ViewGroup viewGroup = (ViewGroup) child;
		if(viewGroup.getChildCount() > 3){
			TodoItem todoItem = findTodoItem(viewGroup);
			child = todoItem.getChildAt(0);
			viewGroup = (ViewGroup) child;
			child = viewGroup.getChildAt(0);
			viewGroup = (ViewGroup) child;
			child = viewGroup.getChildAt(1);
			int left = child.getLeft();
			int right = child.getRight();
			int x = (int) ev.getX();
			if(x >= left && x <= right)
				return false;
		}
        return super.onInterceptTouchEvent(ev); 
	}
	private TodoItem findTodoItem(ViewGroup viewGroup){
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View child = viewGroup.getChildAt(i);
			if(child instanceof TodoItem)
				return (TodoItem) child;
		}
		throw new IllegalArgumentException("Can't find todo item");
	}
}
