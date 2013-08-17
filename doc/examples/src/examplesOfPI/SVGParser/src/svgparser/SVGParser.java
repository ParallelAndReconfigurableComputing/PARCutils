package svgparser;

import java.io.File;

import org.w3c.dom.*;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class SVGParser {
	
	private String file;
	private Document doc = null;
	private collections.Node<Element> tree = null;
	
	public SVGParser(String file) {
		this.file = file;
		getDomDocument();
	}
	
	private collections.Node<Element> recursiveCreateTreeForElement(Element value, collections.Node<Element> parent) {
		String nodeName = "["+value.getNodeName()+":"+value.getAttribute("id")+"]";
		collections.Node<Element> newNode = new collections.SimpleNode<Element>(nodeName, parent, value);
		
		NodeList children = value.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				
//				if (e.getLocalName().equals("g")) {
					recursiveCreateTreeForElement(e, newNode);
//				} else if (e.getLocalName().equals("path")) {
//					recursiveCreateTreeForElement(e, newNode);
//				}
			}
		}
		return newNode;
	}
	
	public collections.Node<Element> getAsTree() {
		Element svgroot = doc.getDocumentElement();
		tree = recursiveCreateTreeForElement(svgroot, null);
		return tree;
	}
	
	public Document getDocument() {
		return doc;
	}
	
	private void getDomDocument() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File(file));
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			DOMParser parser = new DOMParser();
			parser.parse(file);
			doc = parser.getDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
