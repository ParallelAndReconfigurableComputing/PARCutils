package pi.RedLib;

public class BooleanOR implements Reduction<Boolean> {

	@Override
	public Boolean reduce(Boolean first, Boolean second) {
		return first || second;
	}

}
