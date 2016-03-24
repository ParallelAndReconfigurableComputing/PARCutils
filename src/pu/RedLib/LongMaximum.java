package pu.RedLib;

public class LongMaximum implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return Math.max(first, second);
	}

}
