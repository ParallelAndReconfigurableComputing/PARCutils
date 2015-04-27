package pi.reductions;

import java.util.Collection;

/**
 * A join operation adds every element of the second <code>collection</code>
 * to the first <code>collection</code>, even if it already exists. However, 
 * the same operation cannot be replicated for <code>sets</code>, since sets
 * cannot have duplicate elements. 
 * 
 * @author Mostafa Mehrabi
 * @since  4/12/2014
 * */

public class CollectionJoin<T> implements Reduction<Collection<T>> {

	@Override
	public Collection<T> reduce(Collection<T> first, Collection<T> second) {
		for (T t : second){
			first.add(t);
		}
		return first;
	}

}
