package pi.reductions;

import java.util.HashSet;
import java.util.Map;

import pi.operands.*;

public class MapIntersection<K, T> implements Reduction<Map<K, Operand<T>>> {

	@Override
	public Map<K, Operand<T>> reduce(Map<K, Operand<T>> m1, Map<K, Operand<T>> m2) {
		HashSet<K> keySetMap2 = (HashSet<K>) m2.keySet(); 
		
		for (K key : keySetMap2){
			if (m1.containsKey(key)){
				Operand<T> value1 = m1.get(key);
				if (value1 != null){
					Operand<T> value2 = m2.get(key);
					if (value2 != null)
						value1.operateOn(value2);
					m2.put(key, value1);
				}
				//In the cases where value1 is null, don't do anything
			}
			else if (!m1.containsKey(key)){
				m2.remove(key);
			}
		}	
		return m2;
	}

}
