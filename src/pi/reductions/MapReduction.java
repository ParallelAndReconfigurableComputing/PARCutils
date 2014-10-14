package pi.reductions;

import java.util.Map;

public interface MapReduction<K, V> {
	Map<K, V> reduce (Map<K, V> m1, Map<K, V> m2);
}
