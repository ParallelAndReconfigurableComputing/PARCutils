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

/**
 *	Represents a run-time exception due to incorrect usage of the Parallel Iterator. 
 *	
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */
public class ParIteratorIncorrectUsageException extends RuntimeException {

	public ParIteratorIncorrectUsageException(String message) {
		super(message);
	}
}
