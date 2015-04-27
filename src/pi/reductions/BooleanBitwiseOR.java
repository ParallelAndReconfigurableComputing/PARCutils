package pi.reductions;

public class BooleanBitwiseOR implements Reduction<Boolean> {

	@Override
	public Boolean reduce(Boolean first, Boolean second) {
		return first | second;
	}

}
