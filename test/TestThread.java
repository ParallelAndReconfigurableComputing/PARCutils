package testingfeatures;

import pi.ParIterator;
import pi.UniqueThreadIdGenerator;
import pi.reductions.Reducible;

/**
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
public class TestThread extends Thread {

	private ParIterator<Integer> pi = null;
	private Reducible<Integer> red = null;
	
	public TestThread(ParIterator<Integer> pi, Reducible<Integer> red) {
		this.pi = pi;
		this.red = red;
	}
	
	@Override
	public void run() {
//		testReductions();
		
//		testExceptions();
		
//		testGlobalBreak();
		
		testLocalBreak();
	}
	
	private void testLocalBreak() {	
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		while (pi.hasNext()) {
			int i = pi.next();
			System.out.println("Thread "+tid+ "  got element " + i);
			
			if (i == 5 || i == 3) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				boolean suc = pi.localBreak();
				System.out.println("--- Thread "+tid+"  broke? "+suc);
			} else if (i == 2) {
				pi.globalBreak();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread "+tid+ " finished. ");
	}

	private void testGlobalBreak() {

		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		while (pi.hasNext()) {
			int i = pi.next();
			System.out.println("Thread "+tid+ "  got element " + i);
			
			if (i == 3)
				pi.globalBreak();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread "+tid+ " finished. ");
	}

	private void testExceptions() {
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		
		boolean firstTime = true;
		
		while (pi.hasNext()) {
			try {

				if (firstTime && (tid==1||tid==3)) {
					firstTime = false;
					Object a = null;
					a.equals("");
				}
				
				int i = pi.next();
				System.out.println("Thread "+tid+ "  got element " + i);

//				if (i%3 == 0) {
//					Object a = null;
//					a.equals("");
//				}
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.out.println("Thread "+Thread.currentThread()+ " encountered exception: "+e.getCause());
				pi.register(e);
			}
		}
	}

	private void testReductions() {
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		
		while (pi.hasNext()) {
			int i = pi.next();
			System.out.println("Thread "+tid+ "  got element " + i);
			
			red.set(red.get()+i);
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread "+tid+ "'s final value is " + red.get());
	}
	
}
