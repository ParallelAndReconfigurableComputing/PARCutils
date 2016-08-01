package pu.loopScheduler;

import java.util.concurrent.locks.ReentrantLock;

/**
 * This class splits loop iterations into constant chunk sizes depending on the number of threads
 * that are given to the constructor. Furthermore, the threads dynamically acquire chunks for 
 * iterating a loop in parallel. Unless a chunk size is defined by the user, the default chunk
 * size is set to: <code>loop-size/number-of-threads</code>
 * </br></br>
 * This class is mainly designed for parallelizing iterations over a <code>for loop</code>
 * among multiple threads, where they do not need to compete over loop iterators, and are specifically
 * granted their own chunk of iterators to process. 
 * </br></br>
 * For example, if a <code>for loop</code> has one hundred iterations, and four threads are going to 
 * iterate through the loop, each of them get 25 iterations to process separately. Threads acquire
 * their chunks by invoking method {@link #getChunk()} on an object of this class. The method 
 * returns an object of {@link LoopRange}, where user acquires <code>loopStart</code>
 * and <code>loopEnd</code> attributes that specify the start and end of iteration for a thread. 
 * Threads must continue invoking the {@link #getChunk()} method, until the method returns
 * <code>null</code>, which indicates that the end of iterations is reached. 
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class DynamicLoopScheduler extends AbstractLoopScheduler{

	protected boolean done;
	protected ReentrantLock lock;
		
	DynamicLoopScheduler(int loopStart, int loopEnd, int stride, int chunkSize, int numOfThreads, LoopCondition loopCondition){
		this.done = false;
		this.loopStart = loopStart;
		this.mainLoopStart = loopStart;
		this.loopEnd = loopEnd;
		this.stride = Math.abs(stride);
		this.lock = new ReentrantLock();
		this.loopCondition = loopCondition;
		this.chunkSize = decideChunkSize(chunkSize, numOfThreads);
		this.isAscendingLoop = isAscendingLoop();
	}
	
	//remember to make constructors package visible, and provide a factory class. 
	DynamicLoopScheduler(int loopStart, int loopEnd, int stride, int numOfThreads, LoopCondition loopCondition){
		this(loopStart, loopEnd, stride, 0, numOfThreads, loopCondition);
	}
	
	
	
	@Override
	public LoopRange getChunk(int threadID){
		lock.lock();
		if(done){
			lock.unlock();
			return null;
		}
		LoopRange loopRange;
		if(isAscendingLoop)
			loopRange = ascendingPartitioning();
		else
			loopRange = descendingPartitioning();
		lock.unlock();
		return loopRange;
	}
	
	protected LoopRange ascendingPartitioning(){
		if(loopCondition.equals(LoopCondition.GreaterThan) || loopCondition.equals(LoopCondition.GreaterThanOrEqual)){
			done = true;
			lock.unlock();
			throw new IllegalArgumentException("\n\nINVALID LOOP CONDITION FOR THIS LOOP INTERVAL \n "
					+ "LOOP IS ASCENDING, CONDITION CAN BE EITHER \"LessThan\" OR \"LessThanOrEqual\" \n");
		}
		
		int lowerBound;
		int offset = loopStart - mainLoopStart;
		int remainder = offset%stride;
		
		if(remainder != 0)
			lowerBound = (loopStart + (stride - remainder));
		else
			lowerBound = loopStart;
		
		int upperBound = (loopStart + chunkSize);
		if(loopCondition == LoopCondition.LessThanOrEqual)
			upperBound -= 1;
		
		if(lowerBound >= loopEnd){
			done = true;
			return null;
		}
		
		if(upperBound > loopEnd){
			upperBound = loopEnd;
			done = true;
		}
		
		loopStart += chunkSize;
		
		LoopRange loopRange = new LoopRange(chunkSize);
		loopRange.loopStart = lowerBound;
		loopRange.loopEnd = upperBound;
		loopRange.localStride = this.stride;
		
		return loopRange;
	}
	
	protected LoopRange descendingPartitioning(){
		
		if(loopCondition.equals(LoopCondition.LessThan) || loopCondition.equals(LoopCondition.LessThanOrEqual)){
			done = true;
			lock.unlock();
			throw new IllegalArgumentException("\n\nINVALID LOOP CONDITION FOR THIS LOOP INTERVAL \n"
					+ "LOOP IS DESCENDING, CONDITION CAN BE EITHER \"GreaterThan\" OR \"GreaterThanOrEqual\" \n");
		}
		
		int offset = mainLoopStart - loopStart;
		int remainder = offset%stride;
		int upperBound;
		if (remainder != 0)
			upperBound = loopStart - (stride - remainder); 
		else 
			upperBound = loopStart;

		int lowerBound = loopStart - chunkSize;
		if(loopCondition == LoopCondition.GreaterThanOrEqual)
			lowerBound += 1;
		
		if(upperBound <= loopEnd){
			done = true;
			return null;
		}
		
		if (lowerBound < loopEnd){
			lowerBound = loopEnd;
			done = true;
		}
		
		loopStart -= chunkSize;
		
		LoopRange loopRange = new LoopRange(chunkSize);
		loopRange.loopStart = upperBound;
		loopRange.loopEnd = lowerBound;
		loopRange.localStride = -1 * this.stride;
		
		return loopRange;
	}	
}
