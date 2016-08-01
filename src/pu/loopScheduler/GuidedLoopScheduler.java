package pu.loopScheduler;

/**
 * This class partitions loop iterations using a similar approach to the {@link DynamicLoopScheduler};
 * however the chunk size shrinks as portions of the loop iterations are assigned to the threads. 
 * That is, at a specific time the chunk size assigned to a thread is: <code>remaining-iterations/number-of-threads</code>.
 * </br></br>
 * <b>NOTE:</b> Users can specify minimum chunk size (passed as the chunk-size argument to {@link LoopSchedulerFactory}),
 * to ensure that chunk sizes will not be smaller than the minimum chunk size.
 *
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class GuidedLoopScheduler extends DynamicLoopScheduler {
	private int minimumChunkSize;
	private int numberOfThreads;
	private int loopSize;
	
	
	GuidedLoopScheduler(int loopStart, int loopEnd, int stride, int minimumChunkSize, int numOfThreads, LoopCondition loopCondition) {
		super(loopStart, loopEnd, stride, minimumChunkSize, numOfThreads, loopCondition);
		this.minimumChunkSize = minimumChunkSize;
		this.numberOfThreads = numOfThreads;
		loopSize = getLoopSize();
	}
	
	GuidedLoopScheduler(int loopStart, int loopEnd, int stride, int numOfThreads, LoopCondition loopCondition){
		this(loopStart, loopEnd, stride, 1, numOfThreads, loopCondition);
	}

	private int getLoopSize(){
		int size = Math.abs(this.loopEnd - this.loopStart);
		if(loopCondition.equals(LoopCondition.LessThanOrEqual) || loopCondition.equals(LoopCondition.GreaterThanOrEqual))
			size++;
		return size;
	}
	
	@Override
	public LoopRange getChunk(int threadID) {
		lock.lock();
		if(done){
			lock.unlock();
			return null;
		}
		LoopRange loopRange;
		chunkSize = loopSize / numberOfThreads;
		
		if(chunkSize < minimumChunkSize)
			chunkSize = minimumChunkSize;
		
		if(isAscendingLoop)
			loopRange = ascendingPartitioning();
		else
			loopRange = descendingPartitioning();
		loopSize -= chunkSize;
		if(loopSize <= 0)
			done = true;
		lock.unlock();
		return loopRange;
	}

}
