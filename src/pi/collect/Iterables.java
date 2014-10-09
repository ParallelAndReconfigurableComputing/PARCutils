package pi.collect;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import static pi.util.Preconditions.checkArgument;
import static pi.util.Preconditions.checkNotNull;

/**
 * Author: xiaoxing
 * Date: 29/09/13
 */
public final class Iterables {
	private Iterables() {}

	/**
	 * Divides an iterable into unmodifiable sublists of the given size (the final
	 * iterable may be smaller). For example, partitioning an iterable containing
	 * {@code [a, b, c, d, e]} with a partition size of 3 yields {@code
	 * [[a, b, c], [d, e]]} -- an outer iterable containing two inner lists of
	 * three and two elements, all in the original order.
	 *
	 * <p>Iterators returned by the returned iterable do not support the {@link
	 * Iterator#remove()} method. The returned lists implement {@link
	 * RandomAccess}, whether or not the input list does.
	 *
	 * <p><b>Note:</b> The next method of the iterator within the Iterable is atomic.
	 *
	 * @param iterable the iterable to return a partitioned view of
	 * @param size the desired size of each partition (the last may be smaller)
	 * @return an iterable of unmodifiable lists containing the elements of {@code
	 *     iterable} divided into partitions
	 * @throws IllegalArgumentException if {@code size} is nonpositive
	 */
	public static <T> Iterable<List<T>> partition(
			final Iterable<T> iterable, final int size) {
		checkNotNull(iterable);
		checkArgument(size > 0);
		return new Iterable<List<T>>() {
			@Override
			public Iterator<List<T>> iterator() {
				return Iterators.partition(iterable.iterator(), size);
			}

			@Override
			public void forEach(Consumer<? super List<T>> action) {
				// TODO Auto-generated method stub
				/*added to suppress compiler errors*/
				return;
			}

			@Override
			public Spliterator<List<T>> spliterator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public static <T> Iterable<List<T>> partition(
			final List<T> list, final int size) {
		checkNotNull(list);
		checkArgument(size > 0);
		return new Iterable<List<T>>() {
			@Override
			public Iterator<List<T>> iterator() {
				return Iterators.partition(list, size);
			}

			@Override
			public void forEach(Consumer<? super List<T>> action) {
				// TODO Auto-generated method stub
				/*added to suppress compiler errors*/
				return;
			}

			@Override
			public Spliterator<List<T>> spliterator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
