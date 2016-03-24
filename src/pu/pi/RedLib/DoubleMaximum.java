package pi.RedLib;

public class DoubleMaximum implements Reduction<Double> {

	@Override
	public Double reduce(Double first, Double second) {
		return Math.max(first,  second);
	}

}
