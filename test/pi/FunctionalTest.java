package pi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

/**
 * Author: xiaoxing
 * Date: 31/05/13
 */

@RunWith(JUnit4.class)
public class FunctionalTest {

	@Test
	public void arrayListTest() {
		arrayList(ParIterator.Schedule.MEMORYAWARE);
		arrayList(ParIterator.Schedule.STATIC);
		arrayList(ParIterator.Schedule.DYNAMIC);
		arrayList(ParIterator.Schedule.GUIDED);
	}

	public void arrayList(ParIterator.Schedule schedule) {
		int dataSize = 100;
		int chunkSize = 10;
		int numOfThreads = 4;

		ArrayList<TargetObject> data = TestingTools.getArrayList(dataSize);

		final ParIterator<TargetObject> iterator =
				ParIteratorFactory.createParIterator(data, numOfThreads, schedule, chunkSize, false);

		TestingTools.dataDistributionTest(iterator, numOfThreads, schedule.name() + ", ArrayList");
	}

	@Test
	public void resetTest() {
		reset(ParIterator.Schedule.STATIC);
		reset(ParIterator.Schedule.DYNAMIC);
		reset(ParIterator.Schedule.GUIDED);
	}

	public void reset(ParIterator.Schedule schedule) {
		int dataSize = 100;
		int chunkSize = 10;
		int numOfThreads = 4;

		ArrayList<TargetObject> data = TestingTools.getArrayList(dataSize);

		final ParIterator<TargetObject> iterator =
				ParIteratorFactory.createParIterator(data, numOfThreads, schedule, chunkSize, false);

		TestingTools.dataDistributionTest(iterator, numOfThreads, schedule.name() + " reset 1");
		iterator.reset();
		TestingTools.dataDistributionTest(iterator, numOfThreads, schedule.name() + " reset 2");
		iterator.reset();
		TestingTools.dataDistributionTest(iterator, numOfThreads, schedule.name() + " reset 3");
	}
}
