package pu.RedLib;

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
		
		for (K key : firstMapKeys){
			
			if (second.containsKey(key)){
				
				T secondMapValue = second.get(key);
				if (secondMapValue != null){
					
					T firstMapValue = first.get(key);
					if (firstMapKeys != null){
						
						firstMapValue = reducer.reduce(firstMapValue, secondMapValue);
						first.put(key, firstMapValue);
						
					}else{ //if firstMapValue is null, then simply put secondMapValue 
						first.put(key, secondMapValue);
					}					
				}
				
				//if secondMapValue is null, then don't do anything
								
			} else{//if second map does not contain the same key...
				first.remove(key);
			}
		}
		return first;
	}

}
