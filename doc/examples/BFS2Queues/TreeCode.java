package examplesOfPI.BFS2Queues;


import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Date Created: 1 August 2008 
 * 
 * This class implements the tree interface
 * 
 * @uthor: Wafaa Humadi
 **/
public class TreeCode<E> implements Tree<E>{
	
	Tree<E> parent;
	E element;
	int value;
	public HashSet<Tree<E>> children;
	
	public int getVal(){
		return this.value;
	}
	
	public void setVal(int v){
		this.value = v;
	}
	
	public TreeCode(E element){
		
		if (element != null) {
            this.element = element;
            this.children = new HashSet<Tree<E>>();
        }
        else {
            throw new java.lang.IllegalArgumentException ("Tree node must contain an element.");
        }
        
    	
	}
	
	
	
	/**
	 * returns true if the node has children
	 */
	public boolean hasChildren () {
        
        return !(this.children.isEmpty());
        
    }
	/**
	 * returns iterator of the immediate children
	 */
	 public Iterator<Tree<E>> getChildren() {
	        
	        return this.children.iterator();
	        
	 }
	 /**
	  * returns the parent of a node
	  */
	public Tree<E> getParent() {
		
		return this.parent;
	}
	/**
	 * returns true if the node is a child 
	 */
	public boolean isChild(Tree<E> child) {
		
		return this.children.contains(child);
	}
	
	/**
	 * returns true if a node is a leaf
	 */
	public boolean isLeaf() {
		
		return this.children.isEmpty();
	}
	/**
	 * removes all children of a tree
	 */
	public void removeAllChildren() {
		Tree<E> aTree;
        Iterator<Tree<E>> iterator;
        
        iterator = this.getChildren();
        
        while (iterator.hasNext()) {
            aTree = iterator.next();
            aTree.removeAllChildren();
           
        }
        
	}
	
	
	/**
	 * removes a given node from the tree
	 */
	public void removeChild(Tree<E> child) {
		
        this.children.remove(child);

	}
	/**
	 * removes a brach of tree starting with the given node
	 */
	public void removeTreeBranch(Tree<E> node) {
		
		 Tree<E> parent;
	        
	        parent = node.getParent();
	        node.removeAllChildren();
	        if (parent!=null) {
	            parent.removeChild(node);
	        }

	}
	/**
	 * set the root of a tree
	 */
	public void setParent(Tree<E> parent) {
		this.parent=parent;

	}

	/**
	 * main methos
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * Returns the number of children (nodes)
	 */
	public int getNumChildren(){
		return this.children.size();
	}
	/**
	 * This method adds a node to the tree
	 */
	public void addChild(Tree<E> child) throws CyclicTreeException{
		
		if ((this != child) && (!isDistantChild(child))) {
            this.children.add(child);
            child.setParent(this);
        } else {
            throw new CyclicTreeException("Can't addChild to this Tree because it is a child of this Tree node or this Tree node itself.");
        }
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * checks if child is a direct child of this Tree node
     * isChild is always true when isDistantChild is true 
     * **/
	public boolean isDistantChild (Tree<E> child) {
		
        
        Tree<E> aTree;
        Iterator<Tree<E>> iterator;
        boolean found;
        
        found = false;
        iterator = this.getChildren();
        
        while (iterator.hasNext() && (!found)) {
            aTree =  iterator.next();
            found = ((aTree == child) || (aTree.isDistantChild(child)));
        }
        
        return found;
        
    }
	/**
	 * returns a tree at the specified level
	 * **/
	public Tree<E>[] getTreesAtLevel(int i) {
        
        Iterator<Tree<E>> iterator;
        Tree<E>[] aTreeArray;
        Tree<E>[] anotherTreeArray;
        Tree<E> aTree;
        int j, k, l;
        
        aTree = this.getTopNode();
        anotherTreeArray = new Tree[0];
        anotherTreeArray[0] = aTree;
        
        while (i > 0) {
            aTreeArray = anotherTreeArray;
            k = 0;
            for (j = 0; j < aTreeArray.length; j++) {
                k = k + aTreeArray[j].getNumChildren();
            }
            anotherTreeArray = new Tree[k];
            k = 0;
            for (j = 0; j < aTreeArray.length; j++) {
                iterator = (Iterator<Tree<E>>) aTreeArray[j].getChildren();
                for (l = 0; l < aTreeArray[j].getNumChildren(); l++) {
                    k = k + 1;
                    anotherTreeArray[k] = iterator.next();
                }
                k = k + aTreeArray[j].getNumChildren();
                
            }
            i = i - 1;
        }
        
        return anotherTreeArray;
        
    }
	/**
	 * returns the element of a node
	 ***/
	public E getElement() {
		
		return this.element;
	}
	/**
	 * returns a collection of all the parents in the tree
	 * **/
	public Tree<E>[] getParents() {
		Tree<E>[] parents;
        Tree<E> aTree;
        int i;
        
        i=0;
        aTree = this.getParent();
        
        while (aTree != null){
            aTree = aTree.getParent();
            i++;
        }
        
        parents = new Tree[i];
        aTree = this.getParent();
        
        while (aTree != null){
            aTree = aTree.getParent();
            parents[i] = aTree;
        }
        
        return parents;
        
	}
	/**
	 * returns the top node of the tree
	 * **/
	public Tree<E> getTopNode() {
		 Tree<E> aTree;
	     Tree<E> anotherTree;
	        
	     aTree = this.getParent();
	     anotherTree = null;
	        
	     while (aTree != null) {
	         anotherTree = aTree;
	         aTree = aTree.getParent();
	     }
	        
	     return anotherTree;
	}
	/**
	 * sets element for each node
	 * **/
	public void setElement(E element) {
		 if (element != null) {
	            this.element = element;
	        }
	        else {throw new java.lang.IllegalArgumentException ("Tree node must contain an element.");
	        }
		
	}

	/**
	 * Returns an iterator of all the children and sub-children of the this Tree node
	 **/
	public Iterator<Tree<E>> getAllChildren() {
		
        return this.getAllChildrenList().iterator();
        
	}
	/**
	 * Returns a list of all the children and sub-children of the this Tree node
	 **/
	protected ArrayList getAllChildrenList() {
		
		ArrayList<Tree<E>> nodesList = new ArrayList<Tree<E>>();
        Tree<E> aTree;
        LinkedBlockingDeque<E> queue = new LinkedBlockingDeque<E>();
        queue.addAll(this.getChildrenList());
        nodesList.addAll(this.getChildrenList());
        while (!queue.isEmpty()){
        	aTree = (Tree<E>)queue.pollFirst();
        	if (!aTree.isLeaf()){
        		queue.addAll((Collection) aTree.getChildrenList());
        		nodesList.addAll(aTree.getChildrenList());
        	}
        	
        }
         return nodesList;
        
	}
	/**
	 * Returns the number of all children and sub-children of this tree node
	 **/
	public int getNumAllChildren() {
		
        return this.getAllChildrenList().size();
        
	}
	
	
	/**
	 * returns a list of all nodes that are children in the tree 
	 */
	public ArrayList getChildrenList(){
		ArrayList children = new ArrayList();
		children.addAll(this.children);
		return children;
	}
	
	public HashSet<Tree<E>> children(){
		return this.children;
	}
}

