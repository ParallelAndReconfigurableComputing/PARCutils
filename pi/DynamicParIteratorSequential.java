/*
 *  Copyright (C) 2009 Nasser Giacaman, Oliver Sinnen, Lama Akeila
 *
 *  This file is part of Parallel Iterator.
 *
 *  Parallel Iterator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Iterator is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Iterator. If not, see <http://www.gnu.org/licenses/>.
 */

package pi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.*;

/**
 * Dynamic scheduling for inherently sequential collections
 *        
 * @author Nasser Giacaman
 * @author Lama Akeila
 * @author Oliver Sinnen
 */
public class DynamicParIteratorSequential<E> extends ParIteratorAbstract<E> {

	// internal sequential iterator to iterate through the collection
	private Iterator it;

	// 2D array where references of the elements in the collection will be copied
	private Object[][] buffer;

	// size of the collection
	private int size;

	// number of elements that have been copied out to the buffer
	private int counter = 0;

	// Lock to insure that one thread accesses the collection while copying references of elements
	// to the buffer
	private final ReentrantLock copyLock = new ReentrantLock();

	// currentIndex represents the index each thread is up to while traversing the elements. Each thread
	// has its own value.
	private ThreadLocal<Integer> currentIndex = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};

	// cutoff represents the end of the thread index range. When reached a thead has to copy across the 
	// next chunksize assigned for it or exists if no more remaining elements left in the collection.
	private ThreadLocal<Integer> cutoff = new ThreadLocal<Integer>();

	/**
	 * Consturctor
	 * 
	 * @param collection
	 * @param chunksize
	 * @param numOfThreads
	 */
	public DynamicParIteratorSequential(Collection<E> collection, int chunksize, int numOfThreads, boolean ignoreBarrier) {
		super(numOfThreads, ignoreBarrier);
		
		// if the no chunksize was specified, the default is 1
		if (chunksize == ParIterator.DEFAULT_CHUNKSIZE) {
			this.chunksize = 1;
		} else {
			this.chunksize = chunksize;
		}

		//Creates a 2D array with numOfThread rows and chunksize columns
		buffer = new Object[numOfThreads][this.chunksize];

		//Sequential Iterator
		this.it = collection.iterator();
		this.size = collection.size();

	}

	/* (non-Javadoc)
	 * @see pi.ParIterator#hasNext()
	 */
	public boolean hasNext() {

		// gets thread ID
		int id = uniqueThreadIdGenerator.getCurrentThreadId();

		if (iShouldBreak[id].get()) {

			if (ignoreBarrier)
				return false;
			
			latch.countDown();
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}

		// see if there's any elements for this thread in THE PRIVATE BUFFER
		if ((currentIndex.get() >= 0) & (currentIndex.get() < chunksize)) {

			if (currentIndex.get().equals(cutoff.get())) {

				if (ignoreBarrier)
					return false;
				
				latch.countDown();
				try {
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				return false;
			}
			
			//-- record current element of this iteration boundary, used for exception handling
			int index = currentIndex.get();
			currentElements.put(id, (E) buffer[id][index]);
			
			return true;
			
		} else {

			copyLock.lock();
			// if there are any more elements in the collection
			if (it.hasNext()) {

				int remainingElements = size - counter;
				int toCopy = -1;
				if (remainingElements < chunksize) {
					toCopy = remainingElements;
				} else {
					toCopy = chunksize;
				}

				for (int i = 0; i < toCopy; i++) {
					buffer[id][i] = it.next();
					counter++;
				}

				// resets the bufferIndex to zero
				currentIndex.set(0);
				cutoff.set(toCopy);
				copyLock.unlock();
				
				//-- record current element of this iteration boundary, used for exception handling
				int index = currentIndex.get();
				currentElements.put(id, (E) buffer[id][index]);
				
				return true;

			} else {
				// unlock & return 'false'
				copyLock.unlock();
				
				//-- nothing left to do, so attempt to localBreak
				boolean iBroke = localBreak();
				
				// check to see if there are any reclaimed elements...
				if (!iBroke) {
					boolean gotAnElement = tryToReclaimElement();
					if (gotAnElement)
						return true;
				}

				if (ignoreBarrier)
					return false;
				
				latch.countDown();
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see pi.ParIterator#next()
	 */
	public E next() {

		//-- checks the reclaimed elements
		E reclaimedElement = savedReclaimedElement.get(); 
		if (reclaimedElement != null) {
			savedReclaimedElement.set(null);
			return reclaimedElement;
		}
		
		int id = uniqueThreadIdGenerator.getCurrentThreadId();
		int index = currentIndex.get();
		currentIndex.set(index + 1);

		return (E) buffer[id][index];
	}

	@Override
	protected List<E> getUnprocessedElements() {
		ArrayList<E> unprocessed = new ArrayList<E>();
		int tid = uniqueThreadIdGenerator.getCurrentThreadId();
		while ((currentIndex.get() >= 0) & (currentIndex.get() < chunksize)) {
			if (!currentIndex.get().equals(cutoff.get())) {
				int index = currentIndex.get();
				currentIndex.set(index + 1);
				unprocessed.add((E) buffer[tid][index]);
			}
		}
		return unprocessed;
	}
}
