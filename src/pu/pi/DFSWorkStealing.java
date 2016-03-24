/*
 *  Copyright (C) 2009 Lama Akeila, Oliver Sinnen, Nasser Giacaman
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Date created: 29 April 2009 
 * Last modified: 30 April 2009
 * 
 * This class represents a Parallel DFS Iterator which is implemented with 
 * a local stack for each thread. It supports work-stealing when threads get idle.
 * 
 * @author Lama Akeila
 */
public class DFSWorkStealing<V> extends ParIteratorAbstract<V> {

	// Stores the object to be retrieved when calling the next method
	private Object[][] buffer;

	// Maps each thread id to its local stack
	private ConcurrentHashMap<Integer, LinkedBlockingDeque<V>> localStack;

	

	private final ReentrantLock lock = new ReentrantLock();

	private CountDownLatch latch;

	private int numTreeNodes = 0;

	private AtomicBoolean breakAll = new AtomicBoolean(false);

	private V root;

	private GraphAdapterInterface tree;

	private AtomicInteger stealingThreads = new AtomicInteger(0);

	
	public DFSWorkStealing(GraphAdapterInterface tree, V root, int numOfThreads) {

		super(numOfThreads, false);
		this.tree = tree;
		this.root = root;
		numTreeNodes = tree.verticesSet().size() + 1;
		buffer = new Object[numOfThreads][1];
		localStack = new ConcurrentHashMap<Integer, LinkedBlockingDeque<V>>();
		for (int i = 0; i < numOfThreads; i++) {
			localStack.put(i, new LinkedBlockingDeque<V>());
			if (i == 0)
				localStack.get(0).push(root);
		}

		latch = new CountDownLatch(numOfThreads);

	}

	// This method returns TRUE if there is still available vertex in the
	// iterator (i.e. unvisited vertex), else returns FALSE
	public boolean hasNext() {
		int id = threadID.get();

		ArrayList<GraphAdapterInterface> successors;
		if (breakAll.get() == false) {
			V node = localStack.get(id).pollLast();
			if (node != null) {
				buffer[id][0] = node;
				Iterator it = tree.getChildrenList(node).iterator();
				// push the successors into the local stack
				while (it.hasNext()) {
					V nextNode = (V) it.next();
					localStack.get(id).addLast(nextNode);
				}
		
				return true;
				
			} else {

				stealingThreads.incrementAndGet();
				while (true) {
					if (stealingThreads.get() == numOfThreads) {
						exit(latch);
						return false;
					} else {
						// Work Stealing..
						V stolenNode = null;
						for (int i = 0; i < numOfThreads; i++) {
							stolenNode = localStack.get(i).pollLast();
							if (stolenNode != null) {
								stealingThreads.decrementAndGet();
								break;
							}
						}
						if (stolenNode != null) {
							buffer[id][0] = stolenNode;
							Iterator successorsIt = tree.getChildrenList(
									stolenNode).iterator();
							// push the successors into the local stack
							while (successorsIt.hasNext()) {
								V nextNode = (V) successorsIt.next();
								localStack.get(id).addLast(nextNode);
							}
							return true;
						}

					}
				}
			}
		}
		exit(latch);
		return false;
	}

	private void exit(CountDownLatch latch) {
		latch.countDown();
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
		}
	}


	// This method returns a vertex assigned to a specific thread 
	public V next() {
		int id = threadID.get();
		return (V) buffer[id][0];
	}

	@Override
	public void globalBreak() {
		System.out.println("BreakAll called by thread");
		breakAll.set(true);
	}

	@Override
	public boolean localBreak() {
		throw new UnsupportedOperationException(
				"Local break not supported yet for Graphs");
	}

	@Override
	public void forEachRemaining(Consumer<? super V> action) {
		// TODO Auto-generated method stub
		/*added to suppress compiler errors*/
		return;
	}

}

