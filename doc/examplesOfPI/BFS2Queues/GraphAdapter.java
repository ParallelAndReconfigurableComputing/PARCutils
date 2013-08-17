package examplesOfPI.BFS2Queues;


import pi.GraphAdapterInterface;
import java.util.ArrayList;
import java.util.Collection;



/**
 * Date created: 30 March 2009
 * 
 * This Adapter class allows the user's graph library and the parallel graph
 * algorithm to work together.. Simply, this class should extend the certain
 * graph library to be used and the relevant methods used by the parallelized
 * algorithm should be implemented in order for the parallelized graph algorithm
 * to work properly..
 * 
 * @author Lama Akeila
 */

public class GraphAdapter<T extends TreeCode> implements GraphAdapterInterface{

	private int value;

	private String name;

	public T tree;
	
	public GraphAdapter(String name, T t) {

		this.tree = t;
		this.name = name;

	}
	
	public void setVal(Object d, int val) {
		((T) d).setVal(val);
	}

	public int getVal(Object d) {
		return ((T) d).value;
	}


	public int nodesSize() {

		return tree.getNumAllChildren();

	}

	public ArrayList verticesSet() {

		return tree.getAllChildrenList();

	}

	public ArrayList getChildrenList(Object d) {
		// TreeCode t = ()
		return ((T) d).getChildrenList();
	}

	// Returns the parent of a node
	public ArrayList getParentsList(Object d) {
		ArrayList<T> parentList = new ArrayList<T>();
		parentList.add((T) ((T)d).getParent());
		return parentList ;
	}

	@Override
	public Collection edgesSet() {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}

