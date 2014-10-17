package pi.reductions;

import java.util.HashSet;
import java.util.Map;
import pi.operands.*;

public class MapSubtraction<K, T> implements Reduction<Map<K, Operand<T>>> {

	@Override
	public Map<K, Operand<T>> reduce(Map<K, Operand<T>> m1,Map<K, Operand<T>> m2) {
		HashSet<K> keySetMap2 = (HashSet<K>) m2.keySet();
		for (K key : keySetMap2){
			if (m1.containsKey(key))
				m1.remove(key);
		}
		return m1;
	}

}
