package pi.RedLib;

import java.util.Map;
import java.util.Set;

/**
 * This class reduces two <code>Map</code> objects into one by merging them into
 * the first <code>Map</code> object. The process checks every key in the
 * second <code>Map</code> object. For the keys that exist in the first
 * <code>Map</code> object as well, the values will be merged according to 
 * programmer's specifications. If the first <code>Map</code> object does 
 * not contain the key already, then the key and its corresponding value will
 * be added to the first <code>Map</code> object.
 * 
 *  @author Mostafa Mehrabi	
 *  @since  14/10/2014
 * */

public class MapUnion<K, T> implements Reduction<Map<K, T>> {
	
	protected Reduction<T> reducer;
	
	public MapUnion(Reduction<T> reducer){
		this.reducer = reducer;
	}

	@Override
	public Map<K, T> reduce(Map<K, T> first, Map<K, T> second) {
		Set<K> secondMapKeys = second.keySet();
		for (K key : secondMapKeys){
			T secondMapValue = second.get(key);
			if (first.containsKey(key)){
				if (secondMapValue != null){
					T firstMapValue = first.get(key);
					if (firstMapValue != null){
						firstMapValue = reducer.reduce(firstMapValue, secondMapValue);
						first.put(key, firstMapValue);
					}//if firstMapValue is null, then replace it with secondMapValue
					else{
					first.put(key, secondMapValue);
					}
				}
				
				//if secondMapValue is null, don't do anything...
				
			}else{//if first doesn't contain the key then add it.
				first.put(key, secondMapValue);
			}
		}
		return first;
	}

}
