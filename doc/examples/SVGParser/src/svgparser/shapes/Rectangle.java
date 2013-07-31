package svgparser.shapes;

public class Rectangle extends Shape {
	
	private int lengthA = -1;
	private int lengthB = -1;
	private int lengthC = -1;
	private int lengthD = -1;
	private double angleA  = -1;
	private double angleB  = -1;
	private double angleC  = -1;
	private double angleD  = -1;
	private ShapeType type;
	
	@Override
	public ShapeType getType() {
		return ShapeType.RECTANGLE;
	}

	@Override
	public int getArea() {
		return getWidth() * getHeight();
	}
	
	@Override
	public void setPath(Path path) {
		super.setPath(path);
		
		//-- actually, all the following is useless.. just create more computation
		
		Point p1 = path.getPoints().get(0);
		Point p2 = path.getPoints().get(1);
		Point p3 = path.getPoints().get(2);
		Point p4 = path.getPoints().get(3);
		
		int x1 = p1.getX();
		int y1 = p1.getY();
		int x2 = p2.getX();
		int y2 = p2.getY();
		int x3 = p3.getX();
		int y3 = p3.getY();
		int x4 = p4.getX();
		int y4 = p4.getY();
		
		double alengthA = Math.round(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
		double alengthB = Math.round(Math.sqrt((x2-x3)*(x2-x3) + (y2-y3)*(y2-y3)));
		double alengthC = Math.round(Math.sqrt((x3-x4)*(x3-x4) + (y3-y4)*(y3-y4)));
		double alengthD = Math.round(Math.sqrt((x4-x1)*(x4-x1) + (y4-y1)*(y4-y1)));

		lengthA = (int) alengthA;
		lengthB = (int) alengthB;
		lengthC = (int) alengthC;
		lengthD = (int) alengthD;
		
		if ( allSidesTheSame() )
			type = ShapeType.SQUARE;
		else if ( atLeastTwoSidesTheSame() )
			type = ShapeType.RECTANGLE;
		else 
			type = ShapeType.UNKNOWN;
		
		// these angles are not useful, but they create more work
		angleA = (int)Math.round(Math.acos((alengthC*alengthC + alengthB*alengthB - alengthA*alengthA)/(2*alengthC*alengthB)));
		angleB = (int)Math.round(Math.acos((alengthA*alengthA + alengthD*alengthD - alengthB*alengthB)/(2*alengthA*alengthD)));
		angleC = (int)Math.round(Math.acos((alengthA*alengthA + alengthB*alengthB - alengthC*alengthC)/(2*alengthA*alengthB)));
		angleD = (int)Math.round(Math.acos((alengthD*alengthD + alengthC*alengthC - alengthC*alengthC)/(2*alengthA*alengthB)));
		
	}
	private boolean allSidesTheSame() {
		return lengthA == lengthB && lengthA == lengthC && lengthA == lengthD;
	}	
	
	private boolean atLeastTwoSidesTheSame() {
		if (lengthA == lengthB)
			return true;
		if (lengthA == lengthC)
			return true;
		if (lengthA == lengthD)
			return true;
		if (lengthB == lengthC)
			return true;
		if (lengthB == lengthD)
			return true;
		if (lengthC == lengthD)
			return true;
		return false;
	}
}
