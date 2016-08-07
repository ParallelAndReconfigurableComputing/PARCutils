package pu.RedLib;

import java.util.Collection;
import java.util.Iterator;

public class CollectionIntersection<T> implements Reduction<Collection<T>> {

	@Override
	public Collection<T> reduce(Collection<T> first, Collection<T> second) {
		Iterator<T> iterator = first.iterator();
		while(iterator.hasNext()){
			T t = iterator.next();
			if (!second.contains(t))
				iterator.remove();
		}
		return first;
	}

}
