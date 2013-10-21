package pi.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This thread ID scheme works well with fixed thread number
 * and reusable ParIterator. If the thread number becomes
 * dynamic, a new thread ID scheme should be applied.
 *
 * Author: xiaoxing
 * Date: 4/06/13
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
