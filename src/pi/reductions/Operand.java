package pi.reductions;

public abstract class Operand<T> {
	protected T operandValue;
	
	/**
	 * This method receives an argument of its own type, performs the customized
	 * operation specified by the programmer, and updates then returns the result
	 * which will subsequently update the <code>operandValue</code> of the current
	 * instance.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  14/10/2014
	 * */
	protected abstract T opearte(Operand<T> t);
	
	Operand(T t){
		operandValue = t;
	}
	
	public T getValue(){
		return operandValue;
	}
	
	public void setValue(T t){
		operandValue = t;
	}
	
	public void operateOn(Operand<T> t){
		operandValue = opearte(t);
	}
	
}
