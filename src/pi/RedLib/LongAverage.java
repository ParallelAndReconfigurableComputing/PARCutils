package pi.reductions;

public class LongAverage implements Reduction<Long> {

	@Override
	public Long reduce(Long first, Long second) {
		return (Long) ((first + second)/2);
	}

}
