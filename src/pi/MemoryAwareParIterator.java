package pi;

import pi.collect.Lists;

import java.util.*;

/**
 * Author: xiaoxing
 * Date: 29/06/13
 */
public class MemoryAwareParIterator<E> extends StaticParIterator<E> {

	public MemoryAwareParIterator(Collection<E> collection, int chunkSize, int numOfThreads, boolean ignoreBarrier) {
		super(collection, chunkSize, numOfThreads, ignoreBarrier);
	}

	@Override
	protected List<E> formatData(Collection<E> collection) {
		List<E> data;
		if (collection instanceof RandomAccess) {
			data = (List<E>) collection;
		} else {
			data = new ArrayList<E>(collection);
		}
		Lists.sortByPosition(data);
		return data;
	}

}
