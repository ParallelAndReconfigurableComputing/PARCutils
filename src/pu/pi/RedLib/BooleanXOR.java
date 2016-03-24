package pi.RedLib;

public class BooleanXOR implements Reduction<Boolean> {

	@Override
	public Boolean reduce(Boolean first, Boolean second) {
		return ((!first && second) || (first && !second));
	}

}
