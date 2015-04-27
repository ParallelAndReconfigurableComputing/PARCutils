package pi.RedLib;

public class IntegerMaximum implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return Math.max(first, second);
	}

}
