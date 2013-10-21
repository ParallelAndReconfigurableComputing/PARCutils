package pi;

import pi.collect.Lists;
import pi.util.Flags;
import pi.util.TLocal;

import java.util.*;

/**
 * Author: xiaoxing
 * Date: 31/05/13
 */
public class StaticParIterator<E> extends ParIteratorAbstract<E> {

	final protected List<E> data;
	final protected List<List<E>> chunks;
	final protected TLocal<List<List<E>>> localChunks;

	final protected TLocal<Iterator<List<E>>> localChunkIterator;
	final protected TLocal<Iterator<E>> localIterator;

	public StaticParIterator(Collection<E> collection, int chunkSize, int numOfThreads, boolean ignoreBarrier) {
		super(collection, chunkSize, numOfThreads, ignoreBarrier);
		data = formatData(collection);
		chunks = Lists.partition(data, chunkSize);
		localChunks = new TLocal<List<List<E>>>(threadID);
		for (int i = 0; i < chunks.size(); i++) {
			int tid = i % numOfThreads;
			if (localChunks.get(tid) == null) {
				localChunks.set(tid, new ArrayList<List<E>>());
			}
			localChunks.get(tid).add(chunks.get(i));
		}

		localChunkIterator = new TLocal<Iterator<List<E>>>(threadID, null);
		localIterator = new TLocal<Iterator<E>>(threadID, null);
	}

	protected List<E> formatData(Collection<E> collection) {
		List<E> data;
		if (collection instanceof RandomAccess) {
			data = (List<E>) collection;
		} else {
			data = new ArrayList<E>(collection);
		}
		return data;
	}

	@Override
	public boolean hasNext() {
		if (flags.flagged()) {
			if (flags.flaggedWith(Flags.Flag.BREAK)) {

				if (ignoreBarrier)
					return false;

				latch.countDown();
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}

			if (flags.flaggedWith(Flags.Flag.RESET)) {
				return false;
			}
		}

		if (localChunkIterator.get() == null) {
			localChunkIterator.set(localChunks.get().iterator());
		}

		if (localIterator.get() == null) {
			if (localChunkIterator.get().hasNext()) {
				localIterator.set(localChunkIterator.get().next().iterator());
			} else {
				return false;
			}
		}

		if (!localIterator.get().hasNext()) {
			if (!localChunkIterator.get().hasNext()) {
				if (reclaimedElements.isEmpty()) {
					return false;
				} else {
					localIterator.set(reclaimedElements.poll());
				}
			} else {
				localIterator.set(localChunkIterator.get().next().iterator());
			}
		}

		return localIterator.get().hasNext();
	}

	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		return localIterator.get().next();
	}

	@Override
	public boolean localBreak() {
		flags.set(Flags.Flag.BREAK);
		reclaimedElements.add(localIterator.get());
		while (localChunkIterator.get().hasNext()) {
			reclaimedElements.add(localChunkIterator.get().next().iterator());
		}
		return true;
	}

	@Override
	public void reset() {
		flags.setAll(Flags.Flag.RESET);
		reclaimedElements.clear();
		localChunkIterator.setAll(null);
		localIterator.setAll(null);
		flags.resetAll();
	}
}
