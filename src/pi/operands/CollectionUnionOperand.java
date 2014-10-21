package pi.operands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Creates the union of the invoking <code>Collection&ltT&gt</code> and the second 
 * <code>Collection&ltT&gt</code> which is sent as argument. It basically iterates
 * through every element of the second <code>Collection&ltT&gt</code>, and tries 
 * adding them to the first <code>Collection&ltT&gt</code>. An attempt is successful
 * if and only if the corresponding element is not par of the first collection already!
 * 
 * @author Mostafa Mehrabi
 * @since  15/10/2014
 * */
public class CollectionUnionOperand<T> extends Operand<Collection<T>> {
	
	CollectionUnionOperand(Collection<T> collection) {
		super(collection);
	}

	@Override
	protected Collection<T> operate(Operand<Collection<T>> collectionOperand) {
		Collection<T> collection = collectionOperand.getValue();
		for (T t : collection){
			if(!operandValue.contains(t))
				operandValue.add(t);
		}
		return collection;
	}

}
