package collections.pi;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pi.ParIteratorAbstract;
import pi.UniqueThreadIdGenerator;

/**
 * A Parallel Iterator to traverse a DOM Document.
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */

public class DomParIterator extends ParIteratorAbstract<Node> {
	
	public static long SLEEP_AMOUNT = 50;
	private Document document;
	private LinkedBlockingDeque<Node>[] privateQueues;
	private ThreadLocal<Node> reservedNode = new ThreadLocal<Node>();
	private ThreadLocal<Node> lastNodeTaken = new ThreadLocal<Node>();
	
	private AtomicInteger numThreadsStealing = new AtomicInteger(0);
	private Lock lockDocument = new ReentrantLock();
	
	//-- TODO finer grained locking, lock only the current element... need to think carefully when to assigned/de-assigned the locks
//	private LinkedBlockingQueue<Lock> lockPool;	
	
	public DomParIterator(Document document, int numOfThreads) {
		super(numOfThreads);
		this.document = document;
		
		privateQueues = new LinkedBlockingDeque[numOfThreads];
		for (int i = 0; i < numOfThreads; i++) {
			privateQueues[i] = new LinkedBlockingDeque<Node>();
		}
		privateQueues[0].add(document.getDocumentElement());		//-- put the root node on since it is ready to be executed 
	}
	
	private void enqueueChildrenNodes(Node finished, int tid) {
		LinkedBlockingDeque<Node> myqueue = privateQueues[tid];
		
		lockDocument.lock();
		
		NodeList children = finished.getChildNodes();
		int numChildren = children.getLength();
		
		for (int i = 0; i < numChildren; i++) {
			myqueue.addLast(children.item(i));	// TODO this was also geting NPE inside the BlockingLL implementation
			
			// seems as if the children collection modifies during this time..
			// maybe want to fine-grain lock, ie lock the parent node 
			
		}
		lockDocument.unlock();
	}
	
	@Override
	public boolean hasNext() {
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		
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
			Node finished = lastNodeTaken.get();
			//-- get the children of this node and add to the queue
			
			enqueueChildrenNodes(finished, tid);
			lastNodeTaken.set(null);
		}
		
		Node nextNode = null;
		
		//-- check the back of current thread's queue
		LinkedBlockingDeque<Node> myqueue = privateQueues[tid];
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
				LinkedBlockingDeque<Node> otherqueue = privateQueues[i];
				
//				imAddingChildren[i].lock();
				
				if ((nextNode = otherqueue.pollFirst())!=null) {
					//-- found a Node
					
//					imAddingChildren[i].unlock();
					numThreadsStealing.decrementAndGet();		//-- not 100% correct, but good enough for now.. (stealing and decrement should be atomic)
					reservedNode.set(nextNode);
					return true;
				}
//				imAddingChildren[i].unlock();
			}
			
			//-- found nothing.. but maybe another thread will soon add another node.. so try again..
		}
	}

	@Override
	public Node next() {
		if (reservedNode.get() == null)
			throw new NoSuchElementException("Calling next() without hasNext() giving a 'true'");
		
		Node n = reservedNode.get();
		reservedNode.set(null);
		
		lastNodeTaken.set(n);
		return n;
	}
	
	public Node replace(Node newNode) {
		Node oldNode = lastNodeTaken.get();
		
		if (oldNode == null)
			throw new IllegalStateException("Calling replace() without a matched call to next()");
		
		lastNodeTaken.set(newNode);			//-- set the last node as the new node

		Node parent = oldNode.getParentNode();
		lockDocument.lock();
		
//		Node parent = null;
//		try {
		Node ret = parent.replaceChild(newNode, oldNode);
		
//	} catch (Exception e) {
//		System.err.println(parent);
//		System.err.println(oldNode);
//		System.err.println(newNode);
//		System.err.println(ret);
//		System.err.println(((Element)oldNode).getAttribute("id"));
//		e.printStackTrace();
//	}
		lockDocument.unlock();
		return ret;
	}
	
	@Override
	public void remove() {
		Node toDelete = lastNodeTaken.get();
		
		if (toDelete == null)
			throw new IllegalStateException("Calling remove() without a matched call to next()");
		
//		System.out.println("Will remove "+toDelete);
		
		Node parent = toDelete.getParentNode();
		
		lockDocument.lock();
		
		if (parent != null)
			parent.removeChild(toDelete);
		else
			document.removeChild(toDelete);
		
		lockDocument.unlock();
		lastNodeTaken.set(null);
	}
	
	@Override
	protected List<Node> getUnprocessedElements() {
		ArrayList<Node> list =  new ArrayList<Node>();
		if (reservedNode.get() != null) {
			//-- there was a node reserved, but not taken yet
			list.add(reservedNode.get());
		}
		if (lastNodeTaken.get() != null) {
			//-- there was a node taken, therefore assume it has been completed, so enqueue it's children
			enqueueChildrenNodes(lastNodeTaken.get(), UniqueThreadIdGenerator.getCurrentThreadId());
		}
		return list;
	}
}
