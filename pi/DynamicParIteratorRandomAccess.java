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

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dynamic scheduling for random access collections
 * 
 * @author Nasser Giacaman
 * @author Lama Akeila
 * @author Oliver Sinnen
 */
public class DynamicParIteratorRandomAccess<E> extends ParIteratorAbstract<E> {

	// The passed collection
	private List<E> list;

	// The size of the collection
	private int size;
	
	// represents the current pointer to the current elements. Intially point at the first element
	// in the collection
	private int pointer = 0;

	// The number of elements remaining in the collection. Initially it equals to the size of
	// the collection
	private int remainingElements = 0;

	// Lock to insure that one thread accesses the collection while copyin references of elements
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
	private ThreadLocal<Integer> cutoff = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};

	/**
	 * Constuctor
	 * 
	 * @param list
	 * @param chunksize
	 * @param numOfThreads
	 */
	public DynamicParIteratorRandomAccess(List<E> list, int chunksize, int numOfThreads, boolean ignoreBarrier) {
		super(numOfThreads, ignoreBarrier);
		this.list = list;
		// if the no chunksize was specified, the default is 1
		if (chunksize == ParIterator.DEFAULT_CHUNKSIZE){
			this.chunksize = 1;
		}else {
			this.chunksize = chunksize;
		}
		this.size = list.size();
		remainingElements = list.size();
	}

	
	/* (non-Javadoc)
	 * @see pi.ParIterator#hasNext()
	 */
	public boolean hasNext() {
		
		int tid = uniqueThreadIdGenerator.getCurrentThreadId();
		
		if (iShouldBreak[tid].get()) {
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
		
		
		// True if the thread is still within its current chunksize
		if (currentIndex.get() < cutoff.get()) {
			
			//-- record current element of this iteration boundary, used for exception handling
			int index = currentIndex.get();
			currentElements.put(tid, list.get(index));
			
			return true;

		} else {
			// When a thread finishes processing the its current chunk, it gets the next chunk
			// or exits if no elements remaining
			copyLock.lock();
			if ( currentIndex.get().equals(cutoff.get()) & remainingElements > 0) {
			
				int var;
				if (remainingElements < chunksize) {
					var = remainingElements;
				} else {
					var = chunksize;
				}
				
				currentIndex.set(pointer);				
				cutoff.set(pointer + var);				
				pointer += var;				
				remainingElements = size - pointer;				
				copyLock.unlock();
				
				//-- record current element of this iteration boundary, used for exception handling
				int index = currentIndex.get();
				currentElements.put(tid, list.get(index));
				
				return true;

			} else {
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
		
		int index = currentIndex.get();
		currentIndex.set(currentIndex.get() + 1);
		return list.get(index);
	}

	@Override
	protected List<E> getUnprocessedElements() {
		ArrayList<E> unprocessed = new ArrayList<E>();
		while (currentIndex.get() < cutoff.get()){
			int index = currentIndex.get();
			currentIndex.set(currentIndex.get()+1);
			unprocessed.add(list.get(index));
		}
		return unprocessed;
	}

}
