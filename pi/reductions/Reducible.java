/*
 *  Copyright (C) 2009 Nasser Giacaman, Oliver Sinnen
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

package pi.reductions;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import pi.UniqueThreadIdGenerator;

/**
 *	Defines a Reducible. This essentials behaves like a thread-local variable, with the addition of a reduction function at the end
 *	that may be executed by any of the threads.
 *	<p>
 *	Note that the number of thread-local values used in the reduction is determined according to the number of unique 
 *	<code>Thread</code>s that {@link #set(Object)} a value.
 *
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */
public class Reducible<E> {
	
	private HashMap<Integer,E> threadValues = new HashMap<Integer,E>();
	private boolean alreadyReduced = false;
	private ReentrantLock reductionLock = new ReentrantLock();
	
	private E initialValue = null;
	
	private E reducedValue = null;
	
	/**
	 * A new <code>Reducible</code> without an initial value for any of the threads. Therefore, each thread must call
	 * {@link #set(Object)} before attempting to {@link #get()} its thread-local value. 
	 */
	public Reducible() {
	}
	
	/**
	 * Creates a <code>Reducible</code> with the specified initial value for all threads. 
	 * @param initialValue	The initial value to set for all threads. 
	 */
	public Reducible(E initialValue) {
		this.initialValue = initialValue;
	}
	
	/**
	 * Sets the thread-local value of the current thread to <code>value</code>.
	 * @param value		The value to set for the current thread.
	 */
	public void set(E value) {
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		threadValues.put(tid, value);
	}
	
	/**
	 * Returns the thread-local value for the current thread. This implies that the user has either specified a
	 * default initial value (in the constructor), and/or has already called {@link #set(Object)} for the current thread.
	 * @return	The thread-local value for the current thread.
	 * 
	 * @throws  RuntimeException If no initial value was supplied in the constructor and 
	 * no value has been {@link #set(Object)} for the current thread.
	 */
	public E get() {
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		if (!threadValues.containsKey(tid)) {
			if (initialValue == null)
				throw new RuntimeException("Attempting to get thread-local value of Reducible without first being initialised!");
			else
				threadValues.put(tid, initialValue);
		}
		return threadValues.get(tid);
	}
	
	/**
	 * Returns the number of thread-local values currently stored. Note that the initial value, if there is one, does not get included
	 * in this count. In other words, this is the number of calls made by unique threads to {@link #set(Object)} .   
	 * 
	 * @return	The number of thread-local values currently stored.
	 */
	public int countOfThreadLocalValues() {
		return threadValues.keySet().size();
	}
	
	/**
	 * Performs the specified reduction, as defined by the <code>reduction</code> instance. 
	 * <p>
	 * This method should be called at the end when all thread-local values are ready. Note that a reduction is only calculated once, 
	 * therefore subsequent calls to this method result in the pre-calculated value being returned. 
	 * 
	 * @param reduction	The reduction to perform.
	 * @return			The final reduced value after performing the reduction over all thread-local values.
	 * @throws			RuntimeException	If attempting to perform a reduction but there are no values that have been set 
	 * 					(either by specifying an initial value or using {@link #set(Object)}).
	 * 
	 */
	public E reduce(Reduction<E> reduction) {
		reductionLock.lock();
		
		if (alreadyReduced) {
			reductionLock.unlock();
			return reducedValue;
		}
		
		Integer[] threadIDs = threadValues.keySet().toArray(new Integer[]{});
		int numThreads = threadIDs.length;
		
		if (numThreads == 0) {
			
			if (initialValue == null) {
				reductionLock.unlock();
				throw new RuntimeException("Attempting to perform a reduction with no thread-local values or initial value!");
			} else {
				reducedValue = initialValue;
				reductionLock.unlock();
				return reducedValue;	
			}
		}
		
		E finalValue = threadValues.get(threadIDs[0]);
		alreadyReduced = true;
		
		if (numThreads == 1) {
			reducedValue = finalValue;
			reductionLock.unlock();
			return reducedValue;
		}
		
		//-- start from the second value
		for (int i = 1; i < numThreads; i++) {
			finalValue = reduction.reduce(finalValue, threadValues.get(threadIDs[i]));
		}
		
		reducedValue = finalValue;
		reductionLock.unlock();
		return reducedValue;
	}
}
