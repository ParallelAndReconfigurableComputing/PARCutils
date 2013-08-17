package svgparser.shapes;

import java.util.List;

public class Boundary {
	private int x, y, width, height;
	public Boundary(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	@Override
	public String toString() {
		return "[b "+x+","+y+","+width+","+height+"]";
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int midHorizontal() {
		return x+width/2;
	}
	public int midVertical() {
		return y+height/2;
	}
	public boolean pointWithHeight(Point point) {
		return (point.getY() <= (getY()+getHeight())) && (point.getY() >= getY());
	}
	public boolean pointWithinWidth(Point point) {
		return (point.getX() >= getX()) && (point.getX() <= (getX()+getWidth()));
	}
	public boolean pointOnLeftBoundary(Point point) {
		return point.getX() == getX() && pointWithHeight(point);
	}
	public boolean pointOnTopBoundary(Point point) {
		return point.getY() == getY() && pointWithinWidth(point);
	}
	public boolean pointOnRightBoundary(Point point) {
		return point.getX() == (getX()+getWidth()) && pointWithHeight(point);
	}
	public boolean pointOnBottomBoundary(Point point) {
		return point.getY() == (getY()+getHeight()) && pointWithinWidth(point);
	}
	public boolean pointOnTopLeftCorner(Point point) {
		return pointOnLeftBoundary(point) && pointOnTopBoundary(point);
	}
	public boolean pointOnTopRightCorner(Point point) {
		return pointOnTopBoundary(point) && pointOnRightBoundary(point);
	}
	public boolean pointOnBottomLeftCorner(Point point) {
		return pointOnBottomBoundary(point) && pointOnLeftBoundary(point);
	}
	public boolean pointOnBottomRightCorner(Point point) {
		return pointOnBottomBoundary(point) && pointOnRightBoundary(point);
	}
	public boolean pointsAreCornerPoints(List<Point> points) {
		if (points.size() != 4)
			return false;
		boolean TL=false, TR=false, BL=false, BR=false;
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			if (pointOnBottomLeftCorner(point))
				BL = true;
			if (pointOnBottomRightCorner(point))
				BR = true;
			if (pointOnTopLeftCorner(point))
				TL = true;
			if (pointOnTopRightCorner(point))
				TR = true;
		}
		return TL && TR && BL && BR;
	}
	
	public boolean pointLiesOnBoundary(Point point) {
		if ( pointOnLeftBoundary(point) ) {
			return true;
		}
		
		if (pointOnBottomBoundary(point)) {
			return true;
		}
		
		if (pointOnRightBoundary(point)) {
			return true;
		}
		
		if (pointOnTopBoundary(point)) {
			return true;
		}
		return false;
	}
}