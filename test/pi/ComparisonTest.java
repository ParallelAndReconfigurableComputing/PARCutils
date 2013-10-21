package pi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

/**
 * Author: xiaoxing
 * Date: 1/06/13
 */
@Ignore
@RunWith(JUnit4.class)
public class ComparisonTest {

	Collection<TargetObject> data = null;
	int size = 1000 * 1000 * 3 * 8;
	int numOfThreads = 8;
	int chunkSize = 3;
	ParIterator.Schedule schedule = ParIterator.Schedule.DYNAMIC;

	@Ignore
	@Test
	public void arrayList() throws InterruptedException {
		data = new ArrayList<TargetObject>(size);
		for (int i = 0; i < size; i++) {
			data.add(new TargetObject(i));
		}

		System.out.println("----- ArrayList -----");
		compare(data, schedule);
		compare(data, schedule);
		compare(data, schedule);
	}

	@Ignore
	@Test
	public void linkedList() {
		data = new LinkedList<TargetObject>();
		for (int i = 0; i < size; i++) {
			data.add(new TargetObject(i));
		}

		System.out.println("----- LinkedList -----");
		compare(data, schedule);
		compare(data, schedule);
		compare(data, schedule);
	}

	@Ignore
	@Test
	public void hashSet() {
		data = new HashSet<TargetObject>();
		for (int i = 0; i < size; i++) {
			data.add(new TargetObject(i));
		}

		System.out.println("----- HashSet -----");
		compare(data, schedule);
		compare(data, schedule);
		compare(data, schedule);
	}

	@Test
	public void memVsStatic() {
		int workingsetSize = 1000 * 10 * 8;
		int numOfThreads = 8;
		int chunkSize = 1000 * 10;
		ArrayList<TargetObject> data = TestingTools.getArrayList(workingsetSize);

		data = TestingTools.shuffle(data);

		ParIterator<TargetObject> staticIter =
				ParIteratorFactory.createParIterator(data, numOfThreads, ParIterator.Schedule.STATIC, chunkSize, true);
		ParIterator<TargetObject> memIter =
				ParIteratorFactory.createParIterator(data, numOfThreads, ParIterator.Schedule.MEMORYAWARE, chunkSize, true);
		List<String> redundant = new ArrayList<String>();

		redundant.add(TestingTools.efficiencyTest(staticIter, numOfThreads, "STATIC"));
		redundant.add(TestingTools.efficiencyTest(memIter, numOfThreads, "MEM"));
	}

//	ParIterator<TargetObject> newIter = null;

	private void compare(Collection<TargetObject> data, ParIterator.Schedule schedule) {
		List<String> redundant = new ArrayList<String>();

		ParIterator<TargetObject> oldIter =
				ParIteratorFactory.createParIterator(data, numOfThreads, schedule, chunkSize, true);
		redundant.add(TestingTools.efficiencyTest(oldIter, numOfThreads, "old pi"));

//		if (newIter == null) {
//			ParIterator<TargetObject> newIter =
//					newpi.ParIteratorFactory.getIterator(data, schedule, chunkSize, numOfThreads, true);
//			redundant.add(TestingTools.efficiencyTest(newIter, numOfThreads, "new pi"));
//		}

		for (String s : redundant) {
			System.out.println(s);
		}
	}



}
