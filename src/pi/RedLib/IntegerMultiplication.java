package pi.RedLib;

public class IntegerMultiplication implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return first * second;
	}

}
