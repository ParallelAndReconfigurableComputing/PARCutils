package collections;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */


public class NodeHelper {
	
	private static NodeAction printAction = new NodeAction() {
		@Override
		public void performBefore(Node node) {
			List<Node<?>> children = node.getChildren();
			if (children.size() == 0)
				System.out.println(node.toString()+" has no children.");
			else
				System.out.println(node.toString()+" has "+children.size()+" children:");
			for (int i = 0; i < children.size(); i++) {
				System.out.println("\t"+children.get(i).toString());
			}
		}
		@Override
		public void performAfter(Node node) {
		}
	};
	
	public static void process(Node<?> root, NodeAction action) {
		traverseNodes(root, action);
	}
	

	private static <E> List<Node<E>> traverseNodes(Node<E> node, NodeAction action, List<Node<E>> list) {
		list.add(node);
		
		if (action != null)
			action.performBefore(node);
		List<Node<E>> children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			traverseNodes(children.get(i), action, list);
		}
		if (action != null)
			action.performAfter(node);
		
		return list;
	}
	
	private static <E> List<Node<E>> traverseNodes(Node<E> node, NodeAction action) {
		return traverseNodes(node, action, new ArrayList<Node<E>>());
//		List<Node<E>> list = new ArrayList<Node<E>>();
//		Deque<Node<E>> current = new LinkedList<Node<E>>();
//		current.add(root);
//		list.add(root);
//		
//		Node<E> parent = null;
//		while ((parent = current.pollFirst()) != null) {
//			List<Node<E>> children = parent.getChildren();
//			if (action != null)
//				action.performBefore(parent);
//			current.addAll(children);
//			list.addAll(parent.getChildren());
//		}
//		
//		return list;
	}
	
//	private static <E> List<Node<E>> traverseNodes(Node<E> root, NodeAction action) {
//		List<Node<E>> list = new ArrayList<Node<E>>();
//		Deque<Node<E>> current = new LinkedList<Node<E>>();
//		current.add(root);
//		list.add(root);
//		
//		Node<E> parent = null;
//		while ((parent = current.pollFirst()) != null) {
//			List<Node<E>> children = parent.getChildren();
//			if (action != null)
//				action.performBefore(parent);
//			current.addAll(children);
//			list.addAll(parent.getChildren());
//		}
//		
//		return list;
//	}
	
	public static <E> List<Node<E>> allNodes(Node<E> root) {
		return traverseNodes(root, null);
	}
	
	public static void print(Node<?> root) {
		process(root, printAction);
	}
	
	public static Node<Integer> generateTree(int numNodesTodo, int degree, int value) {
		Node<Integer> root = new SimpleNode<Integer>("[Node 0]", value);
		Deque<Node<Integer>> list = new LinkedList<Node<Integer>>();
		list.add(root);

		int numNodes = 1;
		while (true) {
			Node<Integer> parent = list.removeFirst();
			for (int i = 0; i < degree; i++) {
				if (numNodes == numNodesTodo)
					return root;
				
				Node<Integer> n = new SimpleNode<Integer>("[Node "+String.valueOf(numNodes)+"]", parent, value);
				list.add(n);
				numNodes++;
			}
		}
	}
	
	public static Node<Integer> generateTreeAlternateBinaryValue(int numNodesTodo, int degree) {
		int numNodes = 0;
		Node<Integer> root = new SimpleNode<Integer>("[Node 0]", numNodes++);
		
		Deque<Node> list = new LinkedList<Node>();
		list.add(root);
		
		while (true) {
			Node<Integer> parent = list.removeFirst();
			boolean alternate = parent.getValue() == 0;
			if (alternate) {
				for (int i = 0; i < degree; i++) {
					if (numNodes == numNodesTodo)
						return root;
					
					Node n = new SimpleNode("[Node "+String.valueOf(numNodes)+"]", parent, numNodes%2);
					
					numNodes++;
					list.add(n);
				}
			}
		}
	}
	
}
