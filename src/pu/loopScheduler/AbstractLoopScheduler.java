package pu.loopScheduler;

/**
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public abstract class AbstractLoopScheduler implements LoopScheduler {
	
	public enum LoopCondition {LessThan, LessThanOrEqual, GreaterThan, 
		GreaterThanOrEqual, Equal, NotEqual};
		
	protected int mainLoopStart;
	protected LoopCondition loopCondition;
	protected boolean isAscendingLoop;
	protected int chunkSize;
	protected int loopStart;
	protected int loopEnd;
	protected int stride;
	
	public abstract LoopRange getChunk(int threadID);
	
	protected int decideChunkSize(int userDefinedChunkSize, int numOfThreads){
		if (userDefinedChunkSize > 0)
			return userDefinedChunkSize;			
		
		int loopRange = 0;			
		if (loopStart > loopEnd){
			if (loopCondition == LoopCondition.GreaterThanOrEqual)
				loopRange = (loopStart - loopEnd) + 1;
			else
				loopRange = loopStart - loopEnd;
		}
		else{
			if(loopCondition == LoopCondition.LessThanOrEqual)
				loopRange = (loopEnd - loopStart) + 1;
			else
				loopRange = loopEnd - loopStart;
		}
		
		int chunkSize = Math.abs(loopRange/numOfThreads);
		
		if(chunkSize == 0)
			chunkSize = 1;
		
		return chunkSize;
	}
	
	protected boolean isAscendingLoop(){
		if (loopStart > loopEnd)
			return false;			
		return true;
	}
}
