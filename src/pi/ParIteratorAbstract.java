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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import pi.exceptions.PIExceptionHelper;
import pi.exceptions.ParIteratorException;

/**
 * This class is for convenience, provides an abstract implementation of the ParIterator interface  
 * 
 * It is only intended for internal use, if provides functionality that implies internal knowledge of 
 * StaticParIterator, DynamicParIterator, GuidedParIterator, etc.
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 * @author Lama Akeila
 * 
 */
public abstract class ParIteratorAbstract<E> implements ParIterator<E> {

	protected int chunksize;
	protected int numOfThreads;
	
	protected CountDownLatch latch = null;
	
	protected boolean ignoreBarrier = false;
	
	protected UniqueThreadIdGeneratorNonStatic uniqueThreadIdGenerator = new UniqueThreadIdGeneratorNonStatic();
	
	public void setThreadIdGenerator(UniqueThreadIdGeneratorNonStatic uniqueThreadIdGenerator) {
		this.uniqueThreadIdGenerator = uniqueThreadIdGenerator;
	}
	
	/*
	 * the following variables are used for the purpose of exception handling
	 */
	//-- stores all the registered exceptions that occurred during traversal
	protected ConcurrentLinkedQueue<ParIteratorException<E>> exceptions = new ConcurrentLinkedQueue<ParIteratorException<E>>();
	
	//-- for each thread, it's current element (as determined by the iteration boundary) is stored. See paper.
	protected HashMap<Integer,E> currentElements = new HashMap<Integer, E>();
	
	protected ThreadLocal<Boolean> successfullyCalledHasNext = new ThreadLocal<Boolean>();
	
	//-- used for breaks, each thread checks their position
	protected AtomicBoolean[] iShouldBreak = null;
	
	private ReentrantLock localBreakLock = new ReentrantLock();
    
	//-- elements that have been re-claimed (i.e. after a thread calls localBreak)
	protected ConcurrentLinkedQueue<E> reclaimedElements = new ConcurrentLinkedQueue<E>();
	
	protected ThreadLocal<E> savedReclaimedElement = new ThreadLocal<E>();
	
	public ParIteratorAbstract(int numOfThreads, boolean ignoreBarrier) {
		this.numOfThreads = numOfThreads;
		iShouldBreak = new AtomicBoolean[numOfThreads];
		for (int i = 0; i < numOfThreads; i++)
			iShouldBreak[i] = new AtomicBoolean(false);
		latch = new CountDownLatch(numOfThreads);
		this.ignoreBarrier = ignoreBarrier;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("This Parallel Iterator currently does not support remove()");
	}
	
	@Override
	public void globalBreak() {
		localBreakLock.lock();
		for (int i = 0; i < numOfThreads; i++)
			iShouldBreak[i].set(true);
		localBreakLock.unlock();
	}
	
	@Override
	public boolean localBreak() {
		int tid = uniqueThreadIdGenerator.getCurrentThreadId();
		localBreakLock.lock();
		
		if (allOtherThreadsHaveLocalBreaked(tid)) {
			localBreakLock.unlock();
			return false;
		} else {
			List<E> reclaimedList = getUnprocessedElements();
			reclaimedElements.addAll(reclaimedList);
			iShouldBreak[tid].set(true);
			localBreakLock.unlock();
			return true;
		}
	}
	
	protected boolean allOtherThreadsHaveLocalBreaked(int tid) {
		boolean allBreaked = true;
		for (int i = 0; i < numOfThreads; i++) {
			if (i != tid)
				allBreaked &= iShouldBreak[i].get();
		}
		return allBreaked;
	}
	
	@Override
	public void register(Exception e) {
		E curIteration = currentElements.get(uniqueThreadIdGenerator.getCurrentThreadId());
		Thread regThread = Thread.currentThread(); 
		ParIteratorException<E> pie = PIExceptionHelper.createException(e, curIteration, regThread);
		exceptions.add(pie);
	}
	
	@Override
	public ParIteratorException<E>[] getAllExceptions() {
		return exceptions.toArray(new ParIteratorException[] {});
	}
	
	abstract protected List<E> getUnprocessedElements();
	
	protected boolean tryToReclaimElement() {
		E grabReclaimedElement = reclaimedElements.poll();
		if (grabReclaimedElement != null) {
			savedReclaimedElement.set(grabReclaimedElement);
			return true;
		}
		return false;
	}
	
}
