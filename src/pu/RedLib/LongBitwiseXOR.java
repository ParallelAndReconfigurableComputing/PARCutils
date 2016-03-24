package pu.RedLib;

public class LongBitwiseXOR implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return first ^ second;
	}

}
