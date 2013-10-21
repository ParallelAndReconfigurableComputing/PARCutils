package pi;

import java.util.*;
import java.util.concurrent.*;

/**
 * Author: xiaoxing
 * Date: 1/06/13
 */
public class TestingTools {

	public static ArrayList<TargetObject> getArrayList(int dataSize) {
		ArrayList<TargetObject> data = new ArrayList<TargetObject>();
		for (int i = 0; i < dataSize; i++) {
			data.add(new TargetObject(i));
		}
		return data;
	}

	public static ArrayList<TargetObject> shuffle(ArrayList<TargetObject> data) {
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < data.size(); i++) {
			int change = i + random.nextInt(data.size() - i);
			TargetObject tmp = data.get(i);
			data.set(i, data.get(change));
			data.set(change, tmp);
		}
		return data;
	}

	public static String efficiencyTest(final ParIterator<TargetObject> iterator, int numOfThreads, String name) {

		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
		long results = 0;
		List<Callable<Long>> callables = new ArrayList<Callable<Long>>();

		long start = System.currentTimeMillis();
		for (int i = 0; i < numOfThreads; i++) {
			callables.add(new Callable<Long>() {
				@Override
				public Long call() throws Exception {
					long sum = 0;
					while (iterator.hasNext()) {
						sum += iterator.next().getValue();
					}
					return sum;
				}
			});
		}
		try {
			List<Future<Long>> futures = executor.invokeAll(callables);
			for (Future<Long> future : futures) {
				results += future.get();
			}
			long elapse = System.currentTimeMillis() - start;

			executor.shutdown();
			System.out.format("%s takes: %d ms\n", name, elapse);
			return Long.toString(results);

		} catch (InterruptedException e) {
			e.printStackTrace(); //TODO Resolve the catch block.
		} catch (ExecutionException e) {
			e.printStackTrace(); //TODO Resolve the catch block.
		}
		return null;
	}

	public static void dataDistributionTest(final ParIterator<? extends Object> iterator, int numOfThreads, String name) {

		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
		System.out.format("-----***** %s *****-----\n", name);
		for (int i = 0; i < numOfThreads; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String msg = String.format("----- Thread: %d -----\n", iterator.getID());
					int counter = 0;
					while (iterator.hasNext()) {
						msg += iterator.next() + "\t";
						counter++;
					}
					System.out.format("%d elements.\n", counter);
					System.out.println(msg);
				}
			});
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace(); //TODO Resolve the catch block.
			}
		}
	}

	public static void localbreakTest(
			final ParIterator<? extends Object> iterator, int numOfThreads, final Map<Integer, Integer> breakPlan, String name) {

		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
		System.out.format("-----***** %s *****-----\n", name);
		for (int i = 0; i < numOfThreads; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					Integer breakAt = breakPlan.get(iterator.getID());
					int breakPoint = -1;
					if (breakAt != null) {
						breakPoint = breakAt;
					}
					int counter = 0;
					String msg = String.format("----- Thread: %d -----\n", iterator.getID());
					while (iterator.hasNext()) {
						if (breakPoint == counter) {
							iterator.localBreak();
							System.out.format("Thread %d local break at %d.\n", iterator.getID(), breakPoint);
							break;
						}
						msg += iterator.next() + "\t";
						counter++;
					}
					System.out.println(msg);
				}
			});
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace(); //TODO Resolve the catch block.
			}
		}
	}

}
