package pu.loopScheduler;

/**
 * This class provides the elements for iterating over a partition
 * of a loop. That is, its objects provide the starting point, the
 * ending point, the local stride and the global stride for iterating
 * over a partitioned loop. The <code>local stride</code> attribute holds the same value as
 * the stride for the original loop that is being partitioned. The <code>global stride</code>
 * attribute holds the offset for the next chunk allocated to a thread, and IT IS ONLY USED
 * FOR <b>CYCLIC STATIC</b> LOOP SCHEDULING. 
 * </br></br>
 * <b>NOTE:</b> Importantly, it is noteworthy that because depending on the value of a 
 * local stride, the iteration of a loop partition may not start from the first element of 
 * its assigned chunk, calculations are required to ascertain the first element from which 
 * iterations over a loop partition should start. Therefore, this class does not allow direct
 * use of the <code>globalStride</code> attribute, instead users should shift to the next
 * <code>cyclic static</code> chunk by invoking the {@link #applyGlobalStrid()} method. 
 * </br></br>
 * Each thread must retrieve its own object by calling the {@link LoopScheduler#getChunk()}
 * method on instances of {@link LoopScheduler}, which can be of type: {@link StaticLoopScheduler},
 * {@link DynamicLoopScheduler} or {@link GuidedLoopScheduler} depending on the scheduling 
 * type that is passed to the {@link LoopSchedulerFactory}.
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class LoopRange {
	public int loopStart = 0;
	public int loopEnd = 0;
	public int localStride = 0;
	private int globalStride = 0;
	private int chunkSize;
	
	//LoopRange should not allow instantiations outside of this package. 
	LoopRange(int chunkSize){
		this.chunkSize = chunkSize;
	}
	
	void setGlobalStride(int gStride){
		this.globalStride = gStride;
	}
	
	/**
	 * This method MUST be used for <code><b>CYCLIC STATIC</b></code> loop partitioning pattern. 
	 * otherwise, the method returns without any progress. For <code><b>CYCLIC STATIC</b></code>
	 * partitioning pattern, the next partition for the invoking thread will be decided based on
	 * the values of <code>globalStride, localStride</code> and the current chunk. This method 
	 * does not return any value, but the changes will be applied to the same object. 
	 * </br></br> 
	 * <b>NOTE:</b> Importantly, it must be checked that the iterations do not exceed the very
	 * end of the original loop as the result of invoking this method. 
	 */
	public void applyGlobalStrid(){
		if(globalStride == 0)
			return;
		int newStart = loopEnd + globalStride;
		int offset = newStart - loopStart;
		int absoluteStride = Math.abs(localStride);
		int absoluteRemainder = Math.abs(offset%absoluteStride);
		
		if(absoluteRemainder != 0){
			offset += (localStride/absoluteStride) * (absoluteStride - absoluteRemainder);
		}
		loopStart += offset;		
		loopEnd = newStart + ((localStride/absoluteStride) * chunkSize);
	}
	
	public int getGlobalStrid(){
		return globalStride;
	}
}
