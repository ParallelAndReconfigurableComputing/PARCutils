package pu.RedLib;

public class IntegerBitwiseXOR implements Reduction<Integer>{

	@Override
	public Integer reduce(Integer first, Integer second) {
		return first ^ second;
	}

}
