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
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Guided scheduling for random access collections
 *        
 * @author Nasser Giacaman
 * @author Lama Akeila
 * @author Oliver Sinnen
 */
public class GuidedParIteratorRandomAccess<E> extends ParIteratorAbstract<E> {

	private int size;

	private List<E> list;

	private int pointer = 0;

	private int minChunk;

	private int remainingElements = 0;

	private final ReentrantLock copyLock = new ReentrantLock();

	private ThreadLocal<Integer> localChunksize = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};

	// private to each thread..
	private ThreadLocal<Integer> bufferIndex = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};

	private ThreadLocal<Integer> bufferCutoff = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return -1;
		}
	};

	/**
	 * @param list
	 * @param chunksize
	 * @param numOfThreads
	 */
	public GuidedParIteratorRandomAccess(List<E> list, int chunksize, int numOfThreads, boolean ignoreBarrier) {
		super(numOfThreads, ignoreBarrier);
		
		this.list = list;
		this.size = list.size();
		
		if (chunksize == ParIterator.DEFAULT_CHUNKSIZE) {
			this.minChunk = 1;
		} else {
			this.minChunk = chunksize;
		}
		
		this.chunksize = (int)Math.ceil((((double)size)/numOfThreads));
		
		remainingElements = list.size();
	}

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
		
		if (bufferIndex.get() < bufferCutoff.get()) {
			
			//-- record current element of this iteration boundary, used for exception handling
			int index = bufferIndex.get();
			currentElements.put(tid,list.get(index));
			
			return true;

		} else {
			copyLock.lock();
			if (bufferIndex.get().equals(bufferCutoff.get())
					& remainingElements > 0) {

				int var;
				localChunksize.set((int)Math.ceil(((double)remainingElements)/ numOfThreads));
				if (localChunksize.get() < minChunk)
					localChunksize.set(minChunk);
				if (remainingElements < localChunksize.get()) {
					var = remainingElements;
					// chunksize = remainingElements;
				} else {
					var = localChunksize.get();
				}

				bufferIndex.set(pointer);

				bufferCutoff.set(pointer + var);

				pointer += var;

				remainingElements = size - pointer;

				copyLock.unlock();
				
				//-- record current element of this iteration boundary, used for exception handling
				int index = bufferIndex.get();
				currentElements.put(tid,list.get(index));
				
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
	
	public E next() {

		//-- checks the reclaimed elements
		E reclaimedElement = savedReclaimedElement.get(); 
		if (reclaimedElement != null) {
			savedReclaimedElement.set(null);
			return reclaimedElement;
		}
		
		int index = bufferIndex.get();
		bufferIndex.set(bufferIndex.get() + 1);
		return list.get(index);
	}

	@Override
	protected List<E> getUnprocessedElements() {
		ArrayList<E> unprocessed = new ArrayList<E>();
		while (bufferIndex.get() < bufferCutoff.get()) {
			int index = bufferIndex.get();
			bufferIndex.set(bufferIndex.get()+1);
			unprocessed.add(list.get(index));
		}
		return unprocessed;
	}
}
