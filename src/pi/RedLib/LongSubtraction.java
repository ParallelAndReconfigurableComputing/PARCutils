package pi.reductions;

public class LongSubtraction implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return first - second;
	}

}
