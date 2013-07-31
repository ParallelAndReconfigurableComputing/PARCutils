package svgparser.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Path {
	
	private Boundary boundary = null;
	private List<Point> points = null;
	private Shape shape = null;
	
	private String definitionStr;
	
	public Path(String definitionStr) {
		this.definitionStr = definitionStr;
	}
	
	public Boundary getBoundary() {
		if (boundary == null) {
			getPoints();
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxX = 0;
			int maxY = 0;
			
			for (int i = 0; i < points.size(); i++) {
				Point p = points.get(i);
				minX = Math.min(minX, p.getX());
				minY = Math.min(minY, p.getY());
				maxX = Math.max(maxX, p.getX());
				maxY = Math.max(maxY, p.getY());
			}
			boundary = new Boundary(minX, minY, maxX-minX, maxY-minY);
		}
		return boundary;
	}
	
	public List<Point> getPoints() {
		if (points == null) {
			points = new ArrayList<Point>();
			// -- parse points
			String cleanD = definitionStr.replaceAll("[MLZ]", "");
			StringTokenizer st = new StringTokenizer(cleanD);
			while (st.hasMoreTokens()) {
				String pair = st.nextToken();
				int x = Integer.parseInt(pair.substring(0,pair.indexOf(',')));
				int y = Integer.parseInt(pair.substring(pair.indexOf(',')+1));
				points.add(new Point(x,y));
			}
		}
		return points;
	}
	
	public Shape getShape() {
		if (shape == null)
			shape = ShapeGuesser.guess(this);
		return shape;
	}
	
	public String getDefinitionString() {
		return definitionStr;
	}
	
	//-- package use only
	void setShape(Shape shape) {
		this.shape = shape;
		shape.setPath(this);
	}
}
