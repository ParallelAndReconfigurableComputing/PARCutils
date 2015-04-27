package pi.reductions;

import java.util.Set;

public class SetIntersection<T> implements Reduction<Set<T>> {

	@Override
	public Set<T> reduce(Set<T> first, Set<T> second) {
		for (T t : first){
			if (!second.contains(t))
				first.remove(t);
		}
		return first;
	}

}
