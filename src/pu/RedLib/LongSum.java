package pu.RedLib;

public class LongSum implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return first + second;
	}

}
