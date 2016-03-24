package pi.RedLib;

import java.util.Collection;


/**
 * Creates the union of the first <code>Collection&lt;T&gt;</code> and the second 
 * <code>Collection&lt;T&gt;</code> which are sent as arguments. The reducer iterates
 * through every element of the second <code>Collection&lt;T&gt;</code>, and tries 
 * adding them to the first <code>Collection&lt;T&gt;</code>. An attempt is successful
 * if and only if the corresponding element is not part of the first collection already!
 * 
 * @author Mostafa Mehrabi
 * @since  15/10/2014
 * */
public class CollectionUnion<T> implements Reduction<Collection<T>> {
	
	@Override
	public Collection<T> reduce(Collection<T> first, Collection<T> second) {
		for (T t : second){
			if (!first.contains(t))
				first.add(t);
		}
		return first;
	}

}
