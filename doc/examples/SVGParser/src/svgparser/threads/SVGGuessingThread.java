package svgparser.threads;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import pi.UniqueThreadIdGenerator;
import pi.reductions.Reducible;
import svgparser.shapes.Circle;
import svgparser.shapes.Color;
import svgparser.shapes.Ellipse;
import svgparser.shapes.Path;
import svgparser.shapes.Rectangle;
import svgparser.shapes.Shape;
import svgparser.shapes.Square;
import svgparser.shapes.Style;
//import collections.Node;
import collections.pi.DomParIterator;
import collections.pi.NodeParIterator;

/*
 * This is the ideal interface, but currently buggy.. maybe should change this such that it traverses a collections.Node tree (rather than
 * traversing the original Document elements). But not sure if this is the source of the bugs anyway. 
 */
public class SVGGuessingThread extends Thread {

	private DomParIterator pi;
	private Document document;
	
	private Reducible<Integer> numSquares = null;
	private Reducible<Integer> numRectangles = null;
	private Reducible<Integer> numCircles = null;
	private Reducible<Integer> numEllipses = null;
	private Reducible<Integer> numTriangles = null;
	private Reducible<Integer> numUnknowns = null;
	private Reducible<Shape> biggestShape = null;
	private Reducible<Shape> smallestShape = null;
	
	public void setBiggestShape(Reducible<Shape> biggestShape) {
		this.biggestShape = biggestShape;
	}

	public void setNumSquares(Reducible<Integer> numSquares) {
		this.numSquares = numSquares;
	}

	public void setNumRectangles(Reducible<Integer> numRectangles) {
		this.numRectangles = numRectangles;
	}

	public void setNumCircles(Reducible<Integer> numCircles) {
		this.numCircles = numCircles;
	}

	public void setNumEllipses(Reducible<Integer> numEllipses) {
		this.numEllipses = numEllipses;
	}

	public void setNumTriangles(Reducible<Integer> numTriangles) {
		this.numTriangles = numTriangles;
	}

	public void setNumUnknowns(Reducible<Integer> numUnknowns) {
		this.numUnknowns = numUnknowns;
	}

	public void setSmallestShape(Reducible<Shape> smallestShape) {
		this.smallestShape = smallestShape;
	}

	public SVGGuessingThread(DomParIterator pi, Document document) {
		this.pi = pi;
		this.document = document;
	}
	
	@Override
	public void run() {
//		int tid = UniqueThreadIdGenerator.getCurrentThreadId();

		int numCircles = 0;
		int numSquares = 0;
		int numEllipses = 0;
		int numRectangles = 0;
		int numTriangles = 0;
		int numUnknowns = 0;
		Shape biggestShape = null;
		Shape smallestShape = null;
		
		while (pi.hasNext()) {
			Node n = pi.next();
			
//			System.out.println("Thread "+tid+" got element: "+n.getValue().getAttribute("id"));
			
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
			
//			System.out.println("Thread "+tid+" got element: "+id);
			
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
					pi.replace(newElement);
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
					pi.replace(newElement);
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
					pi.replace(newElement);
					
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
					pi.replace(newElement);
					
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
					pi.replace(newElement);
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
				
//				System.out.println(defStyle + "   --- "+fillColor);
				
			} else if (e.getNodeName().equals("g")) {
				
				String visibility = e.getAttribute("visibility");
				if (visibility.equals("invisible")) {
					pi.remove();
				}
			}
		}
		this.numCircles.set(numCircles);
		this.numEllipses.set(numEllipses);
		this.numRectangles.set(numRectangles);
		this.numSquares.set(numSquares);
		this.numTriangles.set(numTriangles);
		this.numUnknowns.set(numUnknowns);
		this.biggestShape.set(biggestShape);
		this.smallestShape.set(smallestShape);
	}
}
