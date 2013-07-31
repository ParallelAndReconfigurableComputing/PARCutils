package svgparser.threads;

import java.util.Iterator;

import pi.ParIterator;
import journal_paper_benchmarks.NewtonChaos;
import collections.Node;
import collections.NodeHelper;
import collections.pi.NodeParIterator;

public class NewtonTreeMain {
	
	
	public void sequential(Node<Integer> tree, NewtonChaos newton) {
		Iterator<Node<Integer>> it = NodeHelper.allNodes(tree).iterator();
		while (it.hasNext()) {
			Integer n = it.next().getValue();
			newton.performNewton(n);
		}
	}
	
	public static void main(String[] argv) {
		
		String app = "seq";
    	app = "npi";
    	int numThreads = 1;
    	
    	int degree = 10;
    	int numNodes = 1000;
    	int N = 1;
    	
    	if (argv.length != 0) {
    		try {
    			app = argv[0];
				numNodes = Integer.parseInt(argv[1]);
				degree = Integer.parseInt(argv[2]);
				N = Integer.parseInt(argv[3]);
	    		numThreads = Integer.parseInt(argv[4]);
    		} catch (Exception e) {
    			System.out.println("Usage: (seq|npi) <numNodes> <degree> <N> <numThreads> ");
    			System.exit(0);
    		}
    	}
    	NewtonChaos newton = new NewtonChaos();
		Node<Integer> tree = NodeHelper.generateTree(numNodes, degree, N);
		
		
    	System.gc();
    	
    	long start = System.currentTimeMillis();
    	
    	if (app.equals("npi")) {
    		ParIterator<Node<Integer>> pi = new NodeParIterator<Integer>(tree, numThreads);
    		NewtonTreeThread[] threads = new NewtonTreeThread[numThreads];
    		for (int i = 0; i < numThreads; i++) {
    			threads[i] = new NewtonTreeThread(newton, pi);
    			threads[i].start();
    		}
			try {
	    		for (int i = 0; i < numThreads; i++) {
						threads[i].join();
				}
	    		long end = System.currentTimeMillis();
	    		System.out.println("NodePI time for "+numNodes+" nodes, degree "+degree+", "+numThreads+" threads and N="+N+" = "+(end-start));
			} catch (InterruptedException e) {
				e.printStackTrace();
    		}
    	} else if (app.equals("seq")) {
        	NewtonTreeMain main = new NewtonTreeMain();
    		main.sequential(tree, newton);
    		long end = System.currentTimeMillis();
    		System.out.println("Seq time for "+numNodes+" nodes, degree "+degree+" and N="+N+" = "+(end-start));
    	}
	}
}
