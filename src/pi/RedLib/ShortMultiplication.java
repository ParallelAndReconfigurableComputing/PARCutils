package pi.reductions;

public class ShortMultiplication implements Reduction<Short> {

	@Override
	public Short reduce(Short first, Short second) {
		return (short) (first * second);
	}

}
