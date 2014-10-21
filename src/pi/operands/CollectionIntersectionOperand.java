package pi.operands;

import java.util.Collection;

public class CollectionIntersectionOperand<T> extends Operand<Collection<T>> {

	CollectionIntersectionOperand(Collection<T> collection) {
		super(collection);
	}

	@Override
	protected Collection<T> operate(Operand<Collection<T>> collectionOperand) {
		Collection<T> collection = collectionOperand.getValue();	
		for (T t : collection){
			if (!operandValue.contains(t))
				collection.remove(t);
		}
		return collection;
	}

}
