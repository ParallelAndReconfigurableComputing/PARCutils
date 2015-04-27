package pi.reductions;

public class DoubleSubtraction implements Reduction<Double> {

	@Override
	public Double reduce(Double first, Double second) {
		return (Double) (first - second);
	}

}
