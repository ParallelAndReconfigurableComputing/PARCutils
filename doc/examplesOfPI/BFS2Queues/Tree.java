package examplesOfPI.BFS2Queues;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/**
 * Date Created: 1 August 2008 
 * 
 * The Tree Interface.. Any Tree code should implement this tree interface
 * 
 * @uthor: Wafaa Humadi
 **/
public interface Tree<E> {
	
	 
	 Tree<E> getParent();
	 void setParent(Tree<E> parent);
	 Iterator<Tree<E>> getChildren();
	 Iterator<Tree<E>> getAllChildren();
	 boolean isLeaf();
	 boolean isChild(Tree<E> child);
	 void removeChild(Tree<E> child);
	 void removeTreeBranch(Tree<E> node);
	 void removeAllChildren();
	 void addChild(Tree<E> child) throws CyclicTreeException;
	 int getNumChildren();
	 int getNumAllChildren();
	 boolean isDistantChild (Tree<E> child);
	 E getElement();
	 void setElement(E element);
	 Tree<E>[] getParents();
	 Tree<E> getTopNode();
	 Tree<E>[] getTreesAtLevel(int i);
	 ArrayList<Tree<E>> getChildrenList();
	 public HashSet<Tree<E>> children();
}
