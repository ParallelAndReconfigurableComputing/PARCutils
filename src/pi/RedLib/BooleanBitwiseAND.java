package pi.RedLib;

public class BooleanBitwiseAND implements Reduction<Boolean> {

	@Override
	public Boolean reduce(Boolean first, Boolean second) {
		return first & second;
	}

}
