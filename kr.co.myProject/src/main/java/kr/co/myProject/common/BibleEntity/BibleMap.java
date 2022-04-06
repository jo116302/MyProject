package kr.co.myProject.common.BibleEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class BibleMap extends BibleEntity{
	//private final HashMap<String, BibleEntity> bibleMap = new HashMap<String, BibleEntity>();
	private final LinkedHashMap<String, BibleEntity> bibleMap = new LinkedHashMap<String, BibleEntity>();
	
	@Override
	public BibleEntity deepCopy() {
		// TODO Auto-generated method stub
		BibleMap result = new BibleMap();
		for (String key : bibleMap.keySet()) {
			result.put(key, bibleMap.get(key));
		}
		return result;
	}

	public void add(String key, BibleEntity value) {
		bibleMap.put(key, value);
	}
	
	public void put(String key, String value) {
		BiblePrimitive str = new BiblePrimitive(value);
		//javaSourceCode.BiblePrimitive.BiblePrimitive(String String)
		//com.google.gson.JsonPrimitive.JsonPrimitive(Object primitive)
		add(key, new BiblePrimitive(value));
	}
	
	public void put(String key, BibleEntity value) {
		bibleMap.put(key, value);
	}
	
	public void putAll(Map<? extends String, ? extends BibleEntity> entity) {
		bibleMap.putAll(entity);
	}
	
	public BibleEntity get(String key) {
		return bibleMap.get(key);
	}
	
	public Set<String> keySet() {
		return bibleMap.keySet();
	}
	
	public BibleEntity remove(String key) {
		return bibleMap.remove(key);
	}
	
	public boolean containsKey(String key) {
		return bibleMap.containsKey(key);
	}
	
	public boolean containsValue(BibleEntity value) {
		return bibleMap.containsValue(value);
	}
	
	public int size() {
		return bibleMap.size();
	}
}
