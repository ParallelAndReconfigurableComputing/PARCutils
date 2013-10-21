package pi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Author: xiaoxing
 * Date: 26/05/13
 */
@Ignore
@RunWith(JUnit4.class)
public class MemoryAwarePITest {


	public void reset() {
		System.out.println("Hello World.");
	}

	@Test
	public void threadID() throws InterruptedException {
//		int dataSize = 100;
//		int chunkSize = 10;
//		int numOfThreads = 3;
//
//		List<Integer> collection = new ArrayList<Integer>();
//		for (int i = 0; i < dataSize; i++) {
//			collection.add(new Integer(i));
//		}
//		final MemoryAwareParIterator<Integer> memoryAwareParIterator =
//				new MemoryAwareParIterator<Integer>(collection, chunkSize, false);
//
//		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
//		for (int i = 0; i < numOfThreads; i++) {
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
//					String msg = String.format("Thread: %d\n", memoryAwareParIterator.getID());
//					while (memoryAwareParIterator.hasNext()) {
//						msg += String.format("%d\t", memoryAwareParIterator.next());
//					}
//					System.out.println(msg);
//				}
//			});
//		}
//		executor.shutdown();
//		while (!executor.isTerminated()) {
//			Thread.sleep(100);
//		}

	}
}
