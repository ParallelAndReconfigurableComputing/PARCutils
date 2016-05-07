package pu.mapReducers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pu.RedLib.Reduction;

public class ExecutorServiceBarrierMapReducer<T, E> extends AbstractBarrierMapReducer<T, E> {

	private ExecutorService executor = null;
	private int numberOfThreads = 0;
	private int numberOfReductionTasks = 0;
	private Thread waitingThread = null;
	private boolean mapReducerInterrupt = false;
	private int TIMEOUT_SECS = 60;
	
	
	public ExecutorServiceBarrierMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection) {
		
		setReduction(reduction);
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		executor = Executors.newCachedThreadPool();
		numOfComputationTasks = dataCollection.size();
	}
	
	public ExecutorServiceBarrierMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, int executorTimeOutSeconds, Collection<E> dataCollection) {
		
		setReduction(reduction);
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		executor = Executors.newCachedThreadPool();
		numOfComputationTasks = dataCollection.size();		
		TIMEOUT_SECS = executorTimeOutSeconds;
	}
	
	public ExecutorServiceBarrierMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection, int threadCount) {
		
		setReduction(reduction);
		numberOfThreads = threadCount;
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		
		if(threadCount == 0)
			executor = Executors.newCachedThreadPool();
		else
			executor = Executors.newFixedThreadPool(numberOfThreads);
				
		numOfComputationTasks = dataCollection.size();		
	}
	
	public ExecutorServiceBarrierMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, int executorTimeOutSeconds, Collection<E> dataCollection, int threadCount) {
		
		setReduction(reduction);
		numberOfThreads = threadCount;
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		
		if(threadCount == 0)
			executor = Executors.newCachedThreadPool();
		else
			executor = Executors.newFixedThreadPool(numberOfThreads);
				
		numOfComputationTasks = dataCollection.size();		
		TIMEOUT_SECS = executorTimeOutSeconds;
	}
	
	@Override
	protected void waitTillReductionsFinished() {
		while(numberOfReductionTasks != (numOfComputationTasks -1)){
			try{
				waitingThread = Thread.currentThread();
				Thread.sleep(200);
			}catch(InterruptedException e){
				if(mapReducerInterrupt){
					mapReducerInterrupt = false;
					waitingThread = null;
					Thread.interrupted();//to clear the flag!
				}
			}
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(TIMEOUT_SECS, TimeUnit.SECONDS);
			executor.shutdownNow();
		} catch (InterruptedException e) {
			System.out.println("ERROR OCCURED WHILE WAITING FOR EXECUTOR-SERVICE TO SHUTDOWN!");
			e.printStackTrace();
		}
	}

	@Override
	protected void map() {
		for(E element : thisDataCollection){
			Runnable runnable = () -> {
					T result = userDefinedFunctor.exec(element);
					submitResult(result);
				};
			executor.submit(runnable);
		}
	}

	@Override
	protected void parallelReduce(T t1, T t2) {
		Runnable runnable = () -> submitReduction(reduction.reduce(t1, t2));
		executor.submit(runnable);
		numberOfReductionTasks++;
		if(waitingThread != null){
			mapReducerInterrupt = true;
			waitingThread.interrupt();
		}
	}

}
