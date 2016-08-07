package pu.RedLib;

import java.util.Iterator;
import java.util.Set;

public class SetIntersection<T> implements Reduction<Set<T>> {

	@Override
	public Set<T> reduce(Set<T> first, Set<T> second) {
		Iterator<T> iterator = first.iterator();
		while (iterator.hasNext()){
			if (!second.contains(iterator.next()))
				iterator.remove();
		}
		return first;
	}

}
