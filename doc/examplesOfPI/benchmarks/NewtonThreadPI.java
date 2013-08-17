package examplesOfPI.benchmarks;

import pi.ParIterator;

public class NewtonThreadPI extends Thread {

	private ParIterator<Integer> pi = null;
	private NewtonChaos newton = null;
	
	public NewtonThreadPI(NewtonChaos newton, ParIterator<Integer> pi) {
		this.newton = newton;
		this.pi = pi;
	}
	
	@Override
	public void run() {
		while (pi.hasNext()) {
			int n = pi.next();
			newton.performNewton(n);
		}
	}
}

