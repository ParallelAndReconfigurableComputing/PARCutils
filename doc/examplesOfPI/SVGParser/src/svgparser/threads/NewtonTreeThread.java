package svgparser.threads;

import journal_paper_benchmarks.NewtonChaos;
import collections.Node;
import pi.ParIterator;

public class NewtonTreeThread extends Thread {
	
	private NewtonChaos newton;
	private ParIterator<Node<Integer>> pi;
	
	public NewtonTreeThread(NewtonChaos newton, ParIterator<Node<Integer>> pi) {
		this.newton = newton;
		this.pi = pi;
	}
	
	public void run() {
		while (pi.hasNext()) {
			Integer n = pi.next().getValue();
			newton.performNewton(n);
		}
	}
	
}
