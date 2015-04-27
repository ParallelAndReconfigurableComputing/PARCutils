package pi.reductions;

public class IntegerBitwiseOR implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return first | second;
	}

}
