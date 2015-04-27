package pi.reductions;

public class DoubleAverage implements Reduction<Double> {

	@Override
	public Double reduce(Double first, Double second) {
		return (Double) ((first + second)/2.0d);
	}

}
