package pi;

import pi.exceptions.PIExceptionHelper;
import pi.exceptions.ParIteratorException;
import pi.util.Flags;
import pi.util.TLocal;
import pi.util.ThreadID;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: xiaoxing
 * Date: 31/05/13
 */
public abstract class ParIteratorAbstract<E> implements ParIterator<E> {

	protected int chunkSize;
	final protected int numOfThreads;

	protected CountDownLatch latch = null;

	protected boolean ignoreBarrier = false;

	final protected ThreadID threadID;

	protected List<E> data;

	protected Collection<E> collection;

	final protected ConcurrentLinkedQueue<Iterator<E>> reclaimedElements = new ConcurrentLinkedQueue<Iterator<E>>();

	/*
	 * the following variables are used for the purpose of exception handling
	 */
	//-- stores all the registered exceptions that occurred during traversal
	protected ConcurrentLinkedQueue<ParIteratorException<E>> exceptions = new ConcurrentLinkedQueue<ParIteratorException<E>>();

	//-- used for breaks, each thread checks their position
	final protected Flags flags;

	private ReentrantLock localBreakLock = new ReentrantLock();

	public ParIteratorAbstract(Collection<E> collection, int chunkSize, int numOfThreads, boolean ignoreBarrier) {
		this(numOfThreads, ignoreBarrier);
		this.chunkSize = chunkSize;
		data = null;
		this.collection = collection;

	}

	public ParIteratorAbstract(int numOfThreads, boolean ignoreBarrier) {
		this.numOfThreads = numOfThreads;
		threadID = new ThreadID(numOfThreads);

		flags = new Flags(threadID);
		latch = new CountDownLatch(numOfThreads);
		this.ignoreBarrier = ignoreBarrier;

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("This Parallel Iterator currently does not support remove()");
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException("Currently not supported.");
	}

	@Override
	public void globalBreak() {
		localBreakLock.lock();
		for (int i = 0; i < numOfThreads; i++)
			flags.flaggedWith(Flags.Flag.BREAK);
		localBreakLock.unlock();
	}

//	@Override
//	public boolean localBreak() {
//		int tid = threadID.get();
//		localBreakLock.lock();
//
//		if (allOtherThreadsHaveLocalBreaked(tid)) {
//			localBreakLock.unlock();
//			return false;
//		} else {
//			reclaimedElements.add(currentIterator.get());
//			flags.set(Flags.Flag.BREAK);
//			localBreakLock.unlock();
//			return true;
//		}
//	}

	protected boolean allOtherThreadsHaveLocalBreaked(int tid) {
		boolean allBreaked = true;
		for (int i = 0; i < numOfThreads; i++) {
//			if (i != tid)
//				allBreaked &= iShouldBreak.get().booleanValue();
		}
		return allBreaked;
	}


	@Override
	public void register(Exception e) {
		register(e, null);
	}

	public void register(Exception e, E currentElements) {
		Thread regThread = Thread.currentThread();
		ParIteratorException<E> pie = PIExceptionHelper.createException(e, currentElements, regThread);
		exceptions.add(pie);
	}

	@Override
	public ParIteratorException<E>[] getAllExceptions() {
		return exceptions.toArray(new ParIteratorException[] {});
	}

	// for test
	public int getID() {
		// TODO the thread ID thing need to be redesigned because of the reset feature.
		// here is a temporary solution.
		return threadID.get() % numOfThreads;
	}
}
