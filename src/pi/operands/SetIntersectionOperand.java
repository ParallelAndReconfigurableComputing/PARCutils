package pi.operands;

import java.util.Set;

public class SetIntersectionOperand<T> extends Operand<Set<T>> {

	SetIntersectionOperand(Set<T> set) {
		super(set);
	}

	@Override
	protected Set<T> opearte(Operand<Set<T>> setOperand) {
		Set<T> set = setOperand.getValue();
		for(T t : set){
			if (!operandValue.contains(t)){
				set.remove(t);
			}
		}
		return set;
	}

}
