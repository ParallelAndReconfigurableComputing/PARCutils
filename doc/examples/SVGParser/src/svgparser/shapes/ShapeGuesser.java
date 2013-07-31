package svgparser.shapes;

import java.util.List;

public class ShapeGuesser {

	//-- package use only
	static Shape guess(Path path) {
		
		if (tryCircle(path))
			return path.getShape();
		
		if (tryEllipse(path))
			return path.getShape();
		
		if (trySquare(path))
			return path.getShape();
		
		if (tryRectangle(path))
			return path.getShape();
		
		if (tryTriangle(path))
			return path.getShape();
		
		path.setShape(new UnknownShape());
		return path.getShape();
	}
	
	private static boolean tryCircle(Path path) {
		if (tryEllipse(path)) {
			Boundary boundary = path.getBoundary();
			if (boundary.getWidth() == boundary.getHeight()) {
				path.setShape(new Circle());
				return true;
			}
		}
		return false;
	}
	
	private static boolean tryTriangle(Path path) {
		List<Point> points = path.getPoints();
		if (points.size() == 3) {
			path.setShape(new Triangle());
			return true;
		}
		return false;
	}
	
	
	private static boolean tryRectangle(Path path) {
		List<Point> points = path.getPoints();
		Boundary boundary = path.getBoundary();
		
		boolean isRect = boundary.pointsAreCornerPoints(points);
		
		if (isRect) {
			path.setShape(new Rectangle());
			return true;
		}
		return false;
	}
	
	private static boolean trySquare(Path path) {
		if (tryRectangle(path)) {
			Boundary boundary = path.getBoundary();
			if (boundary.getWidth() == boundary.getHeight()) {
				path.setShape(new Square());
				return true;
			}
		}
		return false;
	}
	
	/*
	 * formula for ellipse
	 * (x-h)^2 / a^2  + (y-k)^2 / b^2 = 1
	 */
	private static boolean tryEllipse(Path path) {
		final double epsilon = 0.15;
		List<Point> points = path.getPoints();
		
		Boundary boundary = path.getBoundary();

		double a = boundary.getWidth()/2;
		double b = boundary.getHeight()/2;
		double h = boundary.getX()+a;
		double k = boundary.getY()+b;
		
		boolean allPointsOnEllipseBoundary = true;
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			double x = p.getX();
			double y = p.getY();
			
			double v1 = (x-h)*(x-h)/(a*a);
			double v2 = (y-k)*(y-k)/(b*b);
			double v = v1+v2;
			
			double diff = Math.abs(v-1);
			allPointsOnEllipseBoundary &= (diff < epsilon);
		}
		
		if ( allPointsOnEllipseBoundary ) {
			path.setShape(new Ellipse());
			return true;
		}
		return false;
	}
}
