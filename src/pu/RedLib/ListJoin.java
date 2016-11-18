package pu.RedLib;

import java.util.List;

public class ListJoin<T> implements Reduction<List<T>> {

	@Override
	public List<T> reduce(List<T> first, List<T> second) {
		for(T t : second){
			first.add(t);
		}
		return first;
	}

}
