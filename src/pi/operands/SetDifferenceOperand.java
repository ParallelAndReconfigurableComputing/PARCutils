package pi.operands;

import java.util.Set;

public class SetDifferenceOperand<T> extends Operand<Set<T>> {
	public SetDifferenceOperand(Set<T> set) {
		super(set);
	}
	
	@Override
	protected Set<T> opearte(Operand<Set<T>> setOperand) {
		Set<T> set = setOperand.getValue();
		for (T t : set){
			if (operandValue.contains(t)){
				//because we don't have duplicates in set, removing the 
				//first occurrence is enough
				operandValue.remove(t);
			}
		}
		return operandValue;
	}
}
