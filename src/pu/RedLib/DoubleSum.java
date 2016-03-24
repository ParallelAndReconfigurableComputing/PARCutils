package pu.RedLib;

public class DoubleSum implements Reduction<Double> {

	@Override
	public Double reduce(Double first, Double second) {
		return (Double) (first + second);
	}

}
