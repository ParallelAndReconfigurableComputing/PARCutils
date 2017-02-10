package pu.RedLib;

public class IntegerMinimum implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return Math.min(first, second);
	}
}
