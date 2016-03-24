package pi.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * 
 * A List that "dynamically" creates its elements. It does not contain a buffer of elements, instead
 * the values of the "contained" elements are calculated on the spot when {@link #get(int)} is used.
 * <br><br>
 * Since the elements are calculated on the spot, this collection only contains elements with a
 * uniform integer range. Must therefore use the constructor to specify those parameters, and no 
 * other operations are supported (for example, cannot add, or remove elements, not even an iterator).
 * <br><br>
 * The purpose of this collection is to allow an arbitrarily large collection, without using a ridiculous
 * amount of memory. 
 * <br><br>
 * Almost all the standard List methods throw an <code>UnsupportedOperationException</code>. The only methods supported by this class include:
 * <ul>
 *   <li> {@link #get(int)}
 *   <li> {@link #size()}
 *   <li> {@link #isEmpty()}
 * </ul>
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */
public class DynamicList implements List<Integer>, RandomAccess {
	
	private int start;
	private int size;
	private int increment;
	private DynamicListIterator iter;
	
	/**
	 * Create a new DynamicList. Must specify the integer range of the "contained" elements.
	 * 
	 * @param start			The first element value.
	 * @param size			The number of the elements to create. 
	 * @param increment		The increment amount between neighbouring elements.
	 */
	public DynamicList(int start, int size, int increment) {
		this.start = start;
		this.size = size;
		this.increment = increment;

	}
	
	@Override
	public boolean add(Integer e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, Integer element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends Integer> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer get(int index) {
		return start + index * increment; 
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return size != 0;
	}

	@Override
	public Iterator<Integer> iterator() {
//		throw new UnsupportedOperationException();
		return new DynamicListIterator();
	}
	
	class DynamicListIterator implements Iterator{
		private int current = -1;
		@Override
		public boolean hasNext() {
			return current+1 < size;
			
		}
	
		@Override
		public Object next() {
			current++;
			return DynamicList.this.get(current);
		}
	
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
			
		}

		@Override
		public void forEachRemaining(Consumer action) {
			// TODO Auto-generated method stub
			/*added to suppress compiler errors*/
			return;
		}
		
	}
	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<Integer> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<Integer> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer set(int index, Integer element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<Integer> subList(int fromIndex, int toIndex) {
		int init = this.get(fromIndex);
		return new DynamicList(init, toIndex-fromIndex, this.increment);
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeIf(Predicate<? super Integer> filter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Stream<Integer> stream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Integer> parallelStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEach(Consumer<? super Integer> action) {
		// TODO Auto-generated method stub
		/*added to suppress compiler errors*/
		return;
	}

	@Override
	public void replaceAll(UnaryOperator<Integer> operator) {
		// TODO Auto-generated method stub
		/*added to suppress compiler errors*/
		return;
	}

	@Override
	public void sort(Comparator<? super Integer> c) {
		// TODO Auto-generated method stub
		/*added to suppress compiler errors*/
		return;
	}

	@Override
	public Spliterator<Integer> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}


}
