package pi;

import java.util.*;

/**
 * Author: xiaoxing
 * Date: 1/06/13
 */
public class GuidedParIterator<E> extends DynamicParIterator<E> {

	public GuidedParIterator(final Collection<E> collection, final int chunkSize, final int numOfThreads, final boolean ignoreBarrier) {
		super(collection, chunkSize, numOfThreads, ignoreBarrier);
	}

	@Override
	protected Iterator<List<E>> partition(
			final Collection<E> collection, final int minChunkSize, final int numOfThreads) {
		if (collection instanceof RandomAccess) {
			return new Iterator<List<E>>() {
				@Override
				public boolean hasNext() {
					return (cursor < data.size());
				}

				List<E> data = (List<E>)collection;
				int cursor = 0;

				@Override
				public synchronized List<E> next() {
					if (!hasNext()) {
						return null;
					}
					int from = cursor;
					int remaining = data.size() - from;
					int len = (int) Math.ceil(remaining / numOfThreads);
					if (len < minChunkSize) {
						len = minChunkSize;
					}
					if (len > remaining) {
						len = remaining;
					}
					cursor += len;
					return data.subList(from, from + len);
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};

		} else {
			final Iterator<E> iterator = collection.iterator();
			return new Iterator<List<E>>() {
				@Override
				public boolean hasNext() {
					return iterator.hasNext();
				}

				int remaining = collection.size();
				@Override
				public synchronized List<E> next() {
					if (!hasNext()) {
						return null;
					}
					int len = (int) Math.ceil(remaining / numOfThreads);
					if (len < minChunkSize) {
						len = minChunkSize;
					}
					if (len > remaining) {
						len = remaining;
					}
					Object[] array = new Object[len];
					int count = 0;
					for (; count < len && iterator.hasNext(); count++) {
						array[count] = iterator.next();
					}
					remaining -= len;

					@SuppressWarnings("unchecked")
					List<E> list = Collections.unmodifiableList(
							(List<E>) Arrays.asList(array));
					return list;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}
}
