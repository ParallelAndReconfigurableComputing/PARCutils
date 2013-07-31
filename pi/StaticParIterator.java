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

/**
 * Static scheduling 
 * 
 * @author Nasser Giacaman
 * @author Lama Akeila
 * @author Oliver Sinnen
 *        
 */
public class StaticParIterator<E> extends ParIteratorAbstract<E> {

	private List<E> list;

	private E[] collection;
	
	// the cutoff value for each thread
	private int[] cutoff;

	private int[] currentIndex;

	private int[] blocksToDo;

	private int[] blocksDone;

	private boolean isFromList = false;
	
	protected ThreadLocal<Boolean> ignoreBarrierStatic = new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() { return false;	} };

	// Constructor for inherently sequential collections
	public StaticParIterator(E[] collection, int chunksize, int numOfThreads, boolean ignoreBarrier) {
		super(numOfThreads, ignoreBarrier);
		this.chunksize = chunksize;
		this.collection = collection;
		initialise();
	}

	// Constructor for random access collections
	public StaticParIterator(List<E> list, int chunksize, int numOfThreads, boolean ignoreBarrier) {
		super(numOfThreads, ignoreBarrier);
		isFromList = true;
		this.chunksize = chunksize;
		this.list = list;
		initialise();
	}

	/*
	 * This method is called once by the constructor. It allocates each thread with the number of
	 * iterations it has to do.
	 */
	private void initialise() {

		int remainingElements;
		if (isFromList)
			// for random access collection
			remainingElements = list.size();
		else
			// for inherently sequential collections
			remainingElements = collection.length;

		int remainingThreads = numOfThreads;
		currentIndex = new int[numOfThreads];
		cutoff = new int[numOfThreads];

		blocksToDo = new int[numOfThreads];
		blocksDone = new int[numOfThreads];

		for (int i = 0; i < numOfThreads; i++) {
			blocksDone[i] = 0;
		}
		int pointer = 0;
		
		if (chunksize == ParIterator.DEFAULT_CHUNKSIZE) {

			for (int i = 0; i < numOfThreads; i++) {

				float block = (float) remainingElements / remainingThreads;

				int localChunk = (int) Math.ceil(block);
				blocksToDo[i] = 1;
				currentIndex[i] = pointer;
				cutoff[i] = currentIndex[i] + localChunk;
				pointer += localChunk;
				remainingElements -= localChunk;
				remainingThreads--;

			}

		} else {

			int numBlocksToDo = (int) Math
					.ceil((((double) list.size()) / this.chunksize));
			int numThreadWithExtraBlock = numBlocksToDo % numOfThreads;
			double numBlocksPerThread = ((double) numBlocksToDo) / numOfThreads;

			for (int i = 0; i < numOfThreads; i++) {

				if (i < numThreadWithExtraBlock) {
					blocksToDo[i] = (int) Math.ceil(numBlocksPerThread);
				} else {
					blocksToDo[i] = (int) Math.floor(numBlocksPerThread);
				}

				currentIndex[i] = pointer;
				pointer += this.chunksize;
				cutoff[i] = pointer;
				if (cutoff[i] > list.size())
					cutoff[i] = list.size();

			}
		}
	}

	/* (non-Javadoc)
	 * @see pi.ParIterator#hasNext()
	 */
	public boolean hasNext() {
		
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
		
		//-- this marks the beginning of the "iteration boundary" (for exception handling). Therefore need to record the current element.
		
		if (currentIndex[id] < cutoff[id]) {
			int index = currentIndex[id];
			
			//-- record current element of this iteration boundary, used for exception handling
			if (isFromList)
				currentElements.put(id, list.get(index));
			else
				currentElements.put(id, collection[index]);
			
			successfullyCalledHasNext.set(true);
			return true;
		}
		
		blocksDone[id]++;
		if (blocksDone[id] < blocksToDo[id]) {
			currentIndex[id] = currentIndex[id] + (numOfThreads - 1) * chunksize;
			cutoff[id] = currentIndex[id] + chunksize;
			if (cutoff[id] > list.size())
				cutoff[id] = list.size();
			
			//-- record current element of this iteration boundary, used for exception handling
			int index = currentIndex[id];
			if (isFromList)
				currentElements.put(id, list.get(index));
			else
				currentElements.put(id, collection[index]);
			
			successfullyCalledHasNext.set(true);
			return true;
			
		} else {

			//-- nothing left to do, so attempt to localBreak
			boolean iBroke = false;
			if (!ignoreBarrierStatic.get())
				iBroke = localBreak();
			
			// check to see if there are any reclaimed elements...
			if (!iBroke) {
				boolean gotAnElement = tryToReclaimElement();
				if (gotAnElement) {
					successfullyCalledHasNext.set(true);
					return true;
				}
			}
			
			if (!ignoreBarrierStatic.get()) {
				
				if (ignoreBarrier)
					return false;
				
				latch.countDown();
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see pi.ParIterator#next()
	 */
	public E next() {
		int id = uniqueThreadIdGenerator.getCurrentThreadId();
		
		if (!successfullyCalledHasNext.get())
			throw new RuntimeException("Thread "+Thread.currentThread()+" is calling next() without an associated call to hasNext()");
		
		successfullyCalledHasNext.set(false);
		
		//-- checks the reclaimed elements
		E reclaimedElement = savedReclaimedElement.get(); 
		if (reclaimedElement != null) {
			savedReclaimedElement.set(null);
			return reclaimedElement;
		}
		
		int index = currentIndex[id];
		currentIndex[id]++;

		if (isFromList)
			return list.get(index);
		else
			return collection[index];
	}
	
	@Override
	protected List<E> getUnprocessedElements() {
		ArrayList<E> unprocessed = new ArrayList<E>();
		ignoreBarrierStatic.set(true);
		while (hasNext()) {
			unprocessed.add(next());
		}
		ignoreBarrierStatic.set(false);
		return unprocessed;
	}
}
