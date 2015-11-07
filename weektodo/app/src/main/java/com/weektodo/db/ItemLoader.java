package com.weektodo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.weektodo.util.Log;

public class ItemLoader {
	private SQLiteDatabase db;
	
	public ItemLoader(SQLiteDatabase db) {
		this.db = db;
	}

	public <T extends ChainItem> List<T> loadList(String query, ChainItemBuilder<T> chainItemBuilder){
		Cursor c = db.query("TodoItem", new String[]{"id", "prevId", "description", "type", "crossed"}, query, null, null, null, null);
		int itemCount = c.getCount();
		Log.d("db", "Loaded " + itemCount + " items");
		c.moveToFirst();
		List<T> res = new ArrayList<T>(itemCount);
		for(int i =0; i < itemCount; i++){
			T databaseItem = chainItemBuilder.build(c);
			res.add(databaseItem);
			c.moveToNext();
		}
		c.close();
		return res;
	}
	
	public <T extends ChainItem> ChainItem load(String query, ChainItemBuilder<T> chainItemBuilder){
		List<T> res = loadList(query, chainItemBuilder);
		if(res.isEmpty())
			return null;
		return res.iterator().next();
	}
	
	public <T extends ChainItem> Map<Long, T> loadMap(String query, ChainItemBuilder<T> chainItemBuilder){
		List<T> chainItems = loadList(query, chainItemBuilder);
		Map<Long, T> prevItemMap = new HashMap<Long, T>();
		for (T chainItem : chainItems) {
			prevItemMap.put(chainItem.getPrevId(), chainItem);			
		}
		return prevItemMap;
	}
	
	public void updatePrevId(ChainItem databaseItem, Long prevId){
		updatePrevId("id=" + databaseItem.getDbId(), prevId);
	}
	public void updatePrevId(String query, Long prevId){
		Log.d("db", "Updating prevId, query: " + query + ", prevId=" + prevId);
		ContentValues initialValues = new ContentValues();
		initialValues.put("prevId", prevId);
		db.update("TodoItem", initialValues, query, null);
	}
	
	public <T extends ChainItem> ChainItem getItemByPrevId(Long prevId, ChainItemBuilder<T> chainItemBuilder) {
		return load("prevId=" + prevId, chainItemBuilder);
	}
}
