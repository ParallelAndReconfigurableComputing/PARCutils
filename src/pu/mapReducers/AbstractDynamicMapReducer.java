package pu.mapReducers;

import java.util.concurrent.atomic.AtomicBoolean;

import pu.RedLib.Reduction;

/**
 * This abstract class provides a mechanism for mapping computational tasks 
 * to parallel threads, and dynamically reducing their results in parallel,
 * at the same time that tasks are being processed!</br></br>
 * By implementing the abstract methods of this class, users can implement
 * their own parallelized approach for map-reduce computations on shared-memory!
 * </br></br>
 * <b>NOTE:</b> This approach is designed for SIMD (single instruction, multiple
 * data) specifically! 
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 *
 * @param <T> Type of the final result
 * @param <E> Type of the data that is being processed in SIMD approach. 
 */
public abstract class AbstractDynamicMapReducer<T, E> implements MapReducer<T, E>{
	protected Reduction<T> reduction;
	private AtomicBoolean operationInProgress = new AtomicBoolean();
	private volatile T reducedValue;
	
	protected AbstractDynamicMapReducer(){
		this.operationInProgress.set(false);
		this.reducedValue = null;
		this.reduction = null;
	}
	
	/**
	 * This method starts a map-reduce operation, and it must be called after 
	 * the preliminary setup process has been done. Once this method starts a
	 * map-reduce operation, it does not start any other map-reduce operation
	 * until the current task is finished, and its result is retrieved.
	 *  
	 * @return boolean returns <code>true</code> if starts a map-reduce operation
	 * successfully, <code>false</code> otherwise. 
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	public synchronized boolean startMapReduceOperation(){
		if(reduction == null)
			throw new RuntimeException("The reduction object for MapReducer has not been initialized!");
				
		if(operationInProgress.get())
			return false;
		
		operationInProgress.set(true);
		map();
		return true;		
	}
	
	protected void setReduction(Reduction<T> reduction){
		this.reduction = reduction;
	}
	
	/**
	 * Every computation task must submit its result to this method, in order to get 
	 * it reduced with the results from other computation tasks. 
	 *  	 
	 * @param result the result returned from 
	 */
	protected synchronized void submitResult(T result){
		if (reducedValue == null){
			reducedValue = result;
		}
		else{
			T temp = reducedValue;
			reducedValue = null;
			parallelReduce(result, temp);
		}
	}
	
	/**
	 * Returns the final reduced result of the operations, 
	 * <b>iff</b> the operations are finished. Otherwise,
	 * the method returns <code>null</code>.
	 * 	
	 * @return The final reduced value if operations are finished, otherwise null!
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	public T getFinalResult(){
		waitTillOpertionFinished();
		return reducedValue;
	}
	
	
	protected void waitTillOpertionFinished(){
		waitTillMapReduceFinished();
		operationInProgress.set(false);
	}
	
	/**
	 * This method waits until every computation and reduce operation is finished. 
	 * The approach depends on user-specific implementation of this class.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	protected abstract void waitTillMapReduceFinished();	
	
	/**
	 * This method provides user-specified parallel approach for performing
	 * the tasks. Each parallel task must submit their result using the 
	 * {@link#submitResult} method.
	 * The <i>submitResult</i> method, sends every two submitted
	 * result to {@link#parallelReduce} for parallel reduction.
	 * </br></br>
	 * <b>Note:</b> Invoking this method by user, must be done through the 
	 * <code>public</code> {@link#startMapReduceOperation} method.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	protected abstract void map();
	
	/**
	 * This method provides user-specified parallel approach for performing
	 * reductions as parallel tasks. A user must submit their results back
	 * to {@link#submitResult} method. 
	 * 
	 * @param t1 first instance of the result, returned from one of the tasks
	 * @param t2 second instance of the result, returned from one of the tasks
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	protected abstract void parallelReduce(T t1, T t2);	
}
