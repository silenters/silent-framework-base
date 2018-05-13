package com.silent.framework.base.collection;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 可缓存的Map，大小固定，当超出给定大小时，会删除旧的数据，保留信息的数据
 * @author TanJin
 * @date 2018年1月29日
 */
public class QueueMap<Key, Value> extends AbstractMap<Key, Value> {

	private int maxCount = 100;
	private Queue<Entry> queue = new LinkedList<Entry>();
	
	public QueueMap() {}
	
	/**
	 * 可在实例化时设置Map可保存数据量大小，若超出该大小之后，则Map会自动删除老数据以便腾出空间保存新数据
	 * @param size
	 */
	public QueueMap(int size) {
		maxCount = size;
	}

	/**
	 * 添加数据，并对已保存数据量进行检查，若超过则删除队列中数据，再保存新数据
	 */
	@Override
	public Value put(Key key, Value value) {
		while(queue.size() >= maxCount){
			queue.remove();
		}
		queue.add(new Entry(key, value));
		return value;
	}
	
	/**
	 * 获取指定Key数据
	 */
	@Override
	public Value get(Object key){
		for(Iterator<Entry> iter = queue.iterator();iter.hasNext();){
			Entry type = iter.next();
			if(type.key.equals(key)){
				queue.remove(type);
				queue.add(type);
				return type.value;
			}
		}
		return null;
	}
	
	/**
	 * 将Map中数据以Set列表的形式返回
	 */
	@Override
	public Set<Map.Entry<Key, Value>> entrySet() {
		Set<Map.Entry<Key, Value>> set = new HashSet<Map.Entry<Key, Value>>();
		set.addAll(queue);
		return set;
	}

	/**
	 * 清空数据
	 */
	@Override
	public void clear() {
		queue.clear();
	}
	
	/**
	 * 获取Map中Key值Set列表
	 */
	@Override
	public Set<Key> keySet() {
		Set<Key> set = new HashSet<Key>();
		for(Entry e : queue){
			set.add(e.getKey());
		}
		return set;
	}
	
	/**
	 * 删除指定数据
	 */
	@Override
	public Value remove(Object obj) {
		for(Entry e : queue){
			if(e.getKey().equals(obj)){
				queue.remove(e);
				return e.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Map格式key-value
	 * @author TanJin
	 * @date 2018年1月29日
	 */
	private class Entry implements Map.Entry<Key, Value> {
		private Key key;
		private Value value;

		public Entry(Key key, Value value){
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key +"="+ value;
		}

		public Key getKey() {
			return key;
		}

		public Value getValue() {
			return value;
		}

		public Value setValue(Value value) {
			return this.value = value;
		}
	}

}
