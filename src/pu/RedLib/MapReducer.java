package pu.RedLib;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MapReducer<T> {
	private Reduction<T> reduction;
	private AtomicBoolean operationInProgress = new AtomicBoolean();
	private T reducedValue;
	
	MapReducer(){
		this.operationInProgress.set(false);
		this.reducedValue = null;
		this.reduction = null;
	}
	
	MapReducer(Object... requirements){
		this();
		setUpOperation(requirements);
	}
	
	
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
		if (reducedValue == null)
			reducedValue = result;
		else{
			parallelReduce(result, reducedValue);
			reducedValue = null;
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
	 * This method receives the expected user-inputs that are specific to 
	 * different implementations of this class, and sets the object up for
	 * the map-reduce operation.
	 * 
	 * @param requirements: an array of objects that contain the required 
	 * user-input for specific implementations of this class.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	protected abstract void setUpOperation(Object... requirements);
	
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
	 * the tasks. Each parallel task can submit their result using the 
	 * {@link#submitResult} method.
	 * The <i>submitResult</i> method, sends every two submitted
	 * result to {@link#parallelReduce} for parallel reduction.
	 * </br></br>
	 * <b>Note:</b> Invoking this method by user, should be done through a
	 * customized public method, in case customized customized map-reducers
	 * require specific inputs from user.   
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
