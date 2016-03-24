package pu.RedLib;

import java.util.Map;
import java.util.Set;

public class MapDifference<K, T> implements Reduction<Map<K, T>> {

	@Override
	public Map<K, T> reduce(Map<K, T> first, Map<K, T> second) {
		Set<K> firstMapKeys = first.keySet();
		for (K key : firstMapKeys){
			if (second.containsKey(key))
				first.remove(key);
		}
		return first;
	}


}
