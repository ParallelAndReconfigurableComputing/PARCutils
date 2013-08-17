package collections;


/**
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */

public class SimpleNode<E> extends AbstractNode<E> {
	
	public SimpleNode(String name, E value) {
		super(name, null, value);
	}
	
	public SimpleNode(String name, Node<E> parent, E value) {
		super(name, parent, value);
	}
}
