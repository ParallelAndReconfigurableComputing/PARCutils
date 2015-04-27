package pi.reductions;

public class LongBitwiseAND implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return first & second;
	}
}
