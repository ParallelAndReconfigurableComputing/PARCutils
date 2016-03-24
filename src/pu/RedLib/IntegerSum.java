package pi.RedLib;

public class IntegerSum implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return first + second;
	}

}
