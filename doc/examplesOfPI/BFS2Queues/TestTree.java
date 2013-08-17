package examplesOfPI.BFS2Queues;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * 
 * Date Created: 16 April 2009
 * Last Modified: 27 May 2009
 * 
 * Tests the sequential and parallel BFS Algorithm which is implemented using two
 * queues and the Parallel Iterator.
 * 
 * @author Lama Akeila
 **/
public class TestTree {

	public static TreeCode mainTree = new TreeCode("Tree");
 
	public static GraphAdapter<TreeCode> adapter;

	public static BFSalgorithm treeSearch;

	private static int numOfThread = 1; // Choose the number of threads here

	private static String treeType = "Deep";

	private static boolean deep = false;

	private static boolean wide = false;

	private static int size = 50;

	private static boolean deepWide = false;

	private static boolean isSequential = false;// switch to true if you want to

	private static int wideNodes,deepNodes = 0;
	
	private static int maxIteration = 15;
	// test for one thread only

	public TestTree() {

	}

	public static void main(String[] args) {

		memoryInfo();
		if (args.length > 1) {
			isSequential = args[0].equals("0");
			if (!isSequential)
				numOfThread = Integer.parseInt(args[1]);
			deep = args[2].equals("deep");
			wide = args[2].equals("wide");
			deepWide = args[2].equals("deepWide");
			size = Integer.parseInt(args[3]);
			maxIteration = Integer.parseInt(args[4]);
		}

		if (isSequential) {
			numOfThread = 1;
		}
		System.out.println("NumThread: " + numOfThread);

		if (wide) {
			mainTree = createWideTree(size); // 49(2000) - 37(1500) - 25(1000)
			// -13(500)-change the input
			// parameter
		} else if (deep) {
			mainTree = createDeepTree(size); // 82 is the limit which is 3403
			// node
		} else if (deepWide) {
			mainTree = createWideDeepTree(size, size + 10); // 3732 nodes
		} else {

			//tree = createRootTree();
			// pass num of nodes and degree
			//tree = createTree(size,100);
			mainTree = createCustomisedTree(size,100);
		}

		adapter = new GraphAdapter<TreeCode>("DAG", mainTree);
		//adapter.setVal(maxIteration);
		//System.out.println("Adapteeeeeeeeer size: "+ adapter.getAllChildrenList().size());
		
		run(adapter);

	}
	
	private static TreeCode createRootTree() {
		TreeCode tree = new TreeCode("root");

		return tree;
	}

	private static void memoryInfo() {
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.
		// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();

		// Get amount of free memory within the heap in bytes. This size will increase
		// after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();

		System.out.println("HeapSize in bytes is: " + heapSize
				+ " | HeapMaxSize in bytes: " + heapMaxSize
				+ " | HeapFreeSize in bytes: " + heapFreeSize);
	}

	public static void run(GraphAdapter tree) {

		long start = 0;
		long end = 0;
		ArrayList nodesList = new ArrayList();
		//Iterator<GraphAdapter> nodesIterator = tree.getAllChildren();

//		while (nodesIterator.hasNext()) {
//			GraphAdapter node = nodesIterator.next();
//			nodesList.add(node);
//		}

		if (!isSequential) {
			System.out.println("Parallel Version of BFS with 2 Queues ");
			TreeCode root = mainTree;
			treeSearch = new BFSalgorithm(tree, root, numOfThread);
			ConcurrentLinkedQueue<GraphAdapter> mywork = new ConcurrentLinkedQueue<GraphAdapter>();
			start = System.currentTimeMillis();
			mywork = treeSearch.parallelBFS(root,mywork, "ss");
			end = System.currentTimeMillis();
			
			System.out.println("Total Final Num of Elements: "
					+ mywork.size());
			
		} else {
			
			System.out.println("Sequential Version");
			TreeCode root = mainTree;
			treeSearch = new BFSalgorithm(tree,root);
			ArrayList<GraphAdapter> seqMywork = new ArrayList<GraphAdapter>();
			start = System.currentTimeMillis();
			treeSearch.seqBFS(root,seqMywork, "ss");
			end = System.currentTimeMillis();
			System.out.println("Main Thread--" + "Num of Elements: "
					+ seqMywork.size());
			Iterator<GraphAdapter> it = seqMywork.iterator();
			//			while (it.hasNext()) {
			//				GraphAdapter node = it.next();
			//				System.out.print("--" + node.getElement().toString());
			//			}
			//			System.out.println("");
		}

		System.out.println("Time taken is: " + (end - start) + " ms \n");
		memoryInfo();

	}
	
	public static TreeCode createCustomisedTree(int totalNodes,
			int breadth) {
		int nodeCount = 0;
		System.out.println("Created Customized Tree.. Degree: " + breadth + " -TotalNodes: " + totalNodes);
		TreeCode treeRoot = new TreeCode("root");
		treeRoot.setVal(maxIteration);
		// Creating first Level of the Tree
		ArrayList<TreeCode> firstLevel = new ArrayList<TreeCode>();
		for (int i = 0; i < breadth; i++) {
			String name = "root" + i;
			TreeCode node = new TreeCode(name);
			node.setVal(maxIteration);
			if (nodeCount == totalNodes)
				break;
			try {
				treeRoot.addChild(node);
			} catch (CyclicTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//graph.add(node);
			nodeCount++;
			firstLevel.add(node);
			// System.out.println("Added Node: "+ node.name().toString());

		}
		ArrayList<TreeCode> latestLevel = new ArrayList<TreeCode>();
		latestLevel.addAll(firstLevel);
		while (!latestLevel.isEmpty()) {
			firstLevel = new ArrayList<TreeCode>();
			Iterator<TreeCode> it = latestLevel.iterator();
			while (it.hasNext()) {
				if (nodeCount == totalNodes)
					break;
				TreeCode node = it.next();
				for (int i = 0; i < breadth; i++) {

					if (nodeCount == totalNodes)
						break;

					String name = node.getElement().toString()+ "-" + i;
					TreeCode newNode = new TreeCode(name);
					try {
						node.addChild(newNode);
					} catch (CyclicTreeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newNode.setVal(maxIteration);
					//graph.add(newNode);
					nodeCount++;
					firstLevel.add(newNode);

				}
			}
			latestLevel = firstLevel;
		}

		//System.out.println("Size of graph vertices " + graph.sizeVertices());
		//System.out.println("Size of graph edges " + graph.sizeEdges());

		return treeRoot;
	}
	
	public static TreeCode createTree(int numNodes, int degree) {
		System.out.println("Created Customised Tree with " + numNodes + " Nodes and degree "+ degree);
		TreeCode wideTree = new TreeCode("root");
		wideTree.setVal(maxIteration);
		ArrayList<TreeCode> latestNodes = new ArrayList<TreeCode>();
		int counter = 0;
		// left
		if(degree+1 < numNodes){
			for (int i = 0; i < numNodes; i++) {
				String name = i + "";
				TreeCode node = new TreeCode(name);
				node.setVal(maxIteration);
				try {
					// Adding the tree to the root
					wideTree.addChild(node);
					latestNodes.add(node);
					counter ++;
				} catch (CyclicTreeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else {
			System.out.println("Degree is LESS than Number of nodes");
		}
		while(counter < numNodes){
			System.out.println("Goes Here");
			Iterator<TreeCode> it = latestNodes.iterator();
			ArrayList<TreeCode> nodesLevel = new ArrayList<TreeCode>();
			while(it.hasNext()){
				TreeCode curNode = it.next();
				String name = curNode.getElement().toString();
				for(int i = 0; i < degree;i++){
					TreeCode child = new TreeCode(name+"-child-"+i);
					try {
						curNode.addChild(child);
						nodesLevel.add(child);
						counter ++;
						if(counter == numNodes)
							break;
					} catch (CyclicTreeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(counter == numNodes)
					break;
			
			}
			latestNodes = new ArrayList<TreeCode>();
			latestNodes = nodesLevel;
		}
		System.out.println("Created Tree with "
				+ (wideTree.getNumAllChildren() + 1) + " nodes");

		return wideTree;
	}

	/***************************************************************************
	 * This method returns a tree of size 41 nodes
	 **************************************************************************/
	private static TreeCode createTree1() {
		System.out.println("Created Tree 1");
		TreeCode tree = new TreeCode("root");
		// crearting nodes
		TreeCode l1 = new TreeCode("l1");
		TreeCode l2 = new TreeCode("l2");
		TreeCode l3 = new TreeCode("l3");
		TreeCode l4 = new TreeCode("l4");
		TreeCode l5 = new TreeCode("l5");

		TreeCode l6 = new TreeCode("l6");
		TreeCode l7 = new TreeCode("l7");
		TreeCode l8 = new TreeCode("l8");
		TreeCode l9 = new TreeCode("l9");
		TreeCode l10 = new TreeCode("l10");

		TreeCode l11 = new TreeCode("l11");
		TreeCode l12 = new TreeCode("l12");
		TreeCode l13 = new TreeCode("l13");
		TreeCode l14 = new TreeCode("l14");
		TreeCode l15 = new TreeCode("l15");

		TreeCode l16 = new TreeCode("l16");
		TreeCode l17 = new TreeCode("l17");
		TreeCode l18 = new TreeCode("l18");
		TreeCode l19 = new TreeCode("l19");
		TreeCode l20 = new TreeCode("l20");

		TreeCode r1 = new TreeCode("r1");
		TreeCode r2 = new TreeCode("r2");
		TreeCode r3 = new TreeCode("r3");
		TreeCode r4 = new TreeCode("r4");
		TreeCode r5 = new TreeCode("r5");

		TreeCode r6 = new TreeCode("r6");
		TreeCode r7 = new TreeCode("r7");
		TreeCode r8 = new TreeCode("r8");
		TreeCode r9 = new TreeCode("r9");
		TreeCode r10 = new TreeCode("r10");

		TreeCode r11 = new TreeCode("r11");
		TreeCode r12 = new TreeCode("r12");
		TreeCode r13 = new TreeCode("r13");
		TreeCode r14 = new TreeCode("r14");
		TreeCode r15 = new TreeCode("r15");

		TreeCode r16 = new TreeCode("r16");
		TreeCode r17 = new TreeCode("r17");
		TreeCode r18 = new TreeCode("r18");
		TreeCode r19 = new TreeCode("r19");
		TreeCode r20 = new TreeCode("r20");

		try {
			// adding nodes to their parents
			l1.addChild(l2);
			l1.addChild(l3);
			l1.addChild(l4);

			r1.addChild(r2);
			r1.addChild(r3);
			r1.addChild(r4);

			l2.addChild(l5);
			l2.addChild(l6);

			r2.addChild(r5);
			r2.addChild(r6);

			l3.addChild(l7);
			l3.addChild(l8);

			r3.addChild(r7);
			r3.addChild(r8);

			l4.addChild(l9);
			l4.addChild(l10);

			r4.addChild(r9);
			r4.addChild(r10);

			l5.addChild(l11);
			l6.addChild(l12);
			l7.addChild(l13);
			l8.addChild(l14);
			l9.addChild(l15);
			l10.addChild(l16);
			l11.addChild(l17);
			l17.addChild(l18);
			l18.addChild(l19);
			l18.addChild(l20);

			r5.addChild(r11);
			r6.addChild(r12);
			r7.addChild(r13);
			r8.addChild(r14);
			r9.addChild(r15);
			r10.addChild(r16);
			r16.addChild(r17);
			r17.addChild(r18);
			r18.addChild(r19);
			r18.addChild(r20);

			tree.addChild(l1);
			tree.addChild(r1);

		} catch (CyclicTreeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Created Tree with "
				+ (tree.getNumAllChildren() + 1) + " nodes");
		return tree;
	}


	private static TreeCode createSmallTree() {
		System.out.println("Created Tree 2");
		TreeCode tree = new TreeCode("a");
		// crearting nodes
		TreeCode b = new TreeCode("b");
		TreeCode c = new TreeCode("c");
		TreeCode d = new TreeCode("d");
		TreeCode e = new TreeCode("e");
		TreeCode f = new TreeCode("f");

		TreeCode g = new TreeCode("g");
		TreeCode h = new TreeCode("h");
		TreeCode i = new TreeCode("i");

		try {
			// adding nodes to their parents
			b.addChild(d);
			b.addChild(e);
			c.addChild(f);
			c.addChild(g);
			f.addChild(h);
			f.addChild(i);
			tree.addChild(b);
			tree.addChild(c);

		} catch (CyclicTreeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Created Tree with "
				+ (tree.getNumAllChildren() + 1) + " nodes");
		return tree;
	}

	/***************************************************************************
	 * This method prints out each threads work(The number of iterations for
	 * each thread)
	 * 
	 **************************************************************************/
	public static void print(ArrayList<TreeCode> list, int id) {
		System.out.println("Thread " + id
				+ " completed the following iterations: " + list.size());

		for (int i = 0; i < list.size(); i++) {
			TreeCode e = list.get(i);
			System.out.print(e.getElement().toString() + ", ");
		}
		System.out.println("");

	}

	/***************************************************************************
	 * This method returns a wide GraphAdapter the size of the GraphAdapter =
	 * (40*num) nodes
	 * 
	 **************************************************************************/
	public static TreeCode createWideTree(int num) {
		System.out.println("Created Wide Tree with " + num + " duplicate(s)");
		TreeCode wideTree = new TreeCode("root");

		// left
		for (int i = 0; i < num; i++) {
			String name = i + "";
			try {
				// Adding the tree to the root
				wideTree.addChild(treeMaker(name));
			} catch (CyclicTreeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("Created Wide Tree with "
				+ (wideTree.getNumAllChildren() + 1) + " nodes");

		return wideTree;
	}
	/***************************************************************************
	 * This method creates and returns a GraphAdapter with size of 40 nodes
	 * 
	 **************************************************************************/
	private static TreeCode treeMaker(String name) {
		TreeCode GraphAdapter = new TreeCode("root" + name);

		TreeCode l1 = new TreeCode("l1-" + name);
		TreeCode l2 = new TreeCode("l2-" + name);
		TreeCode l3 = new TreeCode("l3-" + name);
		TreeCode l4 = new TreeCode("l4-" + name);
		TreeCode l5 = new TreeCode("l5-" + name);

		TreeCode l6 = new TreeCode("l6-" + name);
		TreeCode l7 = new TreeCode("l7-" + name);
		TreeCode l8 = new TreeCode("l8-" + name);
		TreeCode l9 = new TreeCode("l9-" + name);
		TreeCode l10 = new TreeCode("l10-" + name);

		TreeCode l11 = new TreeCode("l11-" + name);
		TreeCode l12 = new TreeCode("l12-" + name);
		TreeCode l13 = new TreeCode("l13-" + name);
		TreeCode l14 = new TreeCode("l14-" + name);
		TreeCode l15 = new TreeCode("l15-" + name);

		TreeCode l16 = new TreeCode("l16-" + name);
		TreeCode l17 = new TreeCode("l17-" + name);
		TreeCode l18 = new TreeCode("l18-" + name);
		TreeCode l19 = new TreeCode("l19-" + name);
		TreeCode l20 = new TreeCode("l20-" + name);

		TreeCode r1 = new TreeCode("r1-" + name);
		TreeCode r2 = new TreeCode("r2-" + name);
		TreeCode r3 = new TreeCode("r3-" + name);
		TreeCode r4 = new TreeCode("r4-" + name);
		TreeCode r5 = new TreeCode("r5-" + name);

		TreeCode r6 = new TreeCode("r6-" + name);
		TreeCode r7 = new TreeCode("r7-" + name);
		TreeCode r8 = new TreeCode("r8-" + name);
		TreeCode r9 = new TreeCode("r9-" + name);
		TreeCode r10 = new TreeCode("r10-" + name);

		TreeCode r11 = new TreeCode("r11-" + name);
		TreeCode r12 = new TreeCode("r12-" + name);
		TreeCode r13 = new TreeCode("r13-" + name);
		TreeCode r14 = new TreeCode("r14-" + name);
		TreeCode r15 = new TreeCode("r15-" + name);

		TreeCode r16 = new TreeCode("r16-" + name);
		TreeCode r17 = new TreeCode("r17-" + name);
		TreeCode r18 = new TreeCode("r18-" + name);
		TreeCode r19 = new TreeCode("r19-" + name);
		TreeCode r20 = new TreeCode("r20-" + name);

		try {
			l1.addChild(l2);
			l1.addChild(l3);
			l1.addChild(l4);

			r1.addChild(r2);
			r1.addChild(r3);
			r1.addChild(r4);

			l2.addChild(l5);
			l2.addChild(l6);

			r2.addChild(r5);
			r2.addChild(r6);

			l3.addChild(l7);
			l3.addChild(l8);

			r3.addChild(r7);
			r3.addChild(r8);

			l4.addChild(l9);
			l4.addChild(l10);

			r4.addChild(r9);
			r4.addChild(r10);

			l5.addChild(l11);
			l6.addChild(l12);
			l7.addChild(l13);
			l8.addChild(l14);
			l9.addChild(l15);
			l10.addChild(l16);
			l11.addChild(l17);
			l17.addChild(l18);
			l18.addChild(l19);
			l18.addChild(l20);

			r5.addChild(r11);
			r6.addChild(r12);
			r7.addChild(r13);
			r8.addChild(r14);
			r9.addChild(r15);
			r10.addChild(r16);
			r16.addChild(r17);
			r17.addChild(r18);
			r18.addChild(r19);
			r18.addChild(r20);

			GraphAdapter.addChild(l1);
			GraphAdapter.addChild(r1);

		} catch (CyclicTreeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return GraphAdapter;
	}


	/***************************************************************************
	 * This method creates a deep GraphAdapter of size multiple of 40's takes a
	 * input parameter as number and output a deep GraphAdapter of size =
	 * (40*num) nodes
	 **************************************************************************/
	public static TreeCode createDeepTree(int num) {
		System.out.println("Created Deep Tree with " + num + " duplicate(s)");
		TreeCode deepTree = new TreeCode("root");

		try {
			deepTree.addChild(treeMaker("0"));

			for (int n = 0; n < num; n++) {
				ArrayList list = deepTree.getChildrenList();
				TreeCode t = (TreeCode) list.get(list.size() - 1);
				t.addChild(treeMaker(n + ""));
			}

		} catch (CyclicTreeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Deep Tree has "
				+ (deepTree.getNumAllChildren() + 1) + " nodes");

		return deepTree;
	}
	/***************************************************************************
	 * This method outputs a Wide-Deep tree the size of the tree =
	 * 40*(num1+num2) nodes
	 **************************************************************************/
	public static TreeCode createWideDeepTree(int num1, int num2) {
		System.out.println("Created WideDeep Tree with " + num1
				+ " duplicate(s) wide and " + num2 + " duplicate(s) deep");
		TreeCode wideTree = createWideTree(num1);
		TreeCode deepTree = createDeepTree(num2);
		try {
			wideTree.addChild(deepTree);
		} catch (CyclicTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DeepWide Tree has "
				+ (wideTree.getNumAllChildren() + 1) + " nodes");

		return wideTree;
	}


}

