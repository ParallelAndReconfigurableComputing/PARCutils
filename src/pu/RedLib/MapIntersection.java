package pu.RedLib;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapIntersection<K, T> implements Reduction<Map<K, T>> {
	
	protected Reduction<T> reducer;
	
	public MapIntersection(Reduction<T> reducer){
		this.reducer = reducer;
	}

	@Override
	public Map<K, T> reduce(Map<K, T> first, Map<K, T> second) {
		
		Set<K> firstMapKeys	= first.keySet();
		Map<K, T> result = new HashMap<>();
		
		for (K key : firstMapKeys){
			if (second.containsKey(key)){
				
				T secondValue = second.get(key);
				T firstValue = first.get(key);
				
				if(firstValue == null && secondValue == null)
					result.put(key, null);
				else if (firstValue == null && secondValue != null)
					result.put(key, secondValue);
				else if (secondValue == null && firstValue != null)
					result.put(key, firstValue);
				else
					result.put(key, reducer.reduce(firstValue, secondValue));
					
			}
		}		
		return result;		
	}

}
