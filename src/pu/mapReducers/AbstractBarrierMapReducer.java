package pu.mapReducers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pu.RedLib.Reduction;

public abstract class AbstractBarrierMapReducer<T, E> implements MapReducer<T, E> {
	protected Reduction<T> reduction;
	protected Collection<E> thisDataCollection;
	protected int numOfComputationTasks;
	protected FunctorOneArgWithReturn<T, E> userDefinedFunctor;
	private AtomicBoolean operationInProgress = new AtomicBoolean();
	List<T> listOfResults;
	private volatile T reducedValue;
	private ReentrantLock lock;
	protected AtomicLong reductionStartTime = null;
	protected AtomicLong latestReductionTime = null; 
	
	protected AbstractBarrierMapReducer(){
		this.listOfResults = new ArrayList<>();
		this.operationInProgress.set(false);
		this.reducedValue = null;
		this.reduction = null;
		this.lock = new ReentrantLock();
		this.reductionStartTime = new AtomicLong(0);
		this.latestReductionTime = new AtomicLong(0);
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
	
	protected void submitResult(T result){
		lock.lock();
		listOfResults.add(result);
		if(listOfResults.size() == numOfComputationTasks){
			reductionStartTime.set(System.currentTimeMillis());
			for(T t : listOfResults)
				if (t != null)
					submitReduction(t);
		}
		lock.unlock();
	}
	
	/**
	 * Every computation task must submit its result to this method, in order to get 
	 * it reduced with the results from other computation tasks. 
	 *  	 
	 * @param result the result returned from 
	 */
	protected void submitReduction(T result){
		lock.lock();
		if (reducedValue == null){
			reducedValue = result;
		}
		else{
			T temp = reducedValue;
			reducedValue = null;
			parallelReduce(result, temp);
		}
		lock.unlock();
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
		waitTillReductionsFinished();
		operationInProgress.set(false);
	}
	
	/**
	 * This method waits until every computation and reduce operation is finished. 
	 * The approach depends on user-specific implementation of this class.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	protected abstract void waitTillReductionsFinished();	
	
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
	
	/**
	 * Returns the time period in which reductions have been done
	 * in parallel at the end of the process, for evaluation purposes.
	 * 
	 * @return long: the reduction time duration in milliseconds. 
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2016
	 */
	public long getReductionDuration(){
		waitTillReductionsFinished();
		long start = reductionStartTime.get();
		long end = latestReductionTime.get();
		return end - start;
	}
}

