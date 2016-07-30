package pu.loopScheduler;

import pu.loopScheduler.AbstractLoopScheduler.LoopCondition;

/**
 * This class provides static methods for creating the appropriate loop scheduler based on the 
 * scheduling type specified by the user. The scheduling type can be chosen from one of <code>
 * Static, Dynamic, Guided</code> scheduling policies. The instances returned are sub-classes
 * of {@link LoopScheduler} interface.
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class LoopSchedulerFactory {
	
	public enum ScheduleType{
		Static, Dynamic, Guided
	};
	
	/**
	 * This static method returns an instance of {@link LoopScheduler}, based on the loop scheduling type specified by the user. 
	 * @param loopStart
	 * @param loopEnd
	 * @param stride
	 * @param chunkSize
	 * @param numOfThreads
	 * @param loopCondition
	 * @param scheduleType
	 * @return LoopScheduler an object of type {@link LoopScheduler}
	 */
	public static LoopScheduler createLoopScheduler(int loopStart, int loopEnd, int stride, int chunkSize, int numOfThreads, LoopCondition loopCondition, ScheduleType scheduleType){
		switch(scheduleType){
		case Static:
			return new StaticLoopScheduler(loopStart, loopEnd, stride, chunkSize, numOfThreads, loopCondition);
		case Dynamic:
			return new DynamicLoopScheduler(loopStart, loopEnd, stride, chunkSize, numOfThreads, loopCondition);
		case Guided:
			return new GuidedLoopScheduler(loopStart, loopEnd, stride, chunkSize, numOfThreads, loopCondition);
		}
		System.err.println("INVALID SCHEDULING TYPE SPECIFIED FOR THE LOOP-SCHEDULER\nCHOOSE FROM \"Static, Dynamic, Guided\"");
		return null;
	}
	
	/**
	 * This static method returns an instance of {@link LoopScheduler}, based on the loop scheduling type specified by the user. 
	 * @param loopStart
	 * @param loopEnd
	 * @param stride
	 * @param numOfThreads
	 * @param loopCondition
	 * @param scheduleType
	 * @return LoopScheduler an object of type {@link LoopScheduler}
	 */
	public static LoopScheduler createLoopScheduler(int loopStart, int loopEnd, int stride, int numOfThreads, LoopCondition loopCondition, ScheduleType scheduleType){
		switch(scheduleType){
		case Static:
			return new StaticLoopScheduler(loopStart, loopEnd, stride, numOfThreads, loopCondition);
		case Dynamic:
			return new DynamicLoopScheduler(loopStart, loopEnd, stride, numOfThreads, loopCondition);
		case Guided:
			return new GuidedLoopScheduler(loopStart, loopEnd, stride, numOfThreads, loopCondition);
		}
		System.err.println("INVALID SCHEDULING TYPE SPECIFIED FOR THE LOOP-SCHEDULER\nCHOOSE FROM \"Static, Dynamic, Guided\"");
		return null;
	}	
}
