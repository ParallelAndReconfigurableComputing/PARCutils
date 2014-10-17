package pi.operands;

import java.util.Collection;

public class CollectionJoinOperand<T> extends Operand<Collection<T>> {

	CollectionJoinOperand(Collection<T> collection) {
		super(collection);
	}

	@Override
	protected Collection<T> opearte(Operand<Collection<T>> collectionOperand) {
		Collection<T> collection = collectionOperand.getValue();
		for (T t : collection)
			operandValue.add(t);
		return operandValue;
	}

}
