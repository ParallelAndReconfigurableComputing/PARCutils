package pu.loopScheduler;

/**
 * An interface that is implemented by every LoopScheduler instance
 * in this library. Every sub-class must implement the {@link #getChunk()}
 * method, that depending on the scheduling policy partitions and assigns
 * loop iterations to threads that are iterating over a loop in parallel.
 *
 * @author Mostafa Mehrabi
 * @since  2016
 */
public interface LoopScheduler {
	public LoopRange getChunk(int threadID);
}
