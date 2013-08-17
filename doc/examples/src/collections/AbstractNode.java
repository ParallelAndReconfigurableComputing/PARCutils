package collections;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */

public class AbstractNode<E> implements Node<E> {

	private String name;
	private ArrayList<Node<E>> children = new ArrayList<Node<E>>();
	private Node<E> parent = null;
	private E value;
	
	public AbstractNode(String name, Node<E> parent, E value) {
		this.name = name;
		this.parent = parent;
		this.value = value;
		if (parent != null) {
			parent.addChild(this);
		}
	}
	
	@Override
	public E getValue() {
		return value;
	}
	
	@Override
	public void addChild(Node<E> node) {
		children.add(node);
	}
	
	@Override
	public String toString() { return name; }
	
	@Override
	public List<Node<E>> getChildren() {
		return children;
	}
	
	@Override
	public Node<E> getParent() {
		return parent;
	}
	
	@Override
	public void setValue(E e) {
		this.value = e;
	}
	
	@Override
	public boolean deleteFromParent() {
		if (parent != null) {
			return parent.getChildren().remove(this);	
		}
		return false;
	}
}
