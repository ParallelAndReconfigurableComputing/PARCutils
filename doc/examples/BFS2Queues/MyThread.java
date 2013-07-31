package examplesOfPI.BFS2Queues;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * Date Created: 4 May 2009
 * Last Modified: 27 May 2009
 * 
 * Assigns work to each thread.. Each thread runs the run method.. 
 * 
 * @uthor: Lama Akeila
 **/
public class MyThread<E> extends Thread {
	
	private int id;
	private BFSalgorithm treeSearch;
	private ConcurrentLinkedQueue<E> mywork = new ConcurrentLinkedQueue<E>();
	private ConcurrentLinkedQueue<E> nextLevel = new ConcurrentLinkedQueue<E>(); 
	private Iterator it;
	private CountDownLatch latch,waitLatch;
	private boolean processBFS = true;
	
	
	public MyThread(BFSalgorithm<E> treeSearch,int id,Iterator it, ConcurrentLinkedQueue<E> nxtL, CountDownLatch latch, CountDownLatch waitLatch, ConcurrentLinkedQueue<E> mywork) {
		this.treeSearch = treeSearch;
		this.id = id;
		this.nextLevel = nxtL;
		this.it = it;
		this.latch = latch;
		this.waitLatch = waitLatch;
		this.processBFS = true;
		this.mywork = mywork;
		
	}
	
	public void setParam(BFSalgorithm treeSearch,int id,Iterator it, ConcurrentLinkedQueue<E> nxtL, CountDownLatch latch, CountDownLatch waitLatch, ConcurrentLinkedQueue<E> mywork) {
		this.treeSearch = treeSearch;
		this.id = id;
		this.nextLevel = nxtL;
		this.it = it;
		this.latch = latch;
		this.waitLatch = waitLatch;
		this.mywork = mywork;
		
	}

	public int getID() {
		return id;
	}
	
	public void run(){
		while(this.processBFS ){
			waitLatch = new CountDownLatch(1);
			treeSearch.processBFSLevel(mywork, "ss", it);
			latch.countDown();
			while (waitLatch.getCount() > 0) {
				
			}
		}
	}
	
	public void setBFS(boolean value) {
		this.processBFS = value;
	}
	
	public ConcurrentLinkedQueue<E> getDoneList() {
		return mywork;
	}
	

}
