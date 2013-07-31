package svgparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import svgparser.shapes.Point;
import svgparser.shapes.Boundary;
import svgparser.shapes.Shape.ShapeType;

import collections.Node;
import collections.NodeAction;
import collections.NodeHelper;
/*
*  Nasser Giacaman
*/

public class SVGMaker {
	
	private static int minWidth = 20;
	private static int minHeight = 20;
	private static int minDifference = 10;
	private static int maxWidth = 200;
	private static int maxHeight = 200;
	
	private static int canvasWidth = 1200;
	private static int canvasHeight = 550;
	
	private final static String strHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private static String randomColor() {
		int randR = (int) (Math.random()*256);
		int randG = (int) (Math.random()*256);
		int randB = (int) (Math.random()*256);
		return "style=\"stroke:rgb(0,0,0);fill:rgb("+String.valueOf(randR)+","+String.valueOf(randG)+","+String.valueOf(randB)+")\"";
	}
	
	/*
	 * Formula for an ellipse: 
	 * 	(x-h)^2 / a^2  + (y-k)^2 / b^2 = 1
	 */
	private static List<Point> createEllipsePoints(Boundary boundary) {
		List<Point> pts = new ArrayList<Point>();
		int steps = 360;
		
		double a = boundary.getWidth()/2;
		double b = boundary.getHeight()/2;
		double h = boundary.getX()+a;
		double k = boundary.getY()+b;
		
		double angle = 0;
		double beta = -angle * (Math.PI / 180.0); 
		double sinbeta = Math.sin(beta);
		double cosbeta = Math.cos(beta);
		
		for (int i = 0; i < 360; i += 360 / steps) {
			double alpha = i * (Math.PI / 180.0) ;
		    double sinalpha = Math.sin(alpha);
		    double cosalpha = Math.cos(alpha);
		    double X = h + (a * cosalpha * cosbeta - b * sinalpha * sinbeta);
		    double Y = k + (a * cosalpha * sinbeta + b * sinalpha * cosbeta);
		    pts.add(new Point((int)Math.round(X),(int)Math.round(Y)));
		}
		return pts;
	}
	
	private static Boundary createRandomBoundary(boolean square) {
		int randomCornerX = (int)(Math.random()*canvasWidth);
		int randomCornerY = (int)(Math.random()*canvasHeight);

		int randomWidth = (int)(Math.random()*(maxWidth-minWidth))+minWidth;
		int randomHeight = (int)(Math.random()*(maxHeight-minHeight))+minHeight;
		
		if (square) {
			randomHeight = randomWidth;
		} else {
			while (Math.abs(randomHeight-randomWidth) < minDifference)
				randomHeight = (int)(Math.random()*(maxHeight-minHeight))+minHeight;
		}
		
		if ((randomCornerX + randomWidth) > canvasWidth)
			randomCornerX = canvasWidth - randomWidth;
		
		if ((randomCornerY + randomHeight) > canvasHeight)
			randomCornerY = canvasHeight - randomHeight;
		
		return new Boundary(randomCornerX, randomCornerY, randomWidth, randomHeight);
	}
	
	private static String formatPointsAsDAttribute(List<Point> pts) {
		if (pts.size() < 3)
			throw new RuntimeException("Supposed to use this with at least 3 points");
		
		StringBuffer sb = new StringBuffer();
		sb.append("d=\"M ");
		
		for (int i = 0; i < pts.size(); i++) {
			Point p = pts.get(i);
			sb.append(p.getX()).append(",").append(p.getY()).append(" ");
			if (i == 0) {
				sb.append(" L ");
			}
		}
		sb.append(" Z\"");
		return sb.toString();
	}
	
	private static String formatAsEllipsePath(Boundary boundary, String id) {
		List<Point> pts = createEllipsePoints(boundary);
		String str = "<path id=\""+id+"\" "+randomColor()+" "+formatPointsAsDAttribute(pts)+" />";
		return str;
	}
	
	private static String formatAsRectanglePath(Boundary boundary, String id) {
		String str = "<path id=\""+id+"\" "+randomColor()+" d=\"M "+String.valueOf(boundary.getX())+","+String.valueOf(boundary.getY()) +" L " 
		+ String.valueOf(boundary.getX()+boundary.getWidth()) + "," + String.valueOf(boundary.getY()) + " "  
		+ String.valueOf(boundary.getX()+boundary.getWidth()) + "," + String.valueOf(boundary.getY()+boundary.getHeight()) + " "  
		+ String.valueOf(boundary.getX()) + "," + String.valueOf(boundary.getY()+boundary.getHeight()) + " Z\"/>";
		return str;
	}
	
	private final static int TOP = 0;
	private final static int BOTTOM = 1;
	private final static int LEFT = 2;
	private final static int RIGHT = 3;
	
	private static String formatAsTrianglePath(Boundary boundary, String id) {
		int numChosenPoints = 0;
		
		boolean[] chosen = new boolean[4];
		int[] x = new int[3];
		int[] y = new int[3];
		
		while (numChosenPoints < 3) {
			int side = (int)(Math.random()*4);
			if (!chosen[side]) {
				chosen[side]=true;
				
				switch(side) {
				case TOP:
					x[numChosenPoints] = boundary.midHorizontal();
					y[numChosenPoints] = boundary.getHeight();
					break;
				case BOTTOM:
					x[numChosenPoints] = boundary.midHorizontal();
					y[numChosenPoints] = boundary.getY()+boundary.getHeight();
					break;
				case LEFT:
					x[numChosenPoints] = boundary.getX();
					y[numChosenPoints] = boundary.midVertical();
					break;
				case RIGHT:
					x[numChosenPoints] = boundary.getX()+boundary.getWidth();
					y[numChosenPoints] = boundary.midVertical();
					break;
				}
				numChosenPoints++;
			}
		}
		
		String str = "<path id=\""+id+"\" "+randomColor()+" d=\"M "+String.valueOf(x[0])+","+String.valueOf(y[0]) +" L " 
		+ String.valueOf(x[1]) + "," + String.valueOf(y[1]) + " "  
		+ String.valueOf(x[2]) + "," + String.valueOf(y[2]) + " Z\"/>";
		return str;
	}
	
	private static String randomRectanglePath(int id) {
		return formatAsRectanglePath(createRandomBoundary(false), "rec-"+String.valueOf(id));
	}
	
	private static String randomSquarePath(int id) {
		return formatAsRectanglePath(createRandomBoundary(true), "sq-"+String.valueOf(id));
	}
	
	private static String randomTrianglePath(int id) {
		return formatAsTrianglePath(createRandomBoundary(false), "tri-"+String.valueOf(id));
	}
	
	private static String randomCirclePath(int id) {
		return formatAsEllipsePath(createRandomBoundary(true), "cir-"+String.valueOf(id));
	}
	
	private static String randomEllipsePath(int id) {
		return formatAsEllipsePath(createRandomBoundary(false), "elp-"+String.valueOf(id));
	}

	private static final int G_NODE = 0;
	private static final int PATH_NODE = 1;
	
	private static void selectRandomShape(Writer out, int id) {
		int randShape = (int)(Math.random()*5);
		switch (randShape) {
		case SQ:
			out.printLn(randomSquarePath(id++));
			break;
		case REC: 
				out.printLn(randomRectanglePath(id++));
			break;
		case CIR:
				out.printLn(randomCirclePath(id++));
			break;
		case ELP:
				out.printLn(randomEllipsePath(id++));
			break;
		case TRI:
				out.printLn(randomTrianglePath(id++));
			break;
		}
	}
	
	public static void makeRandomShapesInGroupTree(File output, int totalToMake, int degree, final ShapeType makeType) {
		final Writer out = new Writer();
		out.printLn(strHeader);
		out.printLn("<svg version=\"1.1\" width=\""+String.valueOf(canvasWidth)+"\" height=\""+String.valueOf(canvasHeight)+
				"\" viewBox=\"0 0 "+String.valueOf(canvasWidth)+" "+String.valueOf(canvasHeight)+"\">");
		out.indent();
		
		Node tree = NodeHelper.generateTreeAlternateBinaryValue(totalToMake*2+1, degree);
		
		NodeAction action = new NodeAction() {
			private int currentGroupID = 0;
			private int currentPathID = 0;
			@Override
			public void performBefore(Node node) {
				boolean gNode = node.getValue().equals(0);
				if (gNode) {
					out.printLn("<g visibility=\"visible\" id=\"group-"+String.valueOf(currentGroupID++)+"\" style=\"fill:rgb(0,0,0)\">");
					out.indent();
				} else {
					if (makeType == ShapeType.UNKNOWN)
						selectRandomShape(out, currentPathID++);			// 2700			(10,000 elements and degree of 10)
//					out.printLn(randomCirclePath(currentPathID++));			// 5100
					else if (makeType == ShapeType.ELLIPSE)
						out.printLn(randomEllipsePath(currentPathID++));	// 5500
					else if (makeType == ShapeType.RECTANGLE)
						out.printLn(randomRectanglePath(currentPathID++));	// 1000
//					out.printLn(randomSquarePath(currentPathID++));			// 950
					else if (makeType == ShapeType.TRIANGLE)
						out.printLn(randomTrianglePath(currentPathID++));	// 950
				}
			}
			@Override
			public void performAfter(Node node) {
				boolean gNode = node.getValue().equals(0);
				if (gNode) {
					out.unindent();
					out.printLn("</g>");
				} else {
					
				}
			}
		};
		NodeHelper.process(tree, action);
//		NodeHelper.print(tree);
		
		out.unindent();
		out.printLn("</svg>");
		try {
			FileWriter fstream = new FileWriter(output);
	        BufferedWriter bw = new BufferedWriter(fstream);
	        bw.write(out.toString());
	        bw.close();
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	public static void saveSVGFile(String filename, Document document) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = null;
			transformer = tFactory.newTransformer();
			
			File file = new File(filename);
			
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final static int SQ = 0;
	private final static int REC = 1;
	private final static int TRI = 2;
	private final static int CIR = 3;
	private final static int ELP = 4;
	public static void makeRandomShapesAsPaths(File output, int numSquares, int numRects, int numCircles, int numEllipses, int numTriangles, int degree) {
		Writer out = new Writer();
		out.printLn(strHeader);
		out.printLn("<svg version=\"1.1\" width=\""+String.valueOf(canvasWidth)+"\" height=\""+String.valueOf(canvasHeight)+
				"\" viewBox=\"0 0 "+String.valueOf(canvasWidth)+" "+String.valueOf(canvasHeight)+"\">");
		out.indent();
		
		int totalToMake = numSquares+numRects+numCircles+numEllipses+numTriangles;
		int totalMade = 0;
		int numSquaresMade=0, numRectsMade=0, numEllipsesMade=0, numCirclesMade=0, numTrianglesMade = 0;
		
		int gDegree = degree/2;
		int pathDegree = degree-gDegree;
		boolean pathTurn = true;
		
		while (totalMade < totalToMake) {
			int randShape = (int) (Math.random()*5);
			switch (randShape) {
			case SQ:
				if (numSquaresMade < numSquares) {
					out.printLn(randomSquarePath(totalMade++));
					numSquaresMade++;
				}
				break;
			case REC: 
				if (numRectsMade < numRects) {
					out.printLn(randomRectanglePath(totalMade++));
					numRectsMade++;
				}
				break;
			case CIR:
				if (numCirclesMade < numCircles) {
					out.printLn(randomCirclePath(totalMade++));
					numCirclesMade++;
				}
				break;
			case ELP:
				if (numEllipsesMade < numEllipses) {
					out.printLn(randomEllipsePath(totalMade++));
					numEllipsesMade++;
				}
				break;
			case TRI:
				if (numTrianglesMade < numTriangles) {
					out.printLn(randomTrianglePath(totalMade++));
					numTrianglesMade++;
				}
				break;
			}
		}
		
		out.unindent();
		out.printLn("</svg>");
		try {
			FileWriter fstream = new FileWriter(output);
	        BufferedWriter bw = new BufferedWriter(fstream);
	        bw.write(out.toString());
	        bw.close();
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	public static void makeRandomShapesAsPaths(File output, int numShapes, int degree) {
		int count = (int)Math.ceil(numShapes/5.0);
		makeRandomShapesAsPaths(output, count, count, count, count, count, degree);
	}
}
