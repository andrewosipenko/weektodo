package com.weektodo.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;


public class CrossedItemRemoverTest extends TestCase {
	private CrossedItemRemover crossedItemRemover; 
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		crossedItemRemover = new CrossedItemRemover(null, null, null);
	}

	@SuppressWarnings("unchecked")
	public void testBuildEmptyChainMap(){
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(Collections.EMPTY_LIST);
		assertTrue(res.isEmpty());
	}
	
	public void testBuildSingleItemChainMap(){
		List<ChainItem> chainItems = new ArrayList<ChainItem>();
		addChainItem(chainItems, 2, 1);
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(chainItems);
		assertEquals(1, res.size());
		assertChainItem(res, 2, 2, 1);
	}
	
	public void testBuildTwoItemChainMap(){
		List<ChainItem> chainItems = new ArrayList<ChainItem>();
		addChainItem(chainItems, 2, 1);
		addChainItem(chainItems, 3, 2);
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(chainItems);
		assertEquals(1, res.size());
		assertChainItem(res, 3, 2, 1);
	}

	public void testBuildTwoItemChainMapReverse(){
		List<ChainItem> chainItems = new ArrayList<ChainItem>();
		addChainItem(chainItems, 3, 2);
		addChainItem(chainItems, 2, 1);
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(chainItems);
		assertEquals(1, res.size());
		assertChainItem(res, 3, 2, 1);
	}
	
	public void testBuildTwoItemChainMapTwoPartitions(){
		List<ChainItem> chainItems = new ArrayList<ChainItem>();
		addChainItem(chainItems, 2, 1);
		addChainItem(chainItems, 4, 3);
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(chainItems);
		assertEquals(2, res.size());
		assertChainItem(res, 2, 2, 1);
		assertChainItem(res, 4, 4, 3);
	}

	public void testBuildThreeItemChainMap(){
		List<ChainItem> chainItems = new ArrayList<ChainItem>();
		addChainItem(chainItems, 2, 1);
		addChainItem(chainItems, 3, 2);
		addChainItem(chainItems, 4, 3);
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(chainItems);
		assertEquals(1, res.size());
		assertChainItem(res, 4, 2, 1);
	}
	
	public void testBuildThreeItemChainMapTwoPartitions(){
		List<ChainItem> chainItems = new ArrayList<ChainItem>();
		addChainItem(chainItems, 2, 1);
		addChainItem(chainItems, 5, 4);
		addChainItem(chainItems, 3, 2);
		Map<Long, ChainItem> res = crossedItemRemover.buildChainMap(chainItems);
		assertEquals(2, res.size());
		assertChainItem(res, 3, 2, 1);
		assertChainItem(res, 5, 5, 4);
	}
	
	private void addChainItem(List<ChainItem> chainItems, long dbId, long prevId){
		DefaultChainItem chainItem = new DefaultChainItem();
		chainItem.setDbId(dbId);
		chainItem.setPrevId(prevId);
		chainItems.add(chainItem);
	}
	private void assertChainItem(Map<Long, ChainItem> chainItemMap, long key, long dbId, long prevId){
		ChainItem chainItem = chainItemMap.get(key);
		assertEquals((Long)dbId, chainItem.getDbId());
		assertEquals((Long)prevId, chainItem.getPrevId());
	}
}
