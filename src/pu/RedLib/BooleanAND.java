package pu.RedLib;

public class BooleanAND implements Reduction<Boolean> {

	@Override
	public Boolean reduce(Boolean first, Boolean second) {
		return first && second;
	}
	
}
