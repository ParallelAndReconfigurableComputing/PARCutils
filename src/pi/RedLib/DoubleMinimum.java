package pi.RedLib;

public class DoubleMinimum implements Reduction<Double> {

	@Override
	public Double reduce(Double first, Double second) {
		return Math.min(first,  second);
	}

}
