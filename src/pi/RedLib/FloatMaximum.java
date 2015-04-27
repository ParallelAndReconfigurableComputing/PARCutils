package pi.reductions;

public class FloatMaximum implements Reduction<Float> {

	@Override
	public Float reduce(Float first, Float second) {
		return Math.max(first,  second);
	}

}
