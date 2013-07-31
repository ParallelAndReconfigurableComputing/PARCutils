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

/**
 * This class generates a unique id for the thread accessing it.
 * 
 * @author Nasser Giacaman
 * @author Lama Akeila
 * @author Oliver Sinnen
 * 
 */

public class UniqueThreadIdGenerator {
    // The next serial number to be assigned
    private static int nextSerialNum = 0;

    private static ThreadLocal<Integer> serialNum = new ThreadLocal<Integer>() {
        protected synchronized Integer initialValue() {
            return new Integer(nextSerialNum++);
        }
    };
    
    public static int getCurrentThreadId() {
        return ((Integer) (serialNum.get())).intValue();
    }
}
