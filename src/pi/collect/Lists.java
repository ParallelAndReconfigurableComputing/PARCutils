package pi.collect;

import pi.util.VMSpec;

import java.util.*;

import static pi.util.Preconditions.checkArgument;
import static pi.util.Preconditions.checkElementIndex;
import static pi.util.Preconditions.checkNotNull;

/**
 * Author: xiaoxing
 * Date: 29/09/13
 */
public final class Lists {
	private Lists() {}


	/**
	 * Returns a reversed view of the specified list. For example, {@code
	 * Lists.reverse(Arrays.asList(1, 2, 3))} returns a list containing {@code 3,
	 * 2, 1}. The returned list is backed by this list, so changes in the returned
	 * list are reflected in this list, and vice-versa. The returned list supports
	 * all of the optional list operations supported by this list.
	 *
	 * <p>The returned list is random-access if the specified list is random
	 * access.
	 *
	 * @since 7.0
	 */
	public static <T> List<T> reverse(List<T> list) {
//		if (list instanceof ImmutableList) {
//			return ((ImmutableList<T>) list).reverse();
//		} else if (list instanceof ReverseList) {
//			return ((ReverseList<T>) list).getForwardList();
//		} else if (list instanceof RandomAccess) {
//			return new RandomAccessReverseList<T>(list);
//		} else {
//			return new ReverseList<T>(list);
//		}
		return null;
	}

//	private static class ReverseList<T> extends AbstractList<T> {
//		private final List<T> forwardList;
//
//		ReverseList(List<T> forwardList) {
//			this.forwardList = checkNotNull(forwardList);
//		}
//
//		List<T> getForwardList() {
//			return forwardList;
//		}
//
//		private int reverseIndex(int index) {
//			int size = size();
//			checkElementIndex(index, size);
//			return (size - 1) - index;
//		}
//
//		private int reversePosition(int index) {
//			int size = size();
//			checkPositionIndex(index, size);
//			return size - index;
//		}
//
//		@Override public void add(int index, @Nullable T element) {
//			forwardList.add(reversePosition(index), element);
//		}
//
//		@Override public void clear() {
//			forwardList.clear();
//		}
//
//		@Override public T remove(int index) {
//			return forwardList.remove(reverseIndex(index));
//		}
//
//		@Override protected void removeRange(int fromIndex, int toIndex) {
//			subList(fromIndex, toIndex).clear();
//		}
//
//		@Override public T set(int index, @Nullable T element) {
//			return forwardList.set(reverseIndex(index), element);
//		}
//
//		@Override public T get(int index) {
//			return forwardList.get(reverseIndex(index));
//		}
//
//		@Override public int size() {
//			return forwardList.size();
//		}
//
//		@Override public List<T> subList(int fromIndex, int toIndex) {
//			checkPositionIndexes(fromIndex, toIndex, size());
//			return reverse(forwardList.subList(
//					reversePosition(toIndex), reversePosition(fromIndex)));
//		}
//
//		@Override public Iterator<T> iterator() {
//			return listIterator();
//		}
//
//		@Override public ListIterator<T> listIterator(int index) {
//			int start = reversePosition(index);
//			final ListIterator<T> forwardIterator = forwardList.listIterator(start);
//			return new ListIterator<T>() {
//
//				boolean canRemoveOrSet;
//
//				@Override public void add(T e) {
//					forwardIterator.add(e);
//					forwardIterator.previous();
//					canRemoveOrSet = false;
//				}
//
//				@Override public boolean hasNext() {
//					return forwardIterator.hasPrevious();
//				}
//
//				@Override public boolean hasPrevious() {
//					return forwardIterator.hasNext();
//				}
//
//				@Override public T next() {
//					if (!hasNext()) {
//						throw new NoSuchElementException();
//					}
//					canRemoveOrSet = true;
//					return forwardIterator.previous();
//				}
//
//				@Override public int nextIndex() {
//					return reversePosition(forwardIterator.nextIndex());
//				}
//
//				@Override public T previous() {
//					if (!hasPrevious()) {
//						throw new NoSuchElementException();
//					}
//					canRemoveOrSet = true;
//					return forwardIterator.next();
//				}
//
//				@Override public int previousIndex() {
//					return nextIndex() - 1;
//				}
//
//				@Override public void remove() {
//					checkState(canRemoveOrSet, "no calls to next() since the last call to remove()");
//					forwardIterator.remove();
//					canRemoveOrSet = false;
//				}
//
//				@Override public void set(T e) {
//					checkState(canRemoveOrSet);
//					forwardIterator.set(e);
//				}
//			};
//		}
//	}
//
//	private static class RandomAccessReverseList<T> extends ReverseList<T>
//			implements RandomAccess {
//		RandomAccessReverseList(List<T> forwardList) {
//			super(forwardList);
//		}
//	}

	/**
	 * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list,
	 * each of the same size (the final list may be smaller). For example,
	 * partitioning a list containing {@code [a, b, c, d, e]} with a partition
	 * size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer list containing
	 * two inner lists of three and two elements, all in the original order.
	 *
	 * <p>The outer list is unmodifiable, but reflects the latest state of the
	 * source list. The inner lists are sublist views of the original list,
	 * produced on demand using {@link List#subList(int, int)}, and are subject
	 * to all the usual caveats about modification as explained in that API.
	 *
	 * @param list the list to return consecutive sublists of
	 * @param size the desired size of each sublist (the last may be
	 *     smaller)
	 * @return a list of consecutive sublists
	 * @throws IllegalArgumentException if {@code partitionSize} is nonpositive
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {
		checkNotNull(list);
		checkArgument(size > 0);
		return (list instanceof RandomAccess)
				? new RandomAccessPartition<T>(list, size)
				: new Partition<T>(list, size);
	}

	private static class Partition<T> extends AbstractList<List<T>> {
		final List<T> list;
		final int size;

		Partition(List<T> list, int size) {
			this.list = list;
			this.size = size;
		}

		@Override public List<T> get(int index) {
			checkElementIndex(index, size());
			int start = index * size;
			int end = Math.min(start + size, list.size());
			return list.subList(start, end);
		}

		@Override public int size() {
			int s = list.size() / size;
			if (list.size() % size > 0) {
				s += 1;
			}
			return s;
		}

		@Override public boolean isEmpty() {
			return list.isEmpty();
		}
	}

	private static class RandomAccessPartition<T> extends Partition<T>
			implements RandomAccess {
		RandomAccessPartition(List<T> list, int size) {
			super(list, size);
		}
	}

	public static <T> void sortByPosition(List<T> list) {
		final Map<T, Long> addressMap = getAddressMap(list);
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return (int) (addressMap.get(o1) - addressMap.get(o2));
			}
		});
	}

	private static <T> Map<T, Long> getAddressMap(Collection<T> data) {
		Map<T, Long> addressMap = new HashMap<T, Long>();
		for (T d : data) {
			addressMap.put(d, VMSpec.addressOf(d));
		}
		return addressMap;
	}


}
