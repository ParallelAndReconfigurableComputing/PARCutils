package pi.operands;

import java.util.Collection;

public class CollectionDifferenceOperand<T> extends Operand<Collection<T>> {

	public CollectionDifferenceOperand(Collection<T> collection) {
		super(collection);
	}
	
	@Override
	protected Collection<T> operate(Operand<Collection<T>> collectionOperand) {
		Collection<T> collection = collectionOperand.getValue();
		for (T t : collection){
			while(operandValue.contains(t))
				operandValue.remove(t);
		}
		return operandValue;
	}

}
