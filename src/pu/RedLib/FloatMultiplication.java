package pu.RedLib;

public class FloatMultiplication implements Reduction<Float> {

	@Override
	public Float reduce(Float first, Float second) {
		return (Float) (first * second);
	}

}
