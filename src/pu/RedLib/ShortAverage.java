package pu.RedLib;

public class ShortAverage implements Reduction<Short> {

	@Override
	public Short reduce(Short first, Short second) {
		return (short) ((first + second)/2);
	}

}
