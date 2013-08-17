package svgparser.shapes;

public class Ellipse extends Shape {
	
	@Override
	public ShapeType getType() {
		return ShapeType.ELLIPSE;
	}
	
	public int getHorizontalRadius() {
		return getWidth()/2;
	}
	
	public int getVerticalRadius() {
		return getHeight()/2;
	}

	@Override
	public int getArea() {
		return (int)(Math.PI * getHorizontalRadius() * getVerticalRadius());
	}
}
