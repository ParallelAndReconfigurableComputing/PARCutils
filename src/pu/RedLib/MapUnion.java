package pu.RedLib;

import java.util.Map;
import java.util.Map.Entry;

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
		for(Entry<K, T> entry : second.entrySet()){
			K secondMapKey = entry.getKey();
			T secondMapValue = entry.getValue();
			if(first.containsKey(secondMapKey)){
				if(secondMapValue != null){
					T firstMapValue = first.get(secondMapKey);
					if (firstMapValue != null){
						first.put(secondMapKey, reducer.reduce(firstMapValue, secondMapValue));
					}else{
						first.put(secondMapKey, secondMapValue);
					}
				}
			}
			else{
				first.put(secondMapKey, secondMapValue);
			}
		}
		return first;
	}

}
