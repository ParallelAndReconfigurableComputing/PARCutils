package pu.RedLib;

public class FloatMinimum implements Reduction<Float> {

	@Override
	public Float reduce(Float first, Float second) {
		return Math.min(first, second);
	}

}
