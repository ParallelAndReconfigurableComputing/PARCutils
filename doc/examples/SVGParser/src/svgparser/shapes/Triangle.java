package svgparser.shapes;

public class Triangle extends Shape {
	
	public final static int EQUILATERAL = 0; 
	public final static int ISOSCELES = 1; 
	public final static int SCALENE = 2; 
	
	private int triangleType = -1;
	
	private int lengthA = -1;
	private int lengthB = -1;
	private int lengthC = -1;
	private double angleA  = -1;
	private double angleB  = -1;
	private double angleC  = -1;
	
	public int getLengthA() {
		return lengthA;
	}
	
	public int getLengthB() {
		return lengthB;
	}
	
	public int getLengthC() {
		return lengthC;
	}
	
	public double getAngleA() {
		return angleA;
	}
	
	public double getAngleB() {
		return angleB;
	}
	
	public double getAngleC() {
		return angleC;
	}
	
	@Override
	public ShapeType getType() {
		return ShapeType.TRIANGLE;
	}
	
	public int getTriangleType() {
		return triangleType;
	}
	
	@Override
	public void setPath(Path path) {
		super.setPath(path);
		
		Point p1 = path.getPoints().get(0);
		Point p2 = path.getPoints().get(1);
		Point p3 = path.getPoints().get(2);
		
		int x1 = p1.getX();
		int y1 = p1.getY();
		int x2 = p2.getX();
		int y2 = p2.getY();
		int x3 = p3.getX();
		int y3 = p3.getY();
		
		double alengthA = Math.round(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
		double alengthB = Math.round(Math.sqrt((x1-x3)*(x1-x3) + (y1-y3)*(y1-y3)));
		double alengthC = Math.round(Math.sqrt((x3-x2)*(x3-x2) + (y3-y2)*(y3-y2)));

		lengthA = (int) alengthA;
		lengthB = (int) alengthB;
		lengthC = (int) alengthC;
		
		if ( allSidesTheSame() )
			triangleType = Triangle.EQUILATERAL;
		else if ( atLeastTwoSidesTheSame() )
			triangleType = Triangle.ISOSCELES;
		else 
			triangleType = Triangle.SCALENE;

		angleA = (int)Math.round(Math.acos((alengthC*alengthC + alengthB*alengthB - alengthA*alengthA)/(2*alengthC*alengthB)));
		angleB = (int)Math.round(Math.acos((alengthA*alengthA + alengthC*alengthC - alengthB*alengthB)/(2*alengthA*alengthC)));
		angleC = (int)Math.round(Math.acos((alengthA*alengthA + alengthB*alengthB - alengthC*alengthC)/(2*alengthA*alengthB))); 
	}
	
	private boolean allSidesTheSame() {
		return lengthA == lengthB && lengthA == lengthC;
	}
	
	private boolean atLeastTwoSidesTheSame() {
		if (lengthA == lengthB)
			return true;
		if (lengthA == lengthC)
			return true;
		if (lengthB == lengthC)
			return true;
		return false;
	}

	@Override
	public int getArea() {
		return getWidth()*getHeight()/2;
	}
}
