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

/**
 * Date created: 16 July 2009 Last modified: 17 July 2009
 * 
 * This class represents a Parallel DFS Iterator which works on Directed Acyclic
 * Graphs (DAGs). It returns nodes in DFS order from bottom to top (i.e. in case
 * of a tree, leaves are returned before top level nodes).
 * 
 * @author Lama Akeila
 */
public class DFSonDAGBottomTop<V> extends ParIteratorAbstract<V> {

	// Stores the object to be retrieved when calling the next method
	private Object[][] buffer;

	// Stores a boolean value for each thread to indicate whether
	// the thread should be assigned with work or not
	private volatile boolean[][] permissionTable;

	private final ReentrantLock lock = new ReentrantLock();

	private CountDownLatch latch;

	// Maps each encountered node to how many successors it has..
	// successors numbers are decremented on the fly once successors
	// get processed
	private volatile ConcurrentHashMap<V, AtomicInteger> successorsMap;

	private int numTreeNodes = 0;

	private AtomicBoolean breakAll = new AtomicBoolean(false);

	private GraphAdapterInterface tree;

	private V root;

	private LinkedBlockingDeque<V> stack;

	private ConcurrentHashMap<Integer, LinkedBlockingDeque<V>> localStack;

	private ThreadLocal<Boolean> fetchLocalStack = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			// initial value of bufferIndex is -1
			return false;
		}
	};

	private AtomicInteger latestThread = new AtomicInteger();

	private AtomicInteger processedNodes = new AtomicInteger();

	public DFSonDAGBottomTop(GraphAdapterInterface tree, V root2,
			int numOfThreads) {
		super(numOfThreads, false);
		this.tree = tree;
		this.root = root2;
		stack = new LinkedBlockingDeque<V>();
		localStack = new ConcurrentHashMap<Integer, LinkedBlockingDeque<V>>();
		stack.push(root);
		buffer = new Object[numOfThreads][1];
		permissionTable = new boolean[numOfThreads][1];
		permissionTable = initializePermissionTable(permissionTable);
		latch = new CountDownLatch(numOfThreads);

		successorsMap = new ConcurrentHashMap<V, AtomicInteger>();
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

	/**
	 * This method returns TRUE if there is still available vertex in the
	 * iterator (i.e. unvisited vertex), else returns FALSE
	 */
	public boolean hasNext() {
		ArrayList<V> successors;
		if (breakAll.get() == false) {

			int id = UniqueThreadIdGenerator.getCurrentThreadId();

			while (permissionTable[id][0] == false) {
				System.currentTimeMillis();

			}

			Iterator<V> it = stack.iterator();

			V currentNode = null;

			if (fetchLocalStack.get()) {
				currentNode = localStack.get(id).peekFirst();
				V nodeToBeProcessed = checkNode(id, currentNode);
				buffer[id][0] = nodeToBeProcessed;
				processedNodes.incrementAndGet();
				if (processedNodes.get() == numTreeNodes) {
					// All Nodes have been traversed
					permissionTable = giveAllPermission(permissionTable);
				}

				return true;

			} else {
				localStack.put(id, new LinkedBlockingDeque<V>());
				lock.lock();
				currentNode = stack.poll();
				lock.unlock();
				if (currentNode != null) {
					localStack.get(id).push(currentNode);
					V nodeToBeProcessed = checkNode(id, currentNode);
					if (nodeToBeProcessed != null) {
						buffer[id][0] = nodeToBeProcessed;
						processedNodes.incrementAndGet();
						if (processedNodes.get() == numTreeNodes) {
							permissionTable = giveAllPermission(permissionTable);
						}

						return true;
					} else {
						latch.countDown();
						try {
							latch.await();
						} catch (InterruptedException e) {
							System.out.println("Interrupted Exception");
						}

						return false;
					}

				} else {
					latch.countDown();
					try {
						latch.await();
					} catch (InterruptedException e) {
						System.out.println("Interrupted Exception");
					}

					return false;
				}

			}
		}
		latch.countDown();
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
		}

		return false;
	}

	private V checkNode(int id, V currentNode) {
		if (successorsMap.containsKey(currentNode)) {
			if (successorsMap.get(currentNode).compareAndSet(0, 0)) {
				if (localStack.get(id).contains(currentNode)) {
					localStack.get(id).remove(currentNode);
				} else {
					if (stack.contains(currentNode))
						stack.remove(currentNode);
				}
				int childrenSize = tree.getChildrenList(currentNode).size();

				if (childrenSize > 0) {
					Iterator<V> parents = tree.getParentsList(currentNode)
							.iterator();

					while (parents.hasNext()) {
						V parnt = parents.next();
						successorsMap.get(parnt).decrementAndGet();

					}
				}
				if (localStack.get(id).isEmpty()) {
					fetchLocalStack.set(false);
				}

				return currentNode;
			} else {
				lock.lock();
				V currentNode2 = stack.poll();
				lock.unlock();
				if (currentNode2 != null) {
					currentNode = currentNode2;
					localStack.get(id).push(currentNode);
				} else {
					while (!successorsMap.get(currentNode).compareAndSet(0, 0)) {

					}
					if (localStack.get(id).contains(currentNode)) {
						localStack.get(id).remove(currentNode);
					} else {
						if (stack.contains(currentNode))
							stack.remove(currentNode);
					}

					if (tree.getParentsList(currentNode).size() > 0) {
						Iterator<V> parents = tree.getParentsList(currentNode)
								.iterator();
						
						while (parents.hasNext()) {
							V parnt = parents.next();
							successorsMap.get(parnt).decrementAndGet();

						}
					}
					if (localStack.get(id).isEmpty()) {
						fetchLocalStack.set(false);
					}
					return currentNode;
				}
			}
		}

		int childrenSize = tree.getChildrenList(currentNode).size();
		if (childrenSize > 0) {

			successorsMap.putIfAbsent(currentNode, new AtomicInteger(
					childrenSize));
			Iterator<V> childNodes = tree.getChildrenList(currentNode)
					.iterator();
			
			int i = 1;
			while (childNodes.hasNext()) {
				V child = childNodes.next();
				if (i == 1) {
					localStack.get(id).push(child);

				} else {
					stack.push(child);
				}
				i++;

			}
			fetchLocalStack.set(true);
			int newIndex = latestThread.incrementAndGet();

			if (newIndex < numOfThreads) {
				permissionTable[newIndex][0] = true;
			}
			if (!localStack.get(id).isEmpty()) {
				V nextNode = localStack.get(id).peekFirst();
				return checkNode(id, nextNode);
			} else {
				return null;
			}

		} else {
			Iterator<V> parents = tree.getParentsList(currentNode).iterator();
			while (parents.hasNext()) {
				V parnt = parents.next();
				successorsMap.get(parnt).decrementAndGet();
			}
			if (localStack.get(id).contains(currentNode)) {
				localStack.get(id).remove(currentNode);
			} else {
				stack.remove(currentNode);
			}
			if (localStack.get(id).isEmpty()) {
				fetchLocalStack.set(false);
			}

			return currentNode;
		}
	}

	private boolean[][] giveAllPermission(boolean[][] permissionTable) {
		for (int i = 0; i < numOfThreads; i++) {
			permissionTable[i][0] = true;

		}
		return permissionTable;

	}

	// This method returns a vertex assigned to a specific thread 
	public V next() {
		int id = UniqueThreadIdGenerator.getCurrentThreadId();
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
	protected List<V> getUnprocessedElements() {
		throw new UnsupportedOperationException(
				"Local break not supported yet for Graphs");
	}

}

