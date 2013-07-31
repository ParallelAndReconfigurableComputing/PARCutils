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

import java.util.Iterator;

import pi.exceptions.ParIteratorException;

/**
 * A Parallel Iterator over a collection. Unlike the standard sequential <code>java.util.Iterator</code>, the Parallel Iterator
 * is thread-safe and allows multiple threads to traverse it.
 * <p>
 * A Parallel Iterator instance is created using {@link ParIteratorFactory}.
 *  
 * @param <E>		The type of the elements returned from the Parallel Iterator.
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 * @author Lama Akeila
 */
public interface ParIterator<E> extends Iterator<E> {

	/**
	 * 
	 * Enum representing the possible schedules that a Parallel Iterator may have.
	 * 
	 * @author Nasser Giacaman
	 * @author Oliver Sinnen
	 */
	public static enum Schedule { 
		/**
		 * 	Elements are distributed amongst threads at run-time using a fixed chunk size.
		 */
		DYNAMIC, 
		

		/**
		 * 	Elements are distributed amongst threads at run-time using a decreasing chunk size.
		 */
		GUIDED, 
		

		/**
		 * 	Elements are distributed amongst threads before any iteration begins using a fixed chunk size.
		 */
		STATIC };
	
	/**
	 * 	The default chunk size, depending on the scheduling policy.    
	 */
	public final static int DEFAULT_CHUNKSIZE = -1;
	
	/**
	 * Checks to see if any elements remain for the current thread. If such an element exists, <code>true</code> is returned 
	 * and the current thread must follow up with a binding call to <code>next()</code>. A thread must continue to obtain elements 
	 * in this fashion until this method returns <code>false</code>. 
	 * <p>
	 * Note that this method involves an implicit barrier for the last call to <code>hasNext()</code> in order to ensure all threads 
	 * finish at the same time.
	 * @return 	<code>true</code> if at least one element has been reserved for the calling thread (in which case it must follow up 
	 * 			with a call to <code>next()</code>).
	 * <br>
	 * 			<code>false</code> otherwise (in such a case, the calling thread blocks until all the other threads have completed 
	 * their iterations). 
	 * @see		#next()
	 */
	public boolean hasNext();
	
	void setThreadIdGenerator(UniqueThreadIdGeneratorNonStatic uniqueThreadIdGenerator);
	
	/**
	 * Returns an element for the current thread to process. A thread must not call this method unless it has previously called 
	 * <code>hasNext()</code> (and received <code>true</code>).
	 * <p> 
	 * Note that calls to <code>hasNext()</code> (that return <code>true</code>) only allow for one corresponding and binding call to 
	 * <code>hasNext()</code>. Therefore, multiple calls to <code>hasNext()</code> have no effect until the corresponding 
	 * call to <code>next()</code> takes place.  
	 * 
	 * @throws	RuntimeException	If this method has been called without a binding call to <code>hasNext()</code> 
	 * that returned <code>true</code>.
	 * @see		#hasNext()
	 */
	public E next();
	
	/**
	 * Causes all threads to stop processing elements at the next iteration boundary. Therefore, even if there were any unprocessed 
	 * elements, they will not be processed. In such a case, <code>false</code> is returned to every thread at the next call to 
	 * <code>hasNext()</code>. Therefore, all the threads safely stop iterating when they call <code>hasNext()</code> (includes
	 * an implicit barrier to ensure all threads stop at the same time).
	 * @see		#localBreak()
	 */
	public void globalBreak();
	
	/**
	 * Attempts to excuse the current thread from continuing to iterate (and re-distribute all unprocessed elements to 
	 * the other threads). This is only successful if the the elements previously assigned to this
	 * thread are successfully allocated to the other thread(s). If the elements cannot be assigned to another thread (because the other
	 * threads have already stopped processing), then this attempt to local break will fail.
	 * <p>
	 * Therefore, (as opposed to <code>globalBreak()</code>) this method guarantees that even if all threads attempt to 
	 * <code>localBreak()</code>, at least one will fail and is left to traverse the remaining elements. Note that in many 
	 * cases, calling <code>localBreak()</code> will break the original scheduling policy.   
	 *
	 * @return 	<code>true</code> if the attempt is successful, <code>false</code> otherwise.
	 * @see		#globalBreak()
	 */
	public boolean localBreak();
	
	/**
	 * When a thread encounters an exception during traversal of a Parallel Iterator, the exception is registered using this method. 
	 * Therefore, calling this method records the following information (wrapped inside a {@link ParIteratorException}):
	 *   <ul>
	 *   <li>	<code>e</code> - The exception that occurred 
	 *   <li>	The current thread (who encountered the exception)
	 *   <li>	The current element that the thread was processing (the <code>hasNext()</code> method serves as the iteration boundary to determine the current element).
	 *   </ul>
	 * 
	 * @param e		The exception to register
	 * @see			#getAllExceptions()
	 */
	public void register(Exception e);
	
	/**
	 * Returns all the exceptions that have (so far) occurred during traversal of the Parallel Iterator. All exceptions must be caught 
	 * and then registered by the thread that caught the exception.  
	 * @return	All the exceptions (each is wrapped inside a <code>ParIteratorException</code>), or an empty array if none were registered.
	 * @see		#register(Exception)
	 */
	public ParIteratorException<E>[] getAllExceptions();
	
	/**
	 * Removes from the underlying collection the last element returned by the Parallel Iterator to the current thread. 
	 * 
	 * @throws 	UnsupportedOperationException If the Parallel Iterator does not support this.
	 */
	public void remove();
	
}
