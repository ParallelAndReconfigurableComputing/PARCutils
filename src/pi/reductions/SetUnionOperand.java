package pi.reductions;

import java.util.Set;
/**
 * Creates the union of the invoking <code>Set&ltT&gt</code> and the second 
 * <code>Set&ltT&gt</code> which is sent as argument. It basically iterates
 * through every element of the second <code>Set&ltT&gt</code>, and tries 
 * adding them to the first <code>Set&ltT&gt</code>. The nature of <code>Set</code>
 * does not allow duplicate elements. 
 * 
 * @author Mostafa Mehrabi
 * @since  15/10/2014
 * */
public class SetUnionOperand<T> extends Operand<Set<T>> {

	SetUnionOperand(Set<T> t) {
		super(t);
	}

	@Override
	protected Set<T> opearte(Operand<Set<T>> t) {
		Set<T> secondOperandValue = t.getValue();
		for(T setElement : secondOperandValue)
			operandValue.add(setElement);			
		return operandValue;
	}

}
