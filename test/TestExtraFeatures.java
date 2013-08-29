package testingfeatures;

import java.util.ArrayList;
import java.util.LinkedList;

import pi.ParIterator;
import pi.ParIteratorFactory;
import pi.exceptions.ParIteratorException;
import pi.reductions.Reducible;
import pi.reductions.Reduction;

/**
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 * @author Lama Akeila
 * 
 * 
 * Date created: 	20 February 2009
 * Last modified:	20 February 2009
 *
 *	Simple tests to check the extra features of the Parallel Iterator.
 *	
 */
public class TestExtraFeatures {
	
	public TestExtraFeatures() {
	}
	
	public static void main(String[] args) {
		
		ArrayList<Integer> list = new ArrayList<Integer>();
//		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 1; i <= 8; i++) {
			list.add(i);
		}
		
		int numThreads = 4;
//		ParIterator<Integer> pi = ParIteratorFactory.getParIterator(list, ParIterator.DYNAMIC, 2,  numThreads);
		ParIterator<Integer> pi = ParIteratorFactory.createParIterator(list, ParIterator.STATIC_SCHEDULE, 2, numThreads);
		Reducible<Integer> red = new Reducible<Integer>(0);
		
		TestThread[] threads = new TestThread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new TestThread(pi, red);
			threads[i].start();
		}
		
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		printExceptions(pi);
//		System.out.println("---- All done! Final = "+red.reduce(Reduction.IntegerSUM));
	}
	
	private static void printExceptions(ParIterator<Integer> pi) {
		ParIteratorException[] excs = pi.getAllExceptions();
		System.out.println("There was a total of "+excs.length+" exceptions");
		for (int i = 0; i < excs.length; i++) {
			ParIteratorException<Integer> pie = excs[i];
			Exception e = pie.getException();
			Integer iter = pie.getIteration();
			Thread regThread = pie.getRegisteringThread();
			System.out.println("  - Thread "+regThread+"  got a "+e+"  while processing iteration "+iter);
		}
	}
}
