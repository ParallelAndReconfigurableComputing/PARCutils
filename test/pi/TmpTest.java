package pi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pi.collect.Lists;
import pi.util.TLocal;
import pi.util.Flags;
import pi.util.ThreadID;

import java.util.*;

/**
 * Author: xiaoxing
 * Date: 31/05/13
 */


@RunWith(JUnit4.class)
public class TmpTest {

	@Ignore
	@Test
	public void collection2List() {
		Collection<TargetObject> data = new HashSet<TargetObject>();
		for (int i = 0; i < 5; i++) {
			data.add(new TargetObject(i));
		}

		List<TargetObject> d = new ArrayList<TargetObject>(data);
		for (TargetObject s : d) {
			System.out.println(s.getValue());
		}

		for (int i = 0; i < d.size(); i++) {
			d.get(i).setValue(10);
		}
		for (TargetObject s : d) {
			System.out.println(s.getValue());
		}

		for (TargetObject targetObject : data) {
			System.out.println(targetObject.getValue());
		}

	}

	@Ignore
	@Test
	public void mapTest() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(1, 10);
		System.out.format("%d -> %d\n", 1, map.get(1));
		System.out.format("%d -> %d\n", 2, map.get(2));
		int num = map.get(2);
		System.out.println(num);
		if (map.get(2) == null) {
			System.out.println("it is NULL");
		}
	}

	@Ignore
	@Test
	public void reverseList() {
		int length = 10;
		List<Integer> data = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			data.add(new Integer(i));
		}

		List<Integer> reversed = Lists.reverse(data);
		for (Integer integer : reversed) {
			System.out.format("%d\t", integer);
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void TLocal() {
//		ThreadID threadID = new ThreadID();
//		TLocal<Boolean> flag = new TLocal<Boolean>(threadID, false);
//
//		System.out.format("I got a %s\n", flag.get().toString());

	}

	@Ignore
	@Test
	public void flags() {
		System.out.format("%s: %d\n%s: %d\n", Flags.Flag.BREAK.toString(), Flags.Flag.BREAK.getValue(), Flags.Flag.RESET.toString(), Flags.Flag.RESET.getValue());
	}
}
