package pu.RedLib;

public class LongMultiplication implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return (Long) (first * second);
	}

}
