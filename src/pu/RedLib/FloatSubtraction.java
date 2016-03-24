package pi.RedLib;

public class FloatSubtraction implements Reduction<Float> {

	@Override
	public Float reduce(Float first, Float second) {
		return (Float) (first - second);
	}

}
