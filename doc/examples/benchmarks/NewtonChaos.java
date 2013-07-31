package examplesOfPI.benchmarks;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pi.ParIterator;
import pi.ParIteratorFactory;
//import pi.ParIterator.Schedule;

public class NewtonChaos {
	
	private static Color newton(Complex z) {
        double EPSILON = 0.00000001;
        Complex four = new Complex(4, 0);
        Complex one  = new Complex(1, 0);
        
        Complex root1 = new Complex( 1,  0);
        Complex root2 = new Complex(-1,  0);
        Complex root3 = new Complex( 0,  1);
        Complex root4 = new Complex( 0, -1);

        for (int i = 0; i < 100; i++) {
            Complex f  = z.times(z).times(z).times(z).minus(one);
            Complex fp = four.times(z).times(z).times(z);
            z = z.minus(f.divides(fp));
            if (z.minus(root1).abs() <= EPSILON) return Color.WHITE;
            if (z.minus(root2).abs() <= EPSILON) return Color.RED;
            if (z.minus(root3).abs() <= EPSILON) return Color.GREEN;
            if (z.minus(root4).abs() <= EPSILON) return Color.BLUE;
        } 
        return Color.BLACK;
    }
	
	private static Color partialNewton(Complex z) {
        double EPSILON = 0.00000001;
        Complex four = new Complex(4, 0);
        Complex one  = new Complex(1, 0);
        
        Complex root1 = new Complex( 1,  0);
        Complex root2 = new Complex(-1,  0);
        Complex root3 = new Complex( 0,  1);
        Complex root4 = new Complex( 0, -1);
        
//        for (int i = 0; i < 1; i++) {
            Complex f  = z.times(z).times(z).times(z).minus(one);
            Complex fp = four.times(z).times(z).times(z);
            z = z.minus(f.divides(fp));  //-- remove this if want less computation
            if (z.minus(root1).abs() <= EPSILON) return Color.WHITE;
            if (z.minus(root2).abs() <= EPSILON) return Color.RED;
            if (z.minus(root3).abs() <= EPSILON) return Color.GREEN;
            if (z.minus(root4).abs() <= EPSILON) return Color.BLUE;
//        }
        return Color.BLACK;
    }
	
	public void performNewton(int N) {
  	  double xmin   = -1.0;
        double ymin   = -1.0;
        double width  =  2.0;
        double height =  2.0;
        
        if (N==0) {
        	for (int i = 0; i < 100; i++) {
                double x = xmin + i * width  / N;
                double y = ymin + x * height / N;
        	}
        }
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double x = xmin + i * width  / N;
                double y = ymin + j * height / N;
                Complex z = new Complex(x, y);
                partialNewton(z);
//                newton(z);
            }
        }
	}
	
	private static void storeIntoRandomFile(String filename, int maxNValue, int numIterations) {
		BufferedWriter out = null;
		try {
			FileWriter fstream = new FileWriter(filename);
	        out = new BufferedWriter(fstream);
	    } catch (Exception e){
	    	System.err.println("Error: " + e.getMessage());
	    	System.exit(0);
	    }
	    for (int i = 0; i < numIterations; i++) {
			int r = (int)(Math.random()*maxNValue)+1;
			try {
				out.write(String.valueOf(r));
				out.write(System.getProperty( "line.separator" ));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	private static ArrayList<Integer> getRandomFromFile(String filename) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));
		} catch (IOException e) {
			System.out.println("Problem opening file: " + filename);
			System.exit(0);
		}
		
		String str = null;
		try {
			while ((str = in.readLine()) != null) {
				list.add(Integer.parseInt(str));
			}
			in.close();
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private static ArrayList<Integer> getSameSizes(int nValue, int numIterations) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < numIterations; i++) {
			list.add(nValue);
		}
		return list;
	}
	
	private static ArrayList<Integer> getDecreasing(int maxNValue, int numIterations) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		double minNValue = 0;
		for (int i = 0; i < numIterations; i++) {
			double proportion = 1.0 * (numIterations-i) / numIterations; 
			proportion = (maxNValue-minNValue)*proportion + minNValue;
			list.add((int)proportion);
		}
		return list;
	}
	
//	public static void main(String[] args) {
////		
////		if (true) {
////			storeIntoRandomFile("RandomNumbers", 5, 4096);
////			System.out.println("created random numbers .. ");
////			System.exit(0);
////		}
//		
//		String app = "lock";
//		app = "pi";
//		int nValue = 1;
////		int schedule = ParIterator.STATIC_SCHEDULE;
////		int schedule = ParIterator.GUIDED_SCHEDULE;
//	//	Schedule schedule = ParIterator.Schedule.DYNAMIC;
//		int chunk = 1;
////		int chunk = ParIterator.DEFAULT_CHUNKSIZE;
//		int numThreads = 2;
//		int numIterations = 100000; //1500000;//4096; // // 150000;
//		ArrayList<Integer> list = null;
//		
//		boolean debugging = args.length == 0;
//		
//		try {
//			if (!debugging) {		//-- hard-coded for debugging
//				app = args[0];
//				nValue = Integer.parseInt(args[1]);
//				numIterations = Integer.parseInt(args[2]);
//			}
//			
//			if (nValue < 0)
//				list = getDecreasing(Math.abs(nValue), numIterations);
//			else
//				list = getSameSizes(nValue, numIterations);
//			
////			list = getRandomFromFile("RandomNumbers");
////			nValue = 5;
////			numIterations = list.size();
//			System.gc();
//			
//			
//			NewtonChaos newton = new NewtonChaos();
//			
//			if (app.equals("seq")) {
//				
//				//-- start timing...
//				long start = System.currentTimeMillis();
//				
//				Iterator<Integer> it = list.iterator();
//				while (it.hasNext()) {
//					int n = it.next();
//					newton.performNewton(n);
//				}
//				
//				//-- end timing...
//				long end = System.currentTimeMillis();
//				System.out.println("Sequential time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				
//			} else if (app.equals("pi")) {
//				
////				if (!debugging) {
////					switch (Integer.parseInt(args[3])) {
////					case 0:
////						schedule = Schedule.DYNAMIC;
////						break;
////					case 1:
////						schedule = Schedule.GUIDED;
////						break;
////					case 2:
////						schedule = Schedule.STATIC;
////						break;
////					default:
////						break;
////					}
////					chunk = Integer.parseInt(args[4]);
////					numThreads = Integer.parseInt(args[5]);
////				}
//				
//				//-- start timing...
//				long start = System.currentTimeMillis();
//				
//				ParIterator<Integer> pi = ParIteratorFactory.createParIterator(list, numThreads, ParIterator.DYNAMIC_SCHEDULE, chunk);
//				
//				NewtonThreadPI[] threads = new NewtonThreadPI[numThreads];
//				for (int t = 0; t < numThreads; t++) {
//					threads[t] = new NewtonThreadPI(newton, pi);
//					threads[t].start();
//				}
//				
//				for (int t = 0; t < numThreads; t++) 
//					threads[t].join();
//
//				//-- end timing...
//				long end = System.currentTimeMillis();
//				System.out.println("PI time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				
//			} else if (app.equals("lock") || app.equals("fairlock")) {
//				
//				boolean fair = app.equals("fairlock");
//				
//				if (!debugging)
//					numThreads = Integer.parseInt(args[3]);
//				
//				//-- start timing...
//				long start = System.currentTimeMillis();
//				///////////////////////////////////////////////////
//				//NewtonThreadLock[] threads = new NewtonThreadLock[numThreads];
//				//Iterator<Integer> it = list.iterator();
//				
//				Lock lock = new ReentrantLock(fair);
//				
//				//for (int t = 0; t < numThreads; t++) {
//					//threads[t] = new NewtonThreadLock(newton, it, lock);
//					//threads[t].start();
//				//}
//				
//				//for (int t = 0; t < numThreads; t++) 
//					//threads[t].join();
//				
//				//-- end timing...
//				long end = System.currentTimeMillis();
//				if (fair)
//					System.out.println("fairlock time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				else
//					System.out.println("lock time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				
//			} else if (app.equals("cc")) {
//
//				if (!debugging)
//					numThreads = Integer.parseInt(args[3]);
//				
//				//-- start timing...
//				long start = System.currentTimeMillis();
//				
//				//NewtonThreadCCol[] threads = new NewtonThreadCCol[numThreads];
//				ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>(list);
//				
//			//	for (int t = 0; t < numThreads; t++) {
//			//		threads[t] = new NewtonThreadCCol(newton, queue);
//				//	threads[t].start();
//				//}
//				
//				//for (int t = 0; t < numThreads; t++) 
//					//threads[t].join();
//				
//				//-- end timing...
//				long end = System.currentTimeMillis();
//				System.out.println("ConcCollection time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				
//			} else if (app.equals("man")) {
//				
//				if (!debugging)
//					numThreads = Integer.parseInt(args[3]);
//				
//				//-- start timing...
//			//	long start = System.currentTimeMillis();
//				
//				//NewtonThreadManual[] threads = new NewtonThreadManual[numThreads];
//				int chunkSize = (int)Math.ceil((list.size()*1.0) / numThreads);
//				
//				for (int t = 0; t < numThreads; t++) {
//					List<Integer> priList;
//					if (t != numThreads-1) {
//						priList = new ArrayList<Integer>(list.subList(t*chunkSize, (t+1)*chunkSize)); 
//					} else {
//						priList = new ArrayList<Integer>(list.subList(t*chunkSize, list.size()));
//					}
//				//	threads[t] = new NewtonThreadManual(newton, priList, t);
//					//threads[t].start();
//				}
//				
//				//for (int t = 0; t < numThreads; t++)
//					//threads[t].join();
//				
//				//-- end timing...
//			//	long end = System.currentTimeMillis();
//			//	System.out.println("ManualDecomp time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				
//			} else if (app.equals("sync")) {
//				
//				if (!debugging)
//					numThreads = Integer.parseInt(args[3]);
//				
//				//-- start timing...
//				long start = System.currentTimeMillis();
//				
//		//		NewtonThreadSync[] threads = new NewtonThreadSync[numThreads];
//				Iterator<Integer> it = list.iterator();
//				
//			//	NewtonThreadSync.it = it;
//				
//				//for (int t = 0; t < numThreads; t++) {
//					//threads[t] = new NewtonThreadSync(newton);
//					//threads[t].start();
//				//}
//				
//				//for (int t = 0; t < numThreads; t++) 
//					//threads[t].join();
//				
//				//-- end timing...
//				long end = System.currentTimeMillis();
//				System.out.println("sync time for N-Value="+nValue+" and numIterations="+numIterations+" is "+(end-start));
//				
//				
//			} else if (app.equals("generate ")) {
//				
//			} else {
//				Integer.parseInt("force error");
//			}
//		} catch (Exception e) {
//			System.out.println("Usage: (seq|pi|lock|fairlock|sync|cc|man) ([-]nvalue) (numIterations) [schedule] [chunk] [numThreads]");
//			System.exit(0);
//		}
//	}
}

