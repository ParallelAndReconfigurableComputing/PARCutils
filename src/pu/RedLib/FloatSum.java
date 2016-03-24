package pu.RedLib;

public class FloatSum implements Reduction<Float> {

	@Override
	public Float reduce(Float first, Float second) {
		return (Float) (first + second);
	}

}
