package examplesOfPI.BFS2Queues;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import examplesOfPI.benchmarks.*;
import pi.*;
import pi.ParIterator.Schedule;


/**
 * 
 * Date created: 9 July 2009 
 * Last modified: 10 July 2009
 * 
 * This class provides a sequential and parallel implementation of the
 * Breadth-First Search (BFS) Algorithm..
 * 
 * @author Lama Akeila
 */
public class BFSalgorithm<E> {

	private ParIterator pi;
	
	private GraphAdapter tree;
	
	private E root;
	
	private GraphAdapter currentNode;
	
	private int numThreads;
	
	private int width = 300;

	private int height = 300;

	private double centerY = -0.0395159f;

	private double centerX = -0.637011f;

	private double pixmapScale = 0.00403897f;

	private int MaxIterations = 1;

	CountDownLatch latch, waitLatch = null;
	
	private boolean swtch = false;
	
	private NewtonChaos newton = new NewtonChaos();

	private ConcurrentLinkedQueue<E> nextLevel = new ConcurrentLinkedQueue<E>();

	private ConcurrentLinkedQueue<E> readList, writeList,visitedNodes, list1, list2;
	// constructor
	public BFSalgorithm(GraphAdapter tree,E root, int numOfThreads) {
		list1 = new ConcurrentLinkedQueue<E>();
		list2 = new ConcurrentLinkedQueue<E>();
		visitedNodes = new ConcurrentLinkedQueue<E>();
		list1.add(root);
		System.out.println("Connstruction list: "+ list1.size());
		this.tree = tree;
		this.root = root;
		this.pi = pi;
		this.currentNode = tree;
		this.numThreads = numOfThreads;
		this.latch = new CountDownLatch(numOfThreads);
		this.waitLatch = new CountDownLatch(1);
	}

	public BFSalgorithm(GraphAdapter tree, E root) {
		this.tree = tree;
		this.root = root;
	}

	/***************************************************************************
	 * The Sequential version of Breadth-First Search
	 **************************************************************************/
	public void seqBFS(E root,ArrayList<E> mywork, Object target) {
		//GraphAdapter root = tree;
		ArrayList<E> successors = new ArrayList<E>();
		successors.add(root);
		// Processing successors and Updating the successors list to the next
		// level
		while (!successors.isEmpty()) {
			// System.out.println("Not Empty and size is: "+ successors.size());
			ArrayList<E> nextL = new ArrayList<E>();
			Iterator<E> it = successors.iterator();
			while (it.hasNext()) {
				E curNode = it.next();
				// System.out.println("Processing: "+
				// curNode.getElement().toString());
				nextL.addAll(tree.getChildrenList(curNode));
				newton.performNewton(tree.getVal(curNode));
				if (foundTreeTargetSeq(curNode, target, null, mywork)) {
					break;
				}

			}
			successors = nextL;
		}
	}

	/***************************************************************************
	 * The parallel version of Breadth-First Search
	 **************************************************************************/

	public ConcurrentLinkedQueue<E> parallelBFS(E root,
			ConcurrentLinkedQueue<E> mywork, Object target) {
		int chunk = 1;
		System.out.println("Parallel BFS is called and numThread is: "
				+ numThreads+ " ,Chunksize is Static: "+ chunk);
		//GraphAdapter root = tree;
		ArrayList<E> successors = new ArrayList<E>();
		successors.add(root);

		latch = new CountDownLatch(numThreads);

		nextLevel = new ConcurrentLinkedQueue<E>();
		readList = list1;
		System.out.println("ReadList: "+ list1.size());
		writeList = list2;

		MyThread threads[] = new MyThread[numThreads];
		int iteration = 0;
		Schedule schedule = Schedule.GUIDED;
		Iterator<E> it = ParIteratorFactory.createParIterator(
				readList, numThreads, schedule);
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new MyThread(this, i, it, this.nextLevel, latch,
					waitLatch, mywork);
			threads[i].start();
		}

		// Processing successors and Updating the
		// successors list to the next level in parallel
		while (!readList.isEmpty()) {
			//System.out.println("Iteration "+ iteration);
			//System.out.println(iteration+ " list1: "+list1.size()+" list2:"+list2.size());
			
			if (iteration == 0) {
				try {
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			} else {
				latch = new CountDownLatch(numThreads);
				waitLatch = new CountDownLatch(1);
				it = ParIteratorFactory.createParIterator(readList,
						numThreads, schedule);
				for (int i = 0; i < numThreads; i++) {
					threads[i].setParam(this, i, it, this.nextLevel, latch,
							waitLatch, mywork);
				}
				waitLatch.countDown();
				// Main thread waits for the working threads to finish
				// processing the BFS level
				try {
					// System.out.println("Main thread is waiting");
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			}
			swtch = !swtch;
			if(!swtch){
				readList = list1;
				list2 = new ConcurrentLinkedQueue<E>();
				writeList = list2;
			}else{
				readList = list2;
				list1 = new ConcurrentLinkedQueue<E>();
				writeList = list1;
			}
			//successors = new ArrayList<GraphAdapter>();
			//successors.addAll(nextLevel);
			//nextLevel = new ConcurrentLinkedQueue<GraphAdapter>();
			iteration++;
		}
		for (int t = 0; t < threads.length; t++) {
			threads[t].setBFS(false);
			threads[t].setParam(this, t, it, this.nextLevel, latch, waitLatch,
					mywork);
		}
		waitLatch.countDown();
		return mywork;

	}

	public void processBFSLevel(
			ConcurrentLinkedQueue<E> mywork, Object target,
			Iterator<E> it) {
		while (it.hasNext()) {
			E curNode = it.next();
			if (!visitedNodes.contains(curNode)){
				this.writeList.addAll(tree.getChildrenList(curNode));
				newton.performNewton(tree.getVal(curNode));
				if (foundTreeTarget(curNode, target, null, mywork)) {
					break;
				}
			}
		}
	}

	// Processing done at each node during the depth-first search
	private boolean foundTreeTarget(E v, Object target, MyThread t,
			ConcurrentLinkedQueue<E> mywork) {

		//long start = System.currentTimeMillis();
		mywork.add(v);
		// System.out.println("Loop Time: " + (end - start));
		return (v.toString().equals(target.toString()));

	}

	private boolean foundTreeTargetSeq(E v, Object target,
			MyThread t, ArrayList<E> mywork) {

		//long start = System.currentTimeMillis();
		mywork.add(v);
		
		return (v.toString().equals(target.toString()));

	}

}

