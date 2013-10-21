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
import java.util.concurrent.*;



/**
 * Date created: 9 September 2009 
 * Last modified: 22 September 2009
 * 
 * This class represents a Parallel DFS Iterator which works on Directed Acyclic
 * Graphs (DAGs). It returns nodes in DFS order from top to buttom (i.e. in case
 * of a tree, root nodes are returned before leaves). It supports work-stealing
 * when threads get idle.
 * 
 * @author Lama Akeila
 */
public class DFSonDAGs<V> extends ParIteratorAbstract<V> {

	// Stores the object to be retrieved when calling the next method
	private Object[][] buffer;

	// Maps each thread id to its local stack
	private ConcurrentHashMap<Integer, LinkedBlockingDeque<V>> localStack;

	// Stores a boolean value for each thread to indicate whether
	// the thread should be assigned with work or not
	private volatile boolean[][] permissionTable;

	private CountDownLatch latch;

	private int processedNodesNum = 0;

	private int numTreeNodes = 0;

	private AtomicBoolean breakAll = new AtomicBoolean(false);

	private GraphAdapterInterface graph;
	
	private V root;

	private LinkedBlockingDeque<V> stack;
	
	private ConcurrentLinkedQueue<Integer> targets;
	
	private ConcurrentLinkedQueue<V> processedNodes ;
	
	private ConcurrentLinkedQueue<V> waitingList ;
	
	private AtomicInteger stealingThreads = new AtomicInteger(0);
	
	public DFSonDAGs(GraphAdapterInterface graph,V root, int numOfThreads) {

		super(numOfThreads, false);
		this.graph = graph;
		this.root = root;
		stack = new LinkedBlockingDeque<V>();
		stack.push(root);
		numTreeNodes = graph.verticesSet().size();
		System.out.println("Total Nodes: " + numTreeNodes);
		buffer = new Object[numOfThreads][1];
		permissionTable = new boolean[numOfThreads][1];
		permissionTable = initializePermissionTable(permissionTable);
		processedNodes = new ConcurrentLinkedQueue<V>();
		waitingList = new ConcurrentLinkedQueue<V>();
		localStack = new ConcurrentHashMap<Integer, LinkedBlockingDeque<V>>();
		for (int i = 0; i < numOfThreads; i++) {
			localStack.put(i, new LinkedBlockingDeque<V>());
			if (i == 0)
				localStack.get(0).push(root);
		}
		latch = new CountDownLatch(numOfThreads);
		this.targets = new ConcurrentLinkedQueue<Integer>();
	}

	// Gives permission to the first thread
	private boolean[][] initializePermissionTable(boolean[][] permissionTable) {

		for (int i = 0; i < numOfThreads; i++) {
			if (i == 0) {
				permissionTable[0][0] = true;
			} else {
				permissionTable[i][0] = false;
			}
		}
		return permissionTable;
	}

	
	// This method returns TRUE if there is still available vertex in the
    // iterator (i.e. unvisited vertex), else returns FALSE
	public boolean hasNext() {
		int id = threadID.get();

		ArrayList<GraphAdapterInterface> successors;
		if (breakAll.get() == false) {
			
			// Retrieve node from local stack and store it in buffer
			V node = getLocalNode();
			if (node != null) {
				buffer[id][0] = node;
				processedNodes.add(node);
				Iterator<V> it = graph.getChildrenList(node).iterator();
				// push the successors into the local stack
				while (it.hasNext()) {
					V nextNode =  it.next();
					localStack.get(id).addLast(nextNode);
				}
				return true;
			} else {
				// when local stack is empty, start from a new place (Steal Work)..
				stealingThreads.incrementAndGet();
				while (true) {
					if (stealingThreads.get() == numOfThreads) {
						// Exit if all the threads are attempting to steal
						exit(latch);
						return false;
					} else {
						// Work Stealing..
						V stolenNode = null;
						for (int i = 0; i < numOfThreads; i++) {
							stolenNode = stealNode(i);
							if (stolenNode != null){
								stealingThreads.decrementAndGet();
								break;
							}
						}
						if (stolenNode != null) {
							buffer[id][0] = stolenNode;
							processedNodes.add(stolenNode);
							Iterator<V> successorsIt = graph.getChildrenList(stolenNode).iterator();
							// push the successors into the local stack
							while (successorsIt.hasNext()) {
								V nextNode = successorsIt
										.next();
								localStack.get(id).addLast(nextNode);
							}
							return true;
						}

					}
				} // end of while loop
			}
		}
		exit(latch);
		return false;
	}
	
	// Returns a node from the stack of the target
	private V stealNode(int target) {
		int id = threadID.get();
		V currentStackNode =  localStack.get(target).pollLast();
		if(currentStackNode != null){
			// checks that all the parents of the node are processed, if not
			// add it to waiting list and call method again of the target
			if(processedNodes.containsAll(graph.getParentsList(currentStackNode)) && !processedNodes.contains(currentStackNode)){
				return currentStackNode;
			}else if(!processedNodes.containsAll(graph.getParentsList(currentStackNode)) && !processedNodes.contains(currentStackNode)){
				waitingList.add(currentStackNode);
				return stealNode(target);
			}else{
				return stealNode(target);
			}
		} else{
			// returns null when no nodes are available in the target's stack
			return currentStackNode;
		}
	
	}
	
	// Returns a node from the local stack of the thread
	private V getLocalNode() {
		int id = threadID.get();
		V currentStackNode =  localStack.get(id).pollLast();
		// checks that all the parents of the node are processed, if not
		// add it to waiting list and call method again of the target
		if(currentStackNode != null){
			if(processedNodes.containsAll(graph.getParentsList(currentStackNode)) && !processedNodes.contains(currentStackNode)){
				return currentStackNode;
			}else{
				waitingList.add(currentStackNode);
				return getLocalNode();
			}
		}else{
			return currentStackNode;
		}
	
	}

	// Threads call this method to exit
	private void exit(CountDownLatch latch) {
		int id = threadID.get();
	    if (processedNodesNum >= numTreeNodes) {
			// All Nodes have been traversed
		    permissionTable = giveAllPermission(permissionTable);
		}
		latch.countDown();
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
		}
	}

	// Assigns a true value to all the threads in order to do work
	private boolean[][] giveAllPermission(boolean[][] permissionTable) {
		for (int i = 0; i < numOfThreads; i++) {
			permissionTable[i][0] = true;
		}
		return permissionTable;
	}

	
	// This method returns the node assigned to a specific thread 
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
}
