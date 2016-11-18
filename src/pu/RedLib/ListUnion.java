package pu.RedLib;

import java.util.List;
/**
 * This reduction class provides the union of two different list.
 * The difference between Union and Join is that Union does not
 * allow repetition of elements. 
 * 
 * @author Mostafa Mehrabi
 *
 * @param <T> The generic type that the ListUnion will operate on.
 */
public class ListUnion<T> implements Reduction<List<T>> {

	@Override
	public List<T> reduce(List<T> first, List<T> second) {
		for(T t : second){
			if(!first.contains(t))
				first.add(t);
		}
		return first;
	}

}
