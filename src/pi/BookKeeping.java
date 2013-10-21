/*
 *  Copyright (C) 2009 Lama Akeila, Wafaa Humadi, Oliver Sinnen
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

import pi.util.ThreadID;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.*;


/**
 * Date created: 	1 May 2008
 * Last modified:	7 September 2008
 * 
 * @author Lama Akeila 
 * @author Wafaa Humadi
 * 
 * Purpose: The implementation of the Bookkepping mechanism.. It keeps track of the colours of the 
 * nodes during the runtime of the graph algorithm
 */

public class BookKeeping<V> {

	private Collection vertices;
	
	private int numOfThreads;
	
	private AtomicBoolean breakAll = new AtomicBoolean (false);

	private ConcurrentHashMap<Object, Integer> visitedList;
	
	private ConcurrentHashMap<Object, Integer> blackList,greyList;

	private ThreadLocal<Integer> colour = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			// initial value of bufferIndex is -1
			return 0;
		}
	};

	/**
	 * Constructor
	 * @param vertices
	 * @param nThreads
	 */
	public BookKeeping(Collection vertices, int nThreads) {
		this.visitedList = new ConcurrentHashMap(vertices.size());
		this.numOfThreads = nThreads;
		this.blackList = new ConcurrentHashMap<Object, Integer>();
		this.greyList = new ConcurrentHashMap<Object, Integer>();


	}

	/**
	 * This method test if the current vertex is taken or not. It returns false
	 * if its taken * and returns true and mark it as taken (removes it form the
	 * list) otherwise
	 */
	public boolean removeIfNotTaken(Object o) {
		
		if(breakAll.get() == false){
			int id = ThreadID.getStaticID();
			Object test = markIfNotAlreadyMarked(o);
			if (test == null) {
				return true; // success
			}
	
			return false; // already taken
		} else {
			return false;
		}
	}

	public boolean containsVertex(Object vertex) {
		return visitedList.containsKey(vertex);
	}
	
	public boolean greyListcontainsVertex(Object vertex) {
		return greyList.contains(vertex);
	}
	
	public boolean blackListcontainsVertex(Object vertex) {
		return blackList.contains(vertex);
	}

	public void markVisited(Object obj, int threadID, int iterations) {
		colour.set(generateUniqueColour(iterations));
		visitedList.put(obj, colour.get());
	}
	
	public void markBlack(Object obj) {
		blackList.putIfAbsent(obj, 0);
	}
	
	public void markGrey(Object obj) {
		greyList.putIfAbsent(obj, 0);
	}

	public Object markIfNotAlreadyMarked(Object obj) {
		return visitedList.putIfAbsent(obj, colour.get());

	}

	public int getVertexColor(Object o) {
		//Vertex v = (Vertex) o;
		return visitedList.get(o);
	}

	public int getColour() {
		return colour.get();
	}

	public void setColour(Object v, int col) {
		visitedList.put(v, col);
	}
	
	public int generateUniqueColour(int iterationsCount) {
		int id = ThreadID.getStaticID();
		int x = (iterationsCount * numOfThreads) + id;
		return x;
	}
	
	public void setBreakAll(boolean b) {
		breakAll.set(b);
		
	}
}

