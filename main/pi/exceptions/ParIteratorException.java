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

package pi.exceptions;

import pi.ParIterator;

/**
 *	Stores information relevant to an exception that occurred during a particular iteration. Note that this class is not an 
 * <code>Exception</code>, but simply a wrapper to encapsulate the various information when an exception is registered. 
 * <p>
 * See also {@link ParIterator#register(Exception)} and {@link ParIterator#getAllExceptions()}.
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */
public class ParIteratorException<E> {
	
	private Exception exception = null;
	private E iteration = null;
	private Thread registeringThread = null;
	
	/*
	 * This constructor has package access, therefore it is instantiated by the PIExceptionHelper.
	 * The purpose is to hide the constructor from the user API, no need for users to create instances of this class. 
	 */
	ParIteratorException(Exception exception, E iteration, Thread registeringThread) {
		this.exception = exception;
		this.iteration = iteration;
		this.registeringThread = registeringThread;
	}
	
	/**
	 * Returns the <code>Exception</code> that was registered.  
	 * @return	The registered exception.
	 */
	public Exception getException() {
		return exception;
	}
	
	/**
	 * The element that the thread was processing (or due to process) at the time the exception was registered. Note that this is 
	 * determined by the <i>iteration boundary</i>, which is essentially the {@link ParIterator#hasNext()} method.
	 * 
	 * @return		The element of the current iteration when the exception was registered.
	 */
	public E getIteration() {
		return iteration;
	}

	/**
	 * The <code>Thread</code> that registered the exception. For meaningful debugging, the thread that registers the exception should also
	 * be the thread that caught the exception. 
	 * @return	The <code>Thread</code> that registered the exception.
	 */
	public Thread getRegisteringThread() {
		return registeringThread;
	}
}
