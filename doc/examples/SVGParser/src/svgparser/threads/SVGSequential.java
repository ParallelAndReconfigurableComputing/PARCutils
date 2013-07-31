package svgparser.threads;

import java.util.Deque;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import svgparser.shapes.Circle;
import svgparser.shapes.Color;
import svgparser.shapes.Ellipse;
import svgparser.shapes.Path;
import svgparser.shapes.Rectangle;
import svgparser.shapes.Shape;
import svgparser.shapes.Square;
import svgparser.shapes.Style;

public class SVGSequential {

	private Document document;
	
	public SVGSequential(Document document) {
		this.document = document;
	}
	
	public void run() {

		int numCircles = 0;
		int numSquares = 0;
		int numEllipses = 0;
		int numRectangles = 0;
		int numTriangles = 0;
		int numUnknowns = 0;
		Shape biggestShape = null;
		Shape smallestShape = null;
		
		Deque<Node> nodesTodo = new LinkedList<Node>();
		nodesTodo.add(document.getDocumentElement());
		
		Node n = null;
		
		while ((n=nodesTodo.pollFirst()) != null) {
			
			if (!(n instanceof Element))
				continue;
			
			Element e = (Element )n;
			String id = e.getAttribute("id");
			
			//-- style 
			String defStyle = e.getAttribute("style");
			Style style = new Style(defStyle);
			Color fillColor = style.getFillColor();
			Color strokeColor = Color.BLACK;
			int strokeWidth = 1;
			if (fillColor == null) {
				// TODO 
				// -- get fill color from parent node
				
				fillColor = Color.YELLOW; 
				
				strokeColor = Color.RED;
				strokeWidth = 3;
			}
			//-- do something to the color to add computation
			
			boolean willRemove = false;
			
			if (e.getNodeName().equals("path")) {
				String d = e.getAttribute("d");
				
				Path path = new Path(d);
				Shape shape = path.getShape();
				int centerX = shape.getCenterX();
				int centerY = shape.getCenterY();
				int x = path.getBoundary().getX();
				int y = path.getBoundary().getY();
				
				if (smallestShape == null) {
					smallestShape = shape;
				} else {
					if (shape.getArea() < smallestShape.getArea())
						smallestShape = shape;
				}
				
				if (biggestShape == null) {
					biggestShape = shape;
				} else {
					if (shape.getArea() > biggestShape.getArea())
						biggestShape = shape;
				}
				
				switch (shape.getType()) {
				case CIRCLE: {
					numCircles++;
//					System.out.println(id+" is a CIRCLE");
					Circle circle = (Circle) shape;
					int radius = circle.getRadius();
					
					Element newElement = document.createElement("circle");
					newElement.setAttribute("id", id);
					newElement.setAttribute("cx", String.valueOf(centerX));
					newElement.setAttribute("cy", String.valueOf(centerY));
					newElement.setAttribute("r", String.valueOf(radius));
					newElement.setAttribute("fill", fillColor.toString());	
					newElement.setAttribute("stroke", strokeColor.toString());
					newElement.setAttribute("stroke-width", String.valueOf(strokeWidth));
					n.getParentNode().replaceChild(newElement, n);
					break;
				}
				case ELLIPSE: {
					numEllipses++;
//					System.out.println(id+" is an ELLIPSE");
					Ellipse ellipse = (Ellipse) shape;
					int radiusH = ellipse.getHorizontalRadius();
					int radiusV = ellipse.getVerticalRadius();
					
					Element newElement = document.createElement("ellipse");
					newElement.setAttribute("id", id);
					newElement.setAttribute("cx", String.valueOf(centerX));
					newElement.setAttribute("cy", String.valueOf(centerY));
					newElement.setAttribute("rx", String.valueOf(radiusH));
					newElement.setAttribute("ry", String.valueOf(radiusV));
					newElement.setAttribute("fill", fillColor.toString());	
					newElement.setAttribute("stroke", strokeColor.toString());
					newElement.setAttribute("stroke-width", String.valueOf(strokeWidth));
					n.getParentNode().replaceChild(newElement, n);
					break;
				}
				case RECTANGLE: {
					numRectangles++;
//					System.out.println(id+" is a RECTANGLE");
					Rectangle rectangle = (Rectangle) shape;
					int width = rectangle.getWidth();
					int height = rectangle.getHeight();
					
					Element newElement = document.createElement("rect");
					newElement.setAttribute("id", id);
					newElement.setAttribute("x", String.valueOf(x));
					newElement.setAttribute("y", String.valueOf(y));
					newElement.setAttribute("width", String.valueOf(width));
					newElement.setAttribute("height", String.valueOf(height));
					newElement.setAttribute("fill", fillColor.toString());	
					newElement.setAttribute("stroke", strokeColor.toString());
					newElement.setAttribute("stroke-width", String.valueOf(strokeWidth));
					n.getParentNode().replaceChild(newElement, n);
					
					break;
				}
				case SQUARE: {
					numSquares++;
//					System.out.println(id+" is a SQUARE");
					Square square = (Square) shape;
					int width = square.getWidth(); 

					Element newElement = document.createElement("rect");
					newElement.setAttribute("id", id);
					newElement.setAttribute("x", String.valueOf(x));
					newElement.setAttribute("y", String.valueOf(y));
					newElement.setAttribute("width", String.valueOf(width));
					newElement.setAttribute("height", String.valueOf(width));
					newElement.setAttribute("fill", fillColor.toString());	
					newElement.setAttribute("stroke", strokeColor.toString());
					newElement.setAttribute("stroke-width", String.valueOf(strokeWidth));
					n.getParentNode().replaceChild(newElement, n);
					
					break;
				}
				case TRIANGLE: {
					numTriangles++;
//					System.out.println(id+" is a TRIANGLE");
					
					Element newElement = document.createElement("path");
					newElement.setAttribute("id", id);
					newElement.setAttribute("d", d);
					newElement.setAttribute("fill", fillColor.toString());	
					newElement.setAttribute("stroke", strokeColor.toString());
					newElement.setAttribute("stroke-width", String.valueOf(strokeWidth));
					n.getParentNode().replaceChild(newElement, n);
					break;
				}
				case UNKNOWN: {
					numUnknowns++;
					System.out.println(id+" is ##UNKNOWN##");
					break;
				}
				default:
					throw new RuntimeException("Shape type not known! (should at least be Shape.UNKNOWN)");
				}

			} else if (e.getNodeName().equals("g")) {
				String visibility = e.getAttribute("visibility");
				if (visibility.equals("invisible")) {
					willRemove = true;
					n.getParentNode().removeChild(n);
				}
			}
			if (!willRemove) {
				NodeList children = n.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					nodesTodo.add(children.item(i));
				}
			}
		}
//    	System.out.println("  num sq: "+numSquares);
//    	System.out.println("  num re: "+numRectangles);
//    	System.out.println("  num ci: "+numCircles);
//    	System.out.println("  num el: "+numEllipses);
//    	System.out.println("  num tr: "+numTriangles);
//    	System.out.println("  num un: "+numUnknowns);
//    	int totalShapes = numRectangles + numSquares + numCircles + numEllipses + numTriangles + numUnknowns;
//    	System.out.println("  BIGGEST shape is a "+biggestShape.getType()+" with area: "+biggestShape.getArea());
//    	System.out.println("  SMALLEST shape is a "+smallestShape.getType()+" with area: "+smallestShape.getArea());
//    	System.out.println("  Seq code, Total number of shapes: "+(totalShapes));
//    	System.out.println("  -------------------");
	}
}
