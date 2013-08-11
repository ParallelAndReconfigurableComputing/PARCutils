package collections;


/**
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */

public interface NodeAction {

	public void performBefore(Node node);
	public void performAfter(Node node);
	
}
