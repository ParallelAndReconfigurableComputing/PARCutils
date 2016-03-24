package pi.RedLib;

import java.util.Set;
/**
 * Creates the union of two <code>Set&lt;T&gt;</code>s. It basically iterates
 * through every element of the second <code>Set&lt;T&gt;</code>, and tries 
 * adding them to the first <code>Set&lt;T&gt;</code>. The nature of <code>Set</code>
 * does not allow duplicate elements to be added. 
 * 
 * @author Mostafa Mehrabi
 * @since  15/10/2014
 * */
public class SetUnion<T> implements Reduction<Set<T>> {

	@Override
	public Set<T> reduce(Set<T> first, Set<T> second) {
		for (T t : second)
			first.add(t);
		return first;
	}

}
