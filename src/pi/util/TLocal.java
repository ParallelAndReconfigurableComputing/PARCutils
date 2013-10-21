package pi.util;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * This class works similar to the standard ThreadLocal but with
 * global control. All variables owned by individual threads can
 * be accessed globally.
 *
 * Author: xiaoxing
 * Date: 4/06/13
 */
public class TLocal<E> {
	private AtomicReferenceArray<E> values;
//	private Object[] values;
	private ThreadID threadID;

	public TLocal(ThreadID threadID) {
		this(threadID, null);
	}

	public TLocal(ThreadID threadID, E initialValue) {
		this.threadID = threadID;
		values = new AtomicReferenceArray<E>(threadID.getThreadNum());
		for (int i = 0; i < values.length(); i++) {
			values.set(i, initialValue);
		}
	}

	public E get() {
		return get(threadID.get());
	}

	public void set(E value) {
		set(threadID.get(), value);
	}

	public E get(int id) {
		return values.get(id);
	}

	public void set(int id, E value) {
		values.set(id, value);
	}

	public void setAll(E value) {
		for (int i = 0; i < values.length(); i++) {
			values.set(i, value);
		}
	}
}
