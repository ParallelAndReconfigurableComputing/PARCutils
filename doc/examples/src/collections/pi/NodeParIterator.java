package collections.pi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import collections.Node;
import pi.ParIteratorAbstract;

/**
 * A Parallel Iterator to traverse a tree.
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */

public class NodeParIterator<E> extends ParIteratorAbstract<Node<E>> {
	
	public static long SLEEP_AMOUNT = 50;
	
	private LinkedBlockingDeque<Node<E>>[] privateQueues;
	private ThreadLocal<Node<E>> reservedNode = new ThreadLocal<Node<E>>();
	private ThreadLocal<Node<E>> lastNodeTaken = new ThreadLocal<Node<E>>();
	private ThreadLocal<Boolean> removeLastNodeTaken = new ThreadLocal<Boolean>() { protected Boolean initialValue() {return false;}};
	
	private AtomicInteger numThreadsStealing = new AtomicInteger(0);
	
	private ConcurrentLinkedQueue<Node<E>> nodesToDelete = new ConcurrentLinkedQueue<Node<E>>();
	
	public NodeParIterator(Node<E> collection, int numOfThreads) {
		super(numOfThreads);
		
		privateQueues = new LinkedBlockingDeque[numOfThreads];
		for (int i = 0; i < numOfThreads; i++) {
			privateQueues[i] = new LinkedBlockingDeque<Node<E>>();
		}
		privateQueues[0].add(collection);		//-- put the root node on since it is ready to be executed 
	}
	
	private void enqueueChildrenNodes(Node<E> finished, int tid) {
		LinkedBlockingDeque<Node<E>> myqueue = privateQueues[tid];
		Iterator<Node<E>> it = finished.getChildren().iterator();
		while(it.hasNext()) {
			myqueue.addLast(it.next());
		}
	}
	
	@Override
	public boolean hasNext() {
		
		int tid = threadID.get();
		
		if (iShouldBreak[tid].get()) {
			
			//-- keep polling until private queue is empty (i.e. other threads have stolen this thread's nodes)
			while (privateQueues[tid].peek() != null) {
				try {
					Thread.sleep(SLEEP_AMOUNT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//-- only now is this thread considered complete 
			numThreadsStealing.incrementAndGet();
			latch.countDown();
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		//-- if still haven't taken the reserved Node, then ignore request
		if (reservedNode.get() != null)
			return true;
		
		if (lastNodeTaken.get() != null) {
			Node<E> finished = lastNodeTaken.get();
			//-- get the children of this node and add to the queue (assuming it hasn't been requested to be removed)
			
			if (removeLastNodeTaken.get()) {
				//-- the last taken node was marked for removal
				
				//-- record this node, to go back and delete it (when all threads reach the latch)
				nodesToDelete.add(finished);
				
				removeLastNodeTaken.set(false);
			} else {
				//-- was not marked for removal, therefore add its children nodes to be processed 
				
				enqueueChildrenNodes(finished, tid);
			}
			lastNodeTaken.set(null);
		}
		
		Node<E> nextNode = null;
		
		//-- check the back of current thread's queue
		LinkedBlockingDeque<Node<E>> myqueue = privateQueues[tid];
		if ((nextNode = myqueue.pollLast())!=null) {
			//-- found a Node
			reservedNode.set(nextNode);
			return true;
		}
		
		//-- the thread now tries to steal from other threads until it finds something, or all the other threads are also trying to steal
		numThreadsStealing.incrementAndGet();
		
//		System.out.println("thread "+tid+" wants to steal ("+numThreadsStealing.get()+")");
		
		while (true) {
			int numStealers = numThreadsStealing.get();
			
			if (numStealers == numOfThreads) {
				//-- all threads have finished!
				
				//-- go back and commit the removed nodes
				Node toDelete = null;
				while ((toDelete = nodesToDelete.poll()) != null) {
					toDelete.deleteFromParent();
				}
				
				latch.countDown();
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}
			
			//-- check the other threads' queues
			for (int i = 0; i < numOfThreads; i++) {
				LinkedBlockingDeque<Node<E>> otherqueue = privateQueues[i];
				if ((nextNode = otherqueue.pollFirst())!=null) {
					//-- found a Node
					numThreadsStealing.decrementAndGet();		//-- not 100% correct, but good enough for now.. (stealing and decrement should be atomic)
					reservedNode.set(nextNode);
					return true;
				}
			}
			
			//-- found nothing.. but maybe another thread will soon add another node.. so try again..
		}
	}

	@Override
	public Node<E> next() {
		if (reservedNode.get() == null)
			throw new NoSuchElementException("Calling next() without hasNext() giving a 'true'");
		
		Node<E> n = reservedNode.get();
		reservedNode.set(null);
		
		lastNodeTaken.set(n);
		return n;
	}
	
	@Override
	public void remove() {
		if (lastNodeTaken.get() == null)
			throw new IllegalStateException("Calling remove() without a matched call to next()");
		
		System.out.println("Will remove "+lastNodeTaken.get());
		removeLastNodeTaken.set(true);
	}
	
	@Override
	protected List<Node<E>> getUnprocessedElements() {
		ArrayList<Node<E>> list =  new ArrayList<Node<E>>();
		if (reservedNode.get() != null) {
			//-- there was a node reserved, but not taken yet
			list.add(reservedNode.get());
		}
		if (lastNodeTaken.get() != null) {
			//-- there was a node taken, therefore assume it has been completed, so enqueue it's children
			
			if (!removeLastNodeTaken.get())
				enqueueChildrenNodes(lastNodeTaken.get(), threadID.get());
		}
		return new ArrayList<Node<E>>();
	}
}
