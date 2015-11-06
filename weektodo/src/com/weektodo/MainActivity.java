package com.weektodo;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import com.weektodo.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.weektodo.db.DatabaseItem;
import com.weektodo.db.DelegatingChainItemBuilder;
import com.weektodo.db.ItemDao;
import com.weektodo.db.OpenHelper;
import com.weektodo.ui.ListItem;
import com.weektodo.ui.TodoItemBuilder;
import com.weektodo.ui.TodoItemGroupSeparator;
import com.weektodo.ui.TodoItemGroupSeparatorBuilder;

public class MainActivity extends Activity {
	private static final int weekColor = 0xFFFF8080;
	private static final int monthColor = 0xFFEFFF64;
	private static final int yearColor = 0xFFA9FF80;

	private ItemDao itemDao;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i("ui", "Creating layout");
		final EditText editText = (EditText) findViewById(R.id.EditText);
		Button addButton = (Button) findViewById(R.id.AddButton);

		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addTodoItem(editText.getText().toString());
				editText.setText("");
				ScrollView scrollView = (ScrollView) findViewById(R.id.todoListScrollView);
				scrollView.fullScroll(View.FOCUS_DOWN);
			}
		});
		findViewById(R.id.todoListScrollView).setOnTouchListener(
				new View.OnTouchListener() {

					public boolean onTouch(View view, MotionEvent motionEvent) {
						return false;
					}
				});

//		clear(); COMMENT next line if uncomment this
		init();
		
		if (savedInstanceState != null) {
			String strValue = savedInstanceState.getString("editedText");
			if (strValue != null) {
				EditText oControl = (EditText) findViewById(R.id.EditText);
				oControl.setText(strValue);
			}
		}
	}
	
	private void init(){
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		todoList.removeAllViews();
		initDatabase();
		loadDataFromDatabase();
	}

	private void addTodoItem(String text) {
		if(text == null || text.isEmpty())
			return;
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		DatabaseItem databaseItem = (DatabaseItem) todoList.getChildAt(todoList.getChildCount() - 1);
		addTodoItem(null, databaseItem.getDbId(),
				text);
	}

	private void addTodoItem(Long id, Long prevId, String text) {
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		TodoItem itemView = (TodoItem) itemDao.addTodoItem(prevId, text);
		itemView.setColor(yearColor);
		todoList.addView(itemView);
	}
	
	private TodoItemGroupSeparator getPreviousSeparator(int index, int skipCount) {
		return (TodoItemGroupSeparator) getPreviousItem(index, TodoItemGroupSeparator.class, skipCount);
	}

	private DatabaseItem getPreviousItem(int index, Class<?> clazz, int skipCount) {
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		while (index >= 0) {
			View view = todoList.getChildAt(index--);
			if (clazz.isAssignableFrom(view.getClass())) {
				if(skipCount-- == 0){
					DatabaseItem res = (DatabaseItem) view; 
					Log.i("ui", "Get previous Item id=" + res.getDbId());
					return res;
				}
			}
		}
		return null;
	}
	
	private DatabaseItem getNextItem(int index, Class<?> clazz, int skipCount) {
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int max = todoList.getChildCount();
		while (index < max) {
			View view = todoList.getChildAt(index++);
			if (clazz.isAssignableFrom(view.getClass())) {
				if(skipCount-- == 0){
					DatabaseItem res = (DatabaseItem) view;
					Log.i("ui", "Get next Item id=" + res.getDbId());
					return res;
				}
			}
		}
		return null;
	}

	private void initDatabase() {
		TodoItemBuilder todoItemBuilder = new TodoItemBuilder(this);
		itemDao = new ItemDao(this, todoItemBuilder, new DelegatingChainItemBuilder(todoItemBuilder, new TodoItemGroupSeparatorBuilder(this)));
	}

	private void loadDataFromDatabase() {
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int color = -1;
		int i = 0;
		List<DatabaseItem> databaseItems = itemDao.getItems();
//		Collections.reverse(databaseItems);
		for (DatabaseItem databaseItem : databaseItems) {
//			if(i++ < 2)
//				continue;
//			if(i++ > 6)
//				break;
//			if(i++ == 7)
//				continue;
			if (databaseItem instanceof TodoItemGroupSeparator) {
				TodoItemGroupSeparator separator = (TodoItemGroupSeparator) databaseItem;
				if (OpenHelper.WEEK_GROUP_ID.equals(separator.getDescription())) {
					color = weekColor;
				} else if (OpenHelper.MONTH_GROUP_ID.equals(separator
						.getDescription())) {
					color = monthColor;
				} else if (OpenHelper.YEAR_GROUP_ID.equals(separator
						.getDescription())) {
					color = yearColor;
				}
			}
			ListItem view = (ListItem) databaseItem;
			view.setColor(color);
			todoList.addView((View) databaseItem);
		}
	}

	public void planEarlier(TodoItem todoItem) {
		Log.i("ui", "Plan earlier TodoItem id=" + todoItem.getDbId());
		Toast.makeText(this, "Plan earlier", Toast.LENGTH_SHORT).show();
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int index = todoList.indexOfChild(todoItem);
		TodoItemGroupSeparator separator = getPreviousSeparator(index, 1);
		if(separator != null){
			moveTodoItem(todoItem, separator);
		}
	}

	public void planALotEarlier(TodoItem todoItem) {
		Toast.makeText(this, "Plan a lot earlier", Toast.LENGTH_SHORT).show();
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int index = todoList.indexOfChild(todoItem);
		TodoItemGroupSeparator separator = getPreviousSeparator(index, 2);
		if(separator != null){
			moveTodoItem(todoItem, separator);
		}
	}

	public void planLater(TodoItem todoItem) {
		Log.i("ui", "Plan later TodoItem id=" + todoItem.getDbId());
		Toast.makeText(this, "Plan later", Toast.LENGTH_SHORT).show();
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int index = todoList.indexOfChild(todoItem);
		TodoItemGroupSeparator separator = (TodoItemGroupSeparator) getNextItem(index, TodoItemGroupSeparator.class, 0);
		if(separator != null){
			moveTodoItem(todoItem, separator);
		}
	}

	public void planALotLater(TodoItem todoItem) {
		Toast.makeText(this, "Plan a lot later", Toast.LENGTH_SHORT).show();
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int index = todoList.indexOfChild(todoItem);
		TodoItemGroupSeparator separator = (TodoItemGroupSeparator) getNextItem(index, TodoItemGroupSeparator.class, 1);
		if(separator != null){
			moveTodoItem(todoItem, separator);
		}
	}

	private void moveTodoItem(TodoItem todoItem, DatabaseItem prevItem) {
		// if (oldPos < monthTodoItemStartIndex
		// && newPos >= monthTodoItemStartIndex) {
		// monthTodoItemStartIndex--;
		// }
		// if (oldPos < yearTodoItemStartIndex && newPos >=
		// yearTodoItemStartIndex) {
		// yearTodoItemStartIndex--;
		// }
		// if (oldPos >= monthTodoItemStartIndex
		// && newPos < monthTodoItemStartIndex) {
		// monthTodoItemStartIndex++;
		// }
		// if (oldPos >= yearTodoItemStartIndex && newPos <
		// yearTodoItemStartIndex) {
		// yearTodoItemStartIndex++;
		// }
		ensurePrevIdIsCorrect(todoItem);
		moveChild(todoItem, prevItem);
//		updateColor(todoItem, newPos);
	}
	
	private void ensurePrevIdIsCorrect(TodoItem todoItem){
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		int index = todoList.indexOfChild(todoItem);
		DatabaseItem databaseItem = (DatabaseItem) todoList.getChildAt(index - 1);
		todoItem.setPrevId(databaseItem.getDbId());		
	}

	private void moveChild(ListItem child, DatabaseItem prevItem) {
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		Long prevId = null;
		if(prevItem != null){
			prevId = prevItem.getDbId();
		}
		itemDao.moveTodoItem(child, prevId);
		todoList.removeView((View) child);
//		if (newPos > oldPos)
//			newPos--;
		int index = todoList.indexOfChild((View) prevItem);
		todoList.addView((View) child, index + 1);
		ListItem separator = getPreviousSeparator(index + 1, 0);
		child.setColor(separator.getColor());
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("db", "Closing itemDao");
		itemDao.close();
	}
	
	protected void onResume() {
		super.onResume();
		Log.i("db", "Closing itemDao");
		init();
	}	

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Store UI state to the savedInstanceState.
		// This bundle will be passed to onCreate on next call.
		Log.i("db", "Saving instance state");
		EditText txtName = (EditText) findViewById(R.id.EditText);
		String strName = txtName.getText().toString();

		savedInstanceState.putString("editedText", strName);

		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.clearCrossedItems:
				ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
				todoList.removeAllViews();
				itemDao.removeCrossedItems();
				loadDataFromDatabase();
				return true;
			case R.id.clear:
				clear();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	private void clear(){
		ViewGroup todoList = (ViewGroup) findViewById(R.id.todoList);
		todoList.removeAllViews();
		if(itemDao != null){
			itemDao.close();
		}
		File file = getApplicationContext().getDatabasePath("TodoData.db");
		file.delete();
		init();
	}
	
	public void cross(TodoItem todoItem){
		Toast.makeText(this, "cross", Toast.LENGTH_SHORT).show();
		itemDao.cross(todoItem);
	}
	public void unCross(TodoItem todoItem){
		Toast.makeText(this, "uncross", Toast.LENGTH_SHORT).show();
		itemDao.unCross(todoItem);
	}
}