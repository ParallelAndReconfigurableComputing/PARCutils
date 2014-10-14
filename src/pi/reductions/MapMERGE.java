package pi.reductions;

import java.util.HashSet;
import java.util.Map;

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
public class MapMERGE<K, T> implements MapReduction<K, Operable<T>> {

	@Override
	public Map<K, Operable<T>> reduce(Map<K, Operable<T>> m1, Map<K, Operable<T>> m2) {
		HashSet<K> keySetMap2 = (HashSet<K>) m2.keySet();
	
		for (K key : keySetMap2){
			
			Operable<T> value2 = m2.get(key); 
		    
			if (m1.containsKey(key)){
				Operable<T> value1 = m1.get(key);
				if (value2!=null){
					if (value1!=null)
						value1.operateOn(value2);
					else 
						m1.put(key, value2);
				}
				/*For the cases where value2 is null, don't do anything*/				
			}
			
			else if (!m1.containsKey(key))
				m1.put(key, value2);
		}
		return m1;
	}

}
