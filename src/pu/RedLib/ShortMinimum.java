package pu.RedLib;

public class ShortMinimum implements Reduction<Short> {

	@Override
	public Short reduce(Short first, Short second) {
		if(first < second)
			return first;
		return second;
	}

}
