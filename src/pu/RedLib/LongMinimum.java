package pu.RedLib;

public class LongMinimum implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return Math.min(first, second);
	}

}
