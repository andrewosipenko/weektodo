package com.weektodo.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.weektodo.util.Log;

import com.weektodo.TodoItem;
import com.weektodo.ui.TodoItemBuilder;

public class ItemDao {
	private SQLiteDatabase db;
	private TodoItemBuilder todoItemBuilder;
	private ChainItemBuilder<UpdatableDatabaseItem> chainItemBuilder;
	private ItemLoader itemLoader;
	private CrossedItemRemover crossedItemRemover;

	public ItemDao(Context context, TodoItemBuilder todoItemBuilder, ChainItemBuilder<UpdatableDatabaseItem> chainItemBuilder) {
		super();
		db = new OpenHelper(context).getWritableDatabase();
		this.todoItemBuilder = todoItemBuilder ;
		this.chainItemBuilder =chainItemBuilder ;
		itemLoader = new ItemLoader(db);
		crossedItemRemover = new CrossedItemRemover(chainItemBuilder, db, itemLoader);
	}
	
	public List<DatabaseItem> getItems(){
		Map<Long, UpdatableDatabaseItem> prevItemMap = itemLoader.loadMap(null, chainItemBuilder);
		return (List)sortItems(prevItemMap);
	}
	
	private <T extends ChainItem> List <T> sortItems(Map<Long, T> prevItemMap){
		Long prevId = null;
		List<T> res = new ArrayList<T>();
		Log.i("db", "Sorting items");
		while(true){
			T loadedItem = prevItemMap.get(prevId);
			if(loadedItem == null)
				break;
			res.add(loadedItem);
			Log.i("db", "Sorted TodoItem id=" + loadedItem.getDbId() + ", prevId=" + loadedItem.getPrevId());
			prevId = loadedItem.getDbId();
		}
		return res;
	}
	
	private UpdatableDatabaseItem buildDatabaseItem(Cursor c){
		return chainItemBuilder.build(c);
	}
	
	public UpdatableDatabaseItem addTodoItem(Long prevId, String text){
		Long id = insertTodoItemIntoDb(prevId, text);
		UpdatableDatabaseItem res = todoItemBuilder.buildChainItemDataTarget();
		res.setDbId(id);
		res.setDescription(text);
		res.setPrevId(prevId);
		return res;
	}
	
	public void moveTodoItem(UpdatableDatabaseItem databaseItem, Long prevId){
		Log.i("db", "Move TodoItem id=" + databaseItem.getDbId() + ", prevId=" + databaseItem.getPrevId() + ", to " + prevId);
//		printDatabaseItems();
		if(prevId.equals(databaseItem.getPrevId()) || prevId.equals(databaseItem.getDbId())){
			return;
		}
		// TODO add transaction
		Log.i("db", "TodoItem id=" + databaseItem.getDbId() + ", prevId=" + databaseItem.getPrevId());
		UpdatableChainItem nextItem = (UpdatableChainItem) itemLoader.getItemByPrevId(databaseItem.getDbId(), chainItemBuilder);
		if(nextItem != null){
			updatePrevId(nextItem, databaseItem.getPrevId());
		}
		nextItem = (UpdatableChainItem) itemLoader.getItemByPrevId(prevId, chainItemBuilder);
		if(nextItem != null){
			updatePrevId(nextItem, databaseItem.getDbId());
		}
		updatePrevId(databaseItem, prevId);
//		printDatabaseItems();
	}
	

	private Long insertTodoItemIntoDb(Long prevId, String text){
		ContentValues initialValues = new ContentValues();
		initialValues.put("description", text);
		if (prevId != null) {
			initialValues.put("prevId", prevId);
		}
		initialValues.put("type", DelegatingChainItemBuilder.TYPE_ITEM);
		return db.insert("TodoItem", null, initialValues);
	}
	private void updatePrevId(UpdatableChainItem databaseItem, Long prevId){
		itemLoader.updatePrevId(databaseItem, prevId);
		databaseItem.setPrevId(prevId);
	}
	
	
	private void printDatabaseItems(){
		Cursor c = db.query("TodoItem", new String[]{"id", "prevId", "description", "type"}, null, null, null, null, null);
		int itemCount = c.getCount();
		Log.w("db", "Loaded " + itemCount + " items");
		c.moveToFirst();
		for(int i =0; i < itemCount; i++){
			DatabaseItem databaseItem = buildDatabaseItem(c);
			Log.d("db", "Loaded TodoItem id=" + databaseItem.getDbId() + ", prevId=" + databaseItem.getPrevId());
			c.moveToNext();
		}
		c.close();		
	}
	public void close(){
		Log.d("db", "Closing");
		db.close();
	}
	public void cross(TodoItem todoItem){
		updateCrossedState(todoItem, true);
	}
	
	public void unCross(TodoItem todoItem){
		updateCrossedState(todoItem, false);
	}
	
	private void updateCrossedState(UpdatableDatabaseItem databaseItem, boolean crossed){
		ContentValues initialValues = new ContentValues();
		databaseItem.setCrossed(crossed);
		initialValues.put("crossed", databaseItem.isCrossed()? 1 : null);
		db.update("TodoItem", initialValues, "id=" + databaseItem.getDbId(), null);
	}
	
	public void removeCrossedItems(){
		crossedItemRemover.removeCrossedItems();
	}
	
}
