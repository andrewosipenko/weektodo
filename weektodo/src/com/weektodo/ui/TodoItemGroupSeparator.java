package com.weektodo.ui;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weektodo.R;

public class TodoItemGroupSeparator extends LinearLayout implements ListItem {
	private Long dbId;
	private Long prevId;
	private int color;

	public TodoItemGroupSeparator(Context context) {
		super(context);
		((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.separator, this, true);
	}

	public CharSequence getDescription() {
		TextView textView = (TextView) findViewById(R.id.groupId);
		return textView.getText();
	}

	public void setDescription(String description) {
		TextView textView = (TextView) findViewById(R.id.groupId);
		textView.setText(description);
	}

	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		setBackgroundColor(color);
	}

	public void setCrossed(boolean crossed) {
	}

	public boolean isCrossed() {
		return false;
	}

}
