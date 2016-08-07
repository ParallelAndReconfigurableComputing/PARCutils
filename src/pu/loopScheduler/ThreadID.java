package pu.loopScheduler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadID assigns threads with a relative ID with respect to 
 * the group of threads that are running by a framework. An instance
 * of ThreadID works well with fixed number of threads and reusable
 * ParIterator. If the number of threads is meant to change dynamically, 
 * a new instance of ThreadID must be created with each change.
 * </br></br> 
 * This class is used by threads other than ParaTask worker threads,
 * when they tend to apply for their own loop partitions from a loop
 * scheduler. Each thread needs to provide its own relative ID in order
 * to acquire the corresponding data chunk. This is only used by the 
 * static loop scheduler, but the interface forces that for dynamic and
 * guided loop schedulers as well. 
 * @see #get()
 * @author xiaoxing
 * @author Mostafa Mehraib
 * @author Nasser Giacaman
 * @since  2013
 */
public class ThreadID {

	final private AtomicInteger nextID = new AtomicInteger(0);
	final private ThreadLocal<Integer> threadID = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return nextID.getAndIncrement();
		}
	};

	final private int threadNum;

	public ThreadID(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getThreadNum() {
		return threadNum;
	}

	/**
	 * Each thread must call this method on an instance of ThreadID, in order to 
	 * acquire its relative ID with respect to the rest of the threads. 
	 * 
	 * @return int: the relative ID for a thread.
	 */
	public int get() {
		return threadID.get() % threadNum;
	}

	// Static Thread ID
	private static AtomicInteger nextStaticID = new AtomicInteger(0);

	private static ThreadLocal<Integer> staticID = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return nextStaticID.getAndIncrement();
		}
	};

	public static int getStaticID() {
		return staticID.get();
	}

}
