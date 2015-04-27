package pi.RedLib;

public class ShortBitwiseAND implements Reduction<Short>{

	@Override
	public Short reduce(Short first, Short second) {
		return (short) (first & second);
	}
}
