package pi.collect;

import java.util.*;

import static pi.util.Preconditions.checkArgument;
import static pi.util.Preconditions.checkNotNull;

/**
 * Author: xiaoxing
 * Date: 29/09/13
 */
public final class Iterators {
	private Iterators() {}

	/**
	 * Divides an iterator into unmodifiable sublists of the given size (the final
	 * list may be smaller). For example, partitioning an iterator containing
	 * {@code [a, b, c, d, e]} with a partition size of 3 yields {@code
	 * [[a, b, c], [d, e]]} -- an outer iterator containing two inner lists of
	 * three and two elements, all in the original order.
	 *
	 * <p>The returned lists implement {@link java.util.RandomAccess}.
	 *
	 * @param iterator the iterator to return a partitioned view of
	 * @param size the desired size of each partition (the last may be smaller)
	 * @return an iterator of immutable lists containing the elements of {@code
	 *     iterator} divided into partitions
	 * @throws IllegalArgumentException if {@code size} is nonpositive
	 */
	public static <T> Iterator<List<T>> partition(
			final Iterator<T> iterator, final int size) {
		checkNotNull(iterator);
		checkArgument(size > 0);
		return new Iterator<List<T>>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			@Override
			public synchronized List<T> next() {
				if (!hasNext()) {
					return null;
				}
				Object[] array = new Object[size];
				int count = 0;
				for (; count < size && iterator.hasNext(); count++) {
					array[count] = iterator.next();
				}
				for (int i = count; i < size; i++) {
					array[i] = null; // for GWT
				}

				@SuppressWarnings("unchecked") // we only put Ts in it
						List<T> list = Collections.unmodifiableList(
						(List<T>) Arrays.asList(array));
				return list.subList(0, count);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Partition List,better performance with RandomAccess type.
	 * next method is atomic.
	 * @param list Data.
	 * @param size Chunk size.
	 * @param <T> Element type.
	 * @return Partitioned chunks.
	 */
	public static <T> Iterator<List<T>> partition(
			final List<T> list, final int size) {
		checkNotNull(list);
		checkArgument(size > 0);
		return new Iterator<List<T>>() {

			int cursor = 0;

			@Override
			public boolean hasNext() {
				return cursor < list.size();
			}

			@Override
			public synchronized List<T> next() {
				if (!hasNext()) {
					return null;
				}
				int from = cursor;
				int remaining = list.size() - from;
				int len = Math.min(remaining, size);
				cursor += len;
				return list.subList(from, from + len);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};	}


}
