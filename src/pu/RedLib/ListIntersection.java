package pu.RedLib;

import java.util.Iterator;
import java.util.List;

public class ListIntersection<T> implements Reduction<List<T>> {

	@Override
	public List<T> reduce(List<T> first, List<T> second) {
		Iterator<T> iterator = first.iterator();
		while(iterator.hasNext()){
			T t = iterator.next();
			if(!second.contains(t)){
				iterator.remove();
			}
		}
		return first;
	}

}
