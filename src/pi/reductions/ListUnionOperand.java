package pi.reductions;

import java.util.Collection;

/**
 * Creates the union of the invoking <code>Collection&ltT&gt</code> and the second 
 * <code>Collection&ltT&gt</code> which is sent as argument. It basically iterates
 * through every element of the second <code>Collection&ltT&gt</code>, and tries 
 * adding them to the first <code>Collection&ltT&gt</code>. 
 * 
 * @author Mostafa Mehrabi
 * @since  15/10/2014
 * */
public class ListUnionOperand<T> extends Operand<Collection<T>> {
	
	ListUnionOperand(Collection<T> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Collection<T> opearte(Operand<Collection<T>> t) {
		// TODO Auto-generated method stub
		return null;
	}

}
