package pu.loopScheduler;

/**
 * This class partitions the iterations over a loop into static chunks. That is every thread gets
 * <code>loop-size/number-of-threads</code> iterations, or a close number to this if the loop size is not 
 * divisible by the number of threads,  over a loop. This chunk size is set as the default value, if 
 * there is no chunk size specified by a user. However, users can specify chunk sizes for the static
 * partitioning. 
 * </br></br>
 * If user-specified is bigger than ziro, and not equal to the default chunk size, the loop scheduler performs
 * a cyclic loop scheduling. For more information see the descriptions provided for {@link LoopRange}, and its
 * method {@link LoopRange#applyGlobalStrid()}  
 *
 * @see {@link LoopRange}, {@link LoopRange#applyGlobalStrid()}
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class StaticLoopScheduler extends AbstractLoopScheduler{

	private boolean cyclic;
	private int loopSize;
	private int numberOfThreads;
	
	StaticLoopScheduler(int loopStart, int loopEnd, int stride, int chunkSize, int numberOfThreads, LoopCondition loopCondition) {		
		this.loopStart = loopStart;
		this.mainLoopStart = loopStart;
		this.loopEnd = loopEnd;
		this.loopCondition = loopCondition;
		this.loopSize = getLoopSize();
		this.stride = Math.abs(stride);
		this.cyclic = false;
		if((chunkSize > 0) && (chunkSize != (loopSize/numberOfThreads)))
			cyclic = true;
		this.isAscendingLoop = isAscendingLoop();
		this.numberOfThreads = numberOfThreads;
		this.chunkSize = decideChunkSize(chunkSize, numberOfThreads);	
	}
	
	StaticLoopScheduler(int loopStart, int loopEnd, int stride, int numberOfThreads, LoopCondition loopCondition) {
		this(loopStart, loopEnd, stride, 0, numberOfThreads, loopCondition);
	}	
	
	private int getLoopSize(){
		int size = Math.abs(this.loopEnd - this.loopStart);
		if (loopCondition.equals(LoopCondition.LessThanOrEqual) || loopCondition.equals(LoopCondition.GreaterThanOrEqual))
			size++;
		return size;
	}
	
	@Override
	public LoopRange getChunk(int threadID) {
		if(cyclic){
			return getCyclicChunks(threadID);
		}
		return getStaticChunks(threadID);
	}
	
	private LoopRange getStaticChunks(int threadID){
				
		if(isAscendingLoop){
			LoopRange loopRange = ascendingPartitioning(threadID);
			int extra = loopSize%chunkSize;
			if(extra > 0){
				if(threadID < extra){
					loopRange.loopStart = loopRange.loopStart + threadID;
					loopRange.loopEnd = loopRange.loopEnd + threadID + 1;
				}
				else{
					loopRange.loopStart = loopRange.loopStart + extra;
					loopRange.loopEnd = loopRange.loopEnd + extra;
				}
			}
			return loopRange;
		}
		else{
			LoopRange loopRange = descendingPartitioning(threadID);
			int extra = loopSize%chunkSize;
			if(extra > 0){
				if (threadID < extra){
					loopRange.loopStart = loopRange.loopStart - threadID;
					loopRange.loopEnd = loopRange.loopEnd - threadID - 1;
				}
				else{
					loopRange.loopStart = loopRange.loopStart - extra;
					loopRange.loopEnd = loopRange.loopEnd - extra;
				}
			}
			return loopRange;
		}
	}
	
	private LoopRange getCyclicChunks(int threadID){
		
		LoopRange loopRange;
		
		if(isAscendingLoop){
			loopRange = ascendingPartitioning(threadID);
			loopRange.setGlobalStride((numberOfThreads - 1) * chunkSize);
		}
		else{
			loopRange = descendingPartitioning(threadID);
			loopRange.setGlobalStride(-1 * ((numberOfThreads - 1) * chunkSize));
		}		
		return loopRange;
	}
	
	private LoopRange ascendingPartitioning(int threadID){
		if(loopCondition.equals(LoopCondition.GreaterThan) || loopCondition.equals(LoopCondition.GreaterThanOrEqual))
			throw new IllegalArgumentException("\n\nINVALID LOOP CONDITION FOR THIS LOOP INTERVAL \n "
					+ "LOOP IS ASCENDING, CONDITION CAN BE EITHER \"LessThan\" OR \"LessThanOrEqual\" \n");
		
		int offset = threadID * chunkSize;
		int remainder = offset%stride;
		
		int lowerBound = loopStart + offset;
		if(remainder != 0)
			lowerBound += (stride - remainder);
		
		int upperBound = loopStart + (offset + chunkSize);
		if(loopCondition == LoopCondition.LessThanOrEqual)
			upperBound--;
				
		LoopRange loopRange = new LoopRange(chunkSize);
		loopRange.loopStart = lowerBound;
		loopRange.loopEnd = upperBound;
		loopRange.localStride = stride;
		
		return loopRange;
	}
	
	private LoopRange descendingPartitioning(int threadID){
		if(loopCondition.equals(LoopCondition.LessThan) || loopCondition.equals(LoopCondition.LessThanOrEqual))
			throw new IllegalArgumentException("\n\nINVALID LOOP CONDITION FOR THIS LOOP INTERVAL \n"
					+ "LOOP IS DESCENDING, CONDITION CAN BE EITHER \"GreaterThan\" OR \"GreaterThanOrEqual\" \n");
				
		int offset = threadID * chunkSize;
		int remainder = offset%stride;
		
		int upperBound = loopStart - offset;
		if(remainder != 0)
			upperBound -= (stride - remainder);
		
		int lowerBound = loopStart - (offset + chunkSize);
		if(loopCondition == LoopCondition.GreaterThanOrEqual)
			lowerBound++;
		
		
		LoopRange loopRange = new LoopRange(chunkSize);
		loopRange.loopStart = upperBound;
		loopRange.loopEnd = lowerBound;
		loopRange.localStride = -1 * stride;
		return loopRange;
	}	
}
