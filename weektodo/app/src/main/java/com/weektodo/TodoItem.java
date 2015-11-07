package com.weektodo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weektodo.ui.ListItem;

public class TodoItem extends LinearLayout implements ListItem {
	private Long dbId;
	private Long prevId;
	private boolean crossed;
	private static CharacterStyle characterStyle = new StrikethroughSpan();
	private SpannableString content;

	public TodoItem(Context context,
			View.OnTouchListener onTouchListener, View.OnTouchListener crossListener) {
		super(context);
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.item,
				this, true);

		View button = findViewById(R.id.ItemActionsButton);
		button.setOnTouchListener(onTouchListener);
		
		View textView = findViewById(R.id.ItemTextView);
		textView.setOnTouchListener(crossListener);
	}

	@Override
	protected void onFinishInflate() {
	}

	public void setColor(int color) {
		setBackgroundColor(color);
	}

	public CharSequence getDescription() {
		TextView textView = (TextView) findViewById(R.id.ItemTextView);
		return textView.getText();
	}

	public void setDescription(String description) {
		TextView textView = (TextView) findViewById(R.id.ItemTextView);
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
		throw new UnsupportedOperationException();
	}

	public boolean isCrossed() {
		return crossed;
	}

	public void setCrossed(boolean crossed) {
		if(crossed == this.crossed)
			return;
		TextView textView = (TextView) findViewById(R.id.ItemTextView);
		if(crossed){
//			content = new SpannableString(textView.getText());
//			content.setSpan(characterStyle, 0, content.length(), 0);
			textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		else{
//			content.removeSpan(characterStyle);
//			content = null;
			textView.setPaintFlags(textView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
		}
		this.crossed = crossed;
	}
}
