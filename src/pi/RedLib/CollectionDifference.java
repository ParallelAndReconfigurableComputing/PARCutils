package pi.reductions;

import java.util.Collection;

public class CollectionDifference<T> implements Reduction<Collection<T>> {

	@Override
	public Collection<T> reduce(Collection<T> first, Collection<T> second) {
		for (T t : second){
			while (first.contains(t)) //because collections allow duplicates
				first.remove(t);
		}
		return first;
	}
 	
}
