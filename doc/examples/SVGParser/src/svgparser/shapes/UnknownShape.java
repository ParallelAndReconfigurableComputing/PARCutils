package svgparser.shapes;

public class UnknownShape extends Shape {

	@Override
	public ShapeType getType() {
		return ShapeType.UNKNOWN;
	}

	@Override
	public int getArea() {
		return -1;
	}
}
