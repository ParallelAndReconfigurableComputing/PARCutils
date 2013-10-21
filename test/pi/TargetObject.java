package pi;

/**
 * Author: xiaoxing
 * Date: 31/05/13
 */
public class TargetObject {
	private int value;
	public TargetObject(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return Integer.toString(this.value);
	}
}
