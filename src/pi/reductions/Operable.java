package pi.reductions;

public abstract class Operable<T> {
	private T operableValue;
	
	Operable(T t){
		operableValue = t;
	}
	
	public T getValue(){
		return operableValue;
	}
	
	public void setValue(T t){
		operableValue = t;
	}
	
	public void operateOn(Operable<T> t){
		operableValue = opearte(t);
	}
	
	/**
	 * This method receives an argument of its own type, performs the customized
	 * operation specified by the programmer, and updates then returns the result
	 * which will subsequently update the <code>operableValue</code> of the current
	 * instance.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  14/10/2014
	 * */
	protected abstract T opearte(Operable<T> t);
}
