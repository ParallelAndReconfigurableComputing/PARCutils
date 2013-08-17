package svgparser.threads;

import org.w3c.dom.Element;

import pi.UniqueThreadIdGenerator;

import collections.Node;
import collections.pi.NodeParIterator;

public class SVGWorker extends Thread {
	
	private NodeParIterator<Element> pi;
	
	public SVGWorker(NodeParIterator<Element> pi) {
		this.pi = pi;
	}
	
	@Override
	public void run() {
		int tid = UniqueThreadIdGenerator.getCurrentThreadId();
		
		while (pi.hasNext()) {
			Node<Element> n = pi.next();
			System.out.println("Thread "+tid+" got element: "+n.getValue().getAttribute("id"));
			
//			if (n.getValue().getAttribute("id").equals("g2.1")) {
//				pi.remove();
//			}
			
			try {
//				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
