package pi.RedLib;

public class FloatAverage implements Reduction<Float> {

	@Override
	public Float reduce(Float first, Float second) {
		return (Float) ((first + second)/2.0f);
	}

}
