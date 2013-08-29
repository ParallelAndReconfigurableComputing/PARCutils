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
import java.util.concurrent.locks.ReentrantLock;


/**
 * Guided scheduling for inherently sequential collections
 *        
 * @author Nasser Giacaman
 * @author Lama Akeila
 * @author Oliver Sinnen
 */
public class GuidedParIteratorSequential<E> extends ParIteratorAbstract<E> {
	
	private Iterator it;

	private Object[][] buffer;

	private int size;
	
	private int minChunk;
	
	// number of elements that have been copied out
	// of the main collection
	private int counter = 0; 

	private final ReentrantLock copyLock = new ReentrantLock();

	private ThreadLocal<Integer> localChunksize = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};
	
	private ThreadLocal<Integer> bufferIndex = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};

	private ThreadLocal<Integer> bufferCutoff = new ThreadLocal<Integer>();

	public GuidedParIteratorSequential(Collection collection,int chunksize, int numOfThreads, boolean ignoreBarrier) {
		super(numOfThreads, ignoreBarrier);
		
		if (chunksize == ParIterator.DEFAULT_CHUNKSIZE){
			this.minChunk = 1;
		}else {
			this.minChunk = chunksize;
		}
		this.size = collection.size();
		// Maximum chunksize
		this.chunksize = (int)Math.ceil((((double)size)/numOfThreads));
		
		buffer = new Object[numOfThreads][this.chunksize];
		this.it = collection.iterator();

	}
	
	public boolean hasNext() {
	
		// get thread ID
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
		if ((bufferIndex.get() >= 0) & (bufferIndex.get() < localChunksize.get())) {

			if (bufferIndex.get().equals(bufferCutoff.get())) {

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
			int index = bufferIndex.get();
			currentElements.put(id, (E)buffer[id][index]);

			return true;

		} else {

			copyLock.lock();
			
			
			// if there are any more elements in the collection
			if (it.hasNext()) {

				int remainingElements = size - counter;
				localChunksize.set((int)Math.ceil(((double)remainingElements)/ numOfThreads));
				
				if (localChunksize.get() < minChunk)
					localChunksize.set(minChunk);
				
				
				int toCopy = -1;
				if (remainingElements < localChunksize.get()) {

					toCopy = remainingElements;

				} else {
					toCopy = localChunksize.get() ;

				}

				for (int i = 0; i < toCopy; i++) {
					buffer[id][i] = it.next();
					counter++;
				}
				// resets the bufferIndex to zero
				bufferIndex.set(0);
				bufferCutoff.set(toCopy);
				copyLock.unlock();
				
				//-- record current element of this iteration boundary, used for exception handling
				int index = bufferIndex.get();
				currentElements.put(id, (E)buffer[id][index]);
				
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

	public E next() {
		int id = uniqueThreadIdGenerator.getCurrentThreadId();
		
		//-- checks the reclaimed elements
		E reclaimedElement = savedReclaimedElement.get(); 
		if (reclaimedElement != null) {
			savedReclaimedElement.set(null);
			return reclaimedElement;
		}
		
		int currentIndex = bufferIndex.get();
		bufferIndex.set(currentIndex + 1);
		return (E)buffer[id][currentIndex];

	}

	@Override
	protected List<E> getUnprocessedElements() {
		ArrayList<E> unprocessed = new ArrayList<E>();
		int tid = uniqueThreadIdGenerator.getCurrentThreadId();
		while ((bufferIndex.get() >= 0) & (bufferIndex.get() < localChunksize.get())) {
			if (!bufferIndex.get().equals(bufferCutoff.get())) {
				int index = bufferIndex.get();
				bufferIndex.set(index + 1);
				unprocessed.add((E) buffer[tid][index]);
			}
		}
		return unprocessed;
	}
}
