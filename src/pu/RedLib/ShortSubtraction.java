package pu.RedLib;

public class ShortSubtraction implements Reduction<Short> {

	@Override
	public Short reduce(Short first, Short second) {
		return (short) (first - second);
	}

}
