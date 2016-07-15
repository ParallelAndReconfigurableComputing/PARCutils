package pu.loopSplitter;

import java.util.concurrent.locks.ReentrantLock;

/**
 * This class splits loop iterations into constant chunk sizes depending on the number of threads
 * that are given to the constructor. Furthermore, the threads dynamically acquire chunks for 
 * iterating a loop in parallel. 
 * </br>
 * This splitter class is mainly designed for parallelizing iterations over a <code>for loop</code>
 * among multiple threads, where they do not need to compete over loop iterators, and are specifically
 * granted their own chunk of iterators to process. 
 * </br>
 * For example, if a <code>for loop</code> has one hundred iterations, and four threads are going to 
 * iterate through the loop, each of them get 25 iterations to process separately. Threads acquire
 * their chunks by invoking method {@link #getNextChunk()} on an object of this class. The method 
 * returns an object of {@link pu.loopSplitter.LoopRange}, where user acquires <code>loopStart</code>
 * and <code>loopEnd</code> attributes that specify the start and end of iteration for a thread. 
 * Threads must continue invoking the {@link #getNextChunk()} method, until the method returns
 * <code>null</code>, which indicates that the end of iterations is reached. 
 * 
 * @author Mostafa Mehrabi
 *
 */
public class LoopSplitter {

	public enum LoopCondition {LessThan, LessThanOrEqual, GreaterThan, 
		GreaterThanOrEqual, Equal, NotEqual};

	private int loopStart;
	private int loopEnd;
	private int incrementStep;
	private int chunkSize;
	private boolean done;
	private boolean isAscendingLoop;
	private ReentrantLock lock;
	private LoopCondition loopCondition;
	private int mainLoopStart;
		
	public LoopSplitter(int loopStart, int loopEnd, int incrementStep, int numOfThreads, LoopCondition loopCondition){
		this.done = false;
		this.loopStart = loopStart;
		this.mainLoopStart = loopStart;
		this.loopEnd = loopEnd;
		this.incrementStep = Math.abs(incrementStep);
		this.lock = new ReentrantLock();
		this.loopCondition = loopCondition;
		this.isAscendingLoop = isAscendingLoop(numOfThreads);
	}
	
	private boolean isAscendingLoop(int numOfThreads){
		int loopRange = 0;			
		if (loopStart > loopEnd){
			if (loopCondition == LoopCondition.GreaterThanOrEqual)
				loopRange = (loopEnd - loopStart) - 1;
			else
				loopRange = loopEnd - loopStart;
			chunkSize = Math.abs(loopRange/numOfThreads);
			return false;			
		}
		
		if(loopCondition == LoopCondition.LessThanOrEqual)
			loopRange = (loopEnd - loopStart) + 1;
		else
			loopRange = loopEnd - loopStart;
		chunkSize = Math.abs(loopRange/numOfThreads);
		return true;
	}
	
	public LoopRange getNextChunk(){
		if(isAscendingLoop)
			return nextChunkAscending();
		else
			return nextChunkDescending();
	}
	
	private LoopRange nextChunkAscending(){
		lock.lock();
		if (done){
			lock.unlock();
			return null;
		}
		
		if(loopCondition == LoopCondition.GreaterThan || loopCondition == LoopCondition.GreaterThanOrEqual){
			done = true;
			lock.unlock();
			return null;
		}
		
		int lowerBound = (loopStart + loopStart%incrementStep);
		int upperBound = (loopStart + chunkSize);
		if(loopCondition == LoopCondition.LessThanOrEqual)
			upperBound -= 1;
		
		if(lowerBound > loopEnd){
			done = true;
			lock.unlock();
			return null;
		}
		
		if(upperBound > loopEnd){
			upperBound = loopEnd;
			done = true;
		}
		
		loopStart += chunkSize;
		
		LoopRange loopRange = new LoopRange();
		loopRange.loopStart = lowerBound;
		loopRange.loopEnd = upperBound;
		
		lock.unlock();
		return loopRange;
	}
	
	private LoopRange nextChunkDescending(){
		lock.lock();
		if(done){
			lock.unlock();
			return null;
		}
		
		if(loopCondition == LoopCondition.LessThan || loopCondition == LoopCondition.LessThanOrEqual){
			done = true;
			lock.unlock();
			return null;
		}
		
		int inverseStart = mainLoopStart - loopStart;
		inverseStart += inverseStart%incrementStep;
		int upperBound = mainLoopStart - inverseStart;
		int lowerBound = loopStart - chunkSize;
		if(loopCondition == LoopCondition.GreaterThanOrEqual)
			lowerBound += 1;
		
		if(upperBound < loopEnd){
			done = true;
			lock.unlock();
			return null;
		}
		
		if (lowerBound < loopEnd){
			lowerBound = loopEnd;
			done = true;
		}
		
		loopStart -= chunkSize;
		
		LoopRange loopRange = new LoopRange();
		loopRange.loopStart = upperBound;
		loopRange.loopEnd = lowerBound;
		
		lock.unlock();
		return loopRange;
	}	
}
