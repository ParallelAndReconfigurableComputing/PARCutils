package pu.RedLib;

public class IntegerBitwiseAND implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return first & second;
	}

}
