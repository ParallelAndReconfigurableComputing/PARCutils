package svgparser;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pi.reductions.Reducible;
import pi.reductions.Reduction;

import svgparser.shapes.Shape;
import svgparser.shapes.Shape.ShapeType;
import svgparser.threads.SVGGuessingThread;
import svgparser.threads.SVGGuessingThreadMyNode;
import svgparser.threads.SVGSequential;

import collections.Node;
import collections.pi.DomParIterator;
import collections.pi.NodeParIterator;




public class Main {
	
//	private static void firstTest() {
//		int numThreads = 1;
//		
//    	/*
//    	 *  - read SVG file using DOM
//    	 *  - convert DOM to SVGElement tree
//    	 *  - get PI for the SVGElement tree
//    	 * 	- call full-GC to rid of DOM junk
//    	 * 	- parallel traverse the SVGElements (either GElement or a PathElement)  
//    	 * 		- if PathElement, determine color, shape, etc
//    	 * 		- regardless of element type, determine it's depth (from parent's depth).. 
//    	 * 			- each thread stores the node with highest depth (i.e. Reducible<SVGElement> according to depth)
//    	 * 	- reduce the shapes, colors, etc
//    	 */
//    	
//    	SVGParser svg = new SVGParser("/home/lxuser/SVNRepository/development/SVGParser/svgfiles/custom.svg");
//    	
//    	Node<Element> tree = svg.getAsTree();
//    	NodeParIterator<Element> pi = new NodeParIterator<Element>(tree, numThreads);
//    	SVGWorker[] workers = new SVGWorker[numThreads];
//    	for (int i = 0; i < numThreads; i++) {
//    		workers[i] = new SVGWorker(pi);
//    		workers[i].start();
//    	}
//    	for (int i = 0; i < numThreads; i++) {
//    		try {
//				workers[i].join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//    	}
//	}
	
	private static void testGuessingDomParIterator(int numThreads) {
    	
    	SVGParser svg = new SVGParser(dir+"output.svg");
    	Document document = svg.getDocument();
    	
    	Reducible<Integer> numSquares = new Reducible<Integer>(0);
    	Reducible<Integer> numRectangles = new Reducible<Integer>(0);
    	Reducible<Integer> numCircles = new Reducible<Integer>(0);
    	Reducible<Integer> numEllipses = new Reducible<Integer>(0);
    	Reducible<Integer> numTriangles = new Reducible<Integer>(0);
    	Reducible<Integer> numUnknowns = new Reducible<Integer>(0);
    	Reducible<Shape> biggestShape = new Reducible<Shape>();
    	Reducible<Shape> smallestShape = new Reducible<Shape>();
    	
    	//  TODO   where to start measuring from?
    	long start = System.currentTimeMillis();
    	
    	DomParIterator pi = new DomParIterator(document, numThreads);

    	SVGGuessingThread[] workers = new SVGGuessingThread[numThreads];
    	for (int i = 0; i < numThreads; i++) {
    		workers[i] = new SVGGuessingThread(pi, document);

    		workers[i].setBiggestShape(biggestShape);
    		workers[i].setSmallestShape(smallestShape);
    		workers[i].setNumCircles(numCircles);
    		workers[i].setNumSquares(numSquares);
    		workers[i].setNumRectangles(numRectangles);
    		workers[i].setNumEllipses(numEllipses);
    		workers[i].setNumTriangles(numTriangles);
    		workers[i].setNumUnknowns(numUnknowns);
    		workers[i].start();
    	}
    	for (int i = 0; i < numThreads; i++) {
    		try {
				workers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	long end = System.currentTimeMillis();
    	
    	SVGMaker.saveSVGFile(dir+"outputChanged.svg", document);

    	int sq = numSquares.reduce(Reduction.IntegerSUM);
    	int re = numRectangles.reduce(Reduction.IntegerSUM);
    	int ci = numCircles.reduce(Reduction.IntegerSUM);
    	int el = numEllipses.reduce(Reduction.IntegerSUM);
    	int tr = numTriangles.reduce(Reduction.IntegerSUM);
    	int un = numUnknowns.reduce(Reduction.IntegerSUM);
    	int totalShapes = sq+re+ci+el+tr+un;
    	Shape smallest = smallestShape.reduce(new Reduction<Shape>() {
			@Override
			public Shape reduction(Shape first, Shape second) {
				if (first.getArea() < second.getArea())
					return first;
				else 
					return second;
			}
    	});
    	Shape biggest = biggestShape.reduce(new Reduction<Shape>() {
			@Override
			public Shape reduction(Shape first, Shape second) {
				if (first.getArea() > second.getArea())
					return first;
				else 
					return second;
			}
    	});
    	
//    	System.out.println("  num sq: "+sq);
//    	System.out.println("  num re: "+re);
//    	System.out.println("  num ci: "+ci);
//    	System.out.println("  num el: "+el);
//    	System.out.println("  num tr: "+tr);
//    	System.out.println("  num un: "+un);
//    	System.out.println("  BIGGEST shape is a "+biggest.getType()+" with area: "+biggest.getArea());
//    	System.out.println("  SMALLEST shape is a "+smallest.getType()+" with area: "+smallest.getArea());
//    	System.out.println("  Total number of shapes: "+(totalShapes));
//    	System.out.println("  -------------------");
    	
    	System.out.println("DomPI Total time, "+numThreads+" threads, "+totalShapes+" shapes: "+(end-start));
	}
	
	private static void testGuessing(int numThreads) {
    	
    	SVGParser svg = new SVGParser(dir+"output.svg");
    	Document document = svg.getDocument();
    	
    	//  TODO   where to start measuring from?
    	long start = System.currentTimeMillis();
    	
    	Node<Element> tree = svg.getAsTree();
    	NodeParIterator<Element> pi = new NodeParIterator<Element>(tree, numThreads);
//    	DomParIterator pi = new DomParIterator(document, numThreads);
    	
    	Reducible<Integer> numSquares = new Reducible<Integer>(0);
    	Reducible<Integer> numRectangles = new Reducible<Integer>(0);
    	Reducible<Integer> numCircles = new Reducible<Integer>(0);
    	Reducible<Integer> numEllipses = new Reducible<Integer>(0);
    	Reducible<Integer> numTriangles = new Reducible<Integer>(0);
    	Reducible<Integer> numUnknowns = new Reducible<Integer>(0);
    	Reducible<Shape> biggestShape = new Reducible<Shape>();
    	Reducible<Shape> smallestShape = new Reducible<Shape>();
    	
    	SVGGuessingThreadMyNode[] workers = new SVGGuessingThreadMyNode[numThreads];
//    	SVGGuessingThread[] workers = new SVGGuessingThread[numThreads];
    	for (int i = 0; i < numThreads; i++) {
    		workers[i] = new SVGGuessingThreadMyNode(pi, document);
//    		workers[i] = new SVGGuessingThread(pi, document);
    		
    		workers[i].setBiggestShape(biggestShape);
    		workers[i].setSmallestShape(smallestShape);
    		workers[i].setNumCircles(numCircles);
    		workers[i].setNumSquares(numSquares);
    		workers[i].setNumRectangles(numRectangles);
    		workers[i].setNumEllipses(numEllipses);
    		workers[i].setNumTriangles(numTriangles);
    		workers[i].setNumUnknowns(numUnknowns);
    		
    		workers[i].start();
    	}
    	for (int i = 0; i < numThreads; i++) {
    		try {
				workers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	long end = System.currentTimeMillis();
    	
    	SVGMaker.saveSVGFile(dir+"outputChanged.svg", document);
    	
    	int sq = numSquares.reduce(Reduction.IntegerSUM);
    	int re = numRectangles.reduce(Reduction.IntegerSUM);
    	int ci = numCircles.reduce(Reduction.IntegerSUM);
    	int el = numEllipses.reduce(Reduction.IntegerSUM);
    	int tr = numTriangles.reduce(Reduction.IntegerSUM);
    	int un = numUnknowns.reduce(Reduction.IntegerSUM);
    	Shape smallest = smallestShape.reduce(new Reduction<Shape>() {
			@Override
			public Shape reduction(Shape first, Shape second) {
				if (first.getArea() < second.getArea())
					return first;
				else 
					return second;
			}
    	});
    	Shape biggest = biggestShape.reduce(new Reduction<Shape>() {
			@Override
			public Shape reduction(Shape first, Shape second) {
				if (first.getArea() > second.getArea())
					return first;
				else 
					return second;
			}
    	});
    	
    	System.out.println("  num sq: "+sq);
    	System.out.println("  num re: "+re);
    	System.out.println("  num ci: "+ci);
    	System.out.println("  num el: "+el);
    	System.out.println("  num tr: "+tr);
    	System.out.println("  num un: "+un);
    	System.out.println("  BIGGEST shape is a "+biggest.getType()+" with area: "+biggest.getArea());
    	System.out.println("  SMALLEST shape is a "+smallest.getType()+" with area: "+smallest.getArea());
    	System.out.println("  Total number of shapes: "+(sq+re+ci+el+tr+un));
    	System.out.println("  -------------------");

    	System.out.println("Total time: "+(end-start));
	}
	
	private static void createSVG(int numShapes, int degree, ShapeType makeType) {
    	String output = dir+"/output.svg";
    	SVGMaker.makeRandomShapesInGroupTree(new File(output), numShapes, degree, makeType);
    	System.out.println("  finished creating shapes");
	}
	
	static String dir = "/home/lxuser/SVNRepository/development/SVGParser/svgfiles/";
	
    public static void main (String argv []) {
//    	firstTest();
    	
    	dir = "/home/nas/performance_testing/java_pi_for_journal_paper/files/";	//-- sweet16
    	
    	String app = "create";
//    	app = "seq";
    	app = "dpi";
    	int numThreads = 2;
    	
    	int degree = 10;
    	int numShapes = 1000;
    	ShapeType makeType = ShapeType.UNKNOWN;
    	
    	if (argv.length != 0) {
    		try {
    			app = argv[0];
    			if (app.equals("create")) {
    				numShapes = Integer.parseInt(argv[1]);
    				degree = Integer.parseInt(argv[2]);
    				if (argv.length == 4) {
    					if (argv[3].equals("t")) {
    						makeType = ShapeType.TRIANGLE;
    					} else if (argv[3].equals("r")) {
    						makeType = ShapeType.RECTANGLE;
    					} else if (argv[3].equals("e")) {
    						makeType = ShapeType.ELLIPSE;
    					} else if (argv[3].equals("m")) {
    						makeType = ShapeType.UNKNOWN;
    					}
    				}
    	    	} else if (app.equals("seq")) {
    	    		//-- nothing to do 
    	    	} else if (app.equals("dpi")) {
    	    		numThreads = Integer.parseInt(argv[1]);
    	    	} else if (app.equals("npi")) {
    	    		numThreads = Integer.parseInt(argv[1]);
    	    	} else {
    	    		Integer.parseInt("Force error");
    	    	}
    		} catch (Exception e) {
    			System.out.println("Usage 1: create <numShapes> <degree> [t|r|e|m]");
    			System.out.println("Usage 2: (seq|dpi|npi) <numThreads> ");
    			System.exit(0);
    		}
    	}
    	
//    	if (true) {
//    		System.out.println(" time = ...");
//    		return;
//    	}
    	
    	System.gc();
    	
    	if (app.equals("create")) {
        	createSVG(numShapes, degree, makeType);
    	} else if (app.equals("seq")) {
    		testGuessingSequential();
    	} else if (app.equals("dpi")) {
        	testGuessingDomParIterator(numThreads);
    	} else if (app.equals("npi")) {
        	testGuessing(numThreads);
    	}
    }
    
	private static void testGuessingSequential() {
		
    	SVGParser svg = new SVGParser(dir+"output.svg");
    	Document document = svg.getDocument();
    	
    	//  TODO   where to start measuring from?
    	long start = System.currentTimeMillis();
    	
    	SVGSequential svgseq = new SVGSequential(document);
    	svgseq.run();
    	
    	long end = System.currentTimeMillis();
    	
    	SVGMaker.saveSVGFile(dir+"outputChanged.svg", document);
    	
    	System.out.println("Sequential Total time: "+(end-start));
	}
}
