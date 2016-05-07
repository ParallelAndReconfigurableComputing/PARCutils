package pu.mapReducers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pu.RedLib.Reduction;

public class ExecutorServiceDynamicMapReducer<T, E> extends AbstractDynamicMapReducer<T, E> {

	private ExecutorService executor = null;
	private int numberOfThreads = 0;
	private FunctorOneArgWithReturn<T, E> userDefinedFunctor = null;
	private int numberOfReductionTasks = 0;
	private int numberOfComputationTasks = 0;
	private Thread waitingThread = null;
	private boolean mapReducerInterrupt = false;
	private Collection<E> dataCollection = null;
	private int TIMEOUT_SECS = 60;
	
	
	public ExecutorServiceDynamicMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection) {
		
		setReduction(reduction);
		userDefinedFunctor = functor;
		this.dataCollection = dataCollection;
		executor = Executors.newCachedThreadPool();
		numberOfComputationTasks = dataCollection.size();		
	}
	
	public ExecutorServiceDynamicMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, int executorTimeOutSeconds, Collection<E> dataCollection) {
		
		setReduction(reduction);
		userDefinedFunctor = functor;
		this.dataCollection = dataCollection;
		executor = Executors.newCachedThreadPool();
		numberOfComputationTasks = dataCollection.size();		
		TIMEOUT_SECS = executorTimeOutSeconds;
	}
	
	public ExecutorServiceDynamicMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection, int threadCount) {
		
		setReduction(reduction);
		numberOfThreads = threadCount;
		userDefinedFunctor = functor;
		this.dataCollection = dataCollection;
		
		if(threadCount == 0)
			executor = Executors.newCachedThreadPool();
		else
			executor = Executors.newFixedThreadPool(numberOfThreads);
				
		numberOfComputationTasks = dataCollection.size();		
	}
	
	public ExecutorServiceDynamicMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, int executorTimeOutSeconds, Collection<E> dataCollection, int threadCount) {
		
		setReduction(reduction);
		numberOfThreads = threadCount;
		userDefinedFunctor = functor;
		this.dataCollection = dataCollection;
		
		if(threadCount == 0)
			executor = Executors.newCachedThreadPool();
		else
			executor = Executors.newFixedThreadPool(numberOfThreads);
				
		numberOfComputationTasks = dataCollection.size();		
		TIMEOUT_SECS = executorTimeOutSeconds;
	}
	
	@Override
	protected void waitTillMapReduceFinished() {
		while(numberOfReductionTasks != (numberOfComputationTasks -1)){
			try{
				waitingThread = Thread.currentThread();
				Thread.sleep(200);
			}catch(InterruptedException e){
				if(mapReducerInterrupt){
					mapReducerInterrupt = false;
					waitingThread = null;
					Thread.interrupted();//for clearing the flag!
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
		for(E element : dataCollection){
			Runnable runnable = () -> {
					T result = userDefinedFunctor.exec(element);
					submitResult(result);
				};
			executor.submit(runnable);
		}
	}

	@Override
	protected void parallelReduce(T t1, T t2) {
		Runnable runnable = () -> submitResult(reduction.reduce(t1, t2));
		executor.submit(runnable);
		numberOfReductionTasks++;
		if(waitingThread != null){
			mapReducerInterrupt = true;
			waitingThread.interrupt();
		}
	}
}
