package pi.reductions;

import pi.operands.Operand;

public class OperandsReduction<T> implements Reduction<Operand<T>> {

	@Override
	public Operand<T> reduce(Operand<T> first, Operand<T> second) {
		first.operateOn(second);
		return first;
	}

}
