package pi.util;

/**
 * Put all flags in this class in order to reduce condition checks in frequently invoked method such as <code>hasNext()</code>.
 *
 * Author: xiaoxing
 * Date: 5/06/13
 */
public class Flags {

	public enum Flag {
		BREAK(1),
		RESET(1 << 1);

		private int value;
		private Flag(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	};

	final private ThreadID threadID;
//	private AtomicInteger[] flags;

	final private TLocal<Integer> flags;

	public Flags(ThreadID threadID) {
		this.threadID = threadID;
		flags = new TLocal<Integer>(threadID, 0);
	}

	public boolean flagged() {
		return (0 != flags.get());
	}

	public void set(Flag flag) {
		flags.set(flags.get() | flag.getValue());
	}

	public void unset(Flag flag) {
		flags.set(flags.get() & ~flag.getValue());
	}

	public void setAll(Flag flag) {
		for (int i = 0; i < threadID.getThreadNum(); i++) {
			flags.set(i, flags.get(i) | flag.getValue());
		}
	}

	public void unsetAll(Flag flag) {
		for (int i = 0; i < threadID.getThreadNum(); i++) {
			flags.set(i, flags.get(i) & ~flag.getValue());
		}
	}

	public void reset() {
		flags.set(0);
	}

	public void resetAll() {
		flags.setAll(0);
	}

	public boolean flaggedWith(Flag flag) {
//		if ((flags.get() & flag.getValue()) != 0) {
////			unset(flag);	// After checking, unset the flag.
//			return true;
//		}
//		return false;
		return ((flags.get() & flag.getValue()) != 0);
	}
}
