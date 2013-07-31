package svgparser.shapes;

public class Circle extends Ellipse {
	
	@Override
	public ShapeType getType() {
		return ShapeType.CIRCLE;
	}
	
	public int getRadius() {
		return getHorizontalRadius();
	}
	
}
