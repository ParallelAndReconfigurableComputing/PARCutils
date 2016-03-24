package pu.RedLib;

public class IntegerAverage implements Reduction<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return (Integer) ((first + second)/2);
	}

}
