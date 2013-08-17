package collections;

import java.util.List;


/**
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */


public interface Node<E> {
	
	public void setValue(E e);
	public E getValue();
	
	public void addChild(Node<E> node);
	public List<Node<E>> getChildren();
	
	public Node<E> getParent();
	public boolean deleteFromParent();
}
