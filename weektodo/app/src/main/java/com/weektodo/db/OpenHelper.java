package com.weektodo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.weektodo.util.Log;

public class OpenHelper extends SQLiteOpenHelper {
	public static final String WEEK_GROUP_ID = "week";
	public static final String MONTH_GROUP_ID = "month";
	public static final String YEAR_GROUP_ID = "year";

	public OpenHelper(Context context) {
		super(context, "TodoData.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String CREATE_TABLE_TODO_ITEM = "CREATE TABLE TodoItem ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, prevId INTEGER, "
				+ "description TEXT, type TEXT, crossed INTEGER);";
		db.execSQL(CREATE_TABLE_TODO_ITEM);
		Long id = insertGroupSeparator(db, null, WEEK_GROUP_ID);
		id = insertGroupSeparator(db, id, MONTH_GROUP_ID);
		id = insertGroupSeparator(db, id, YEAR_GROUP_ID);
	}

	private Long insertGroupSeparator(SQLiteDatabase db, Long prevId,
			String text) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("description", text);
		if (prevId != null) {
			initialValues.put("prevId", prevId);
		}
		initialValues.put("type", DelegatingChainItemBuilder.TYPE_SEPARATOR);
		Log.i("db", "Inserting GroupSeparator with prevId=" + prevId);
		return db.insert("TodoItem", null, initialValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
