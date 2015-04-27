package pi.reductions;

import java.util.Set;

public class SetDifference<T> implements Reduction<Set<T>> {
	
	@Override
	public Set<T> reduce(Set<T> first, Set<T> second) {
		for (T t : second){
			if (first.contains(t))
				//because we don't have duplicates in set, removing the 
				//first occurrence is enough
				first.remove(t);
		}
		return first;
	}
}
