package pi.RedLib;

import java.util.Collection;

public class CollectionIntersection<T> implements Reduction<Collection<T>> {

	@Override
	public Collection<T> reduce(Collection<T> first, Collection<T> second) {
		for (T t : first){
			if (!second.contains(t))
				first.remove(t);
		}
		return first;
	}

}
