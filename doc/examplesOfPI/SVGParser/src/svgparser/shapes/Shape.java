package svgparser.shapes;

public abstract class Shape {
	
	public static enum ShapeType { UNKNOWN, CIRCLE, ELLIPSE, SQUARE, RECTANGLE, TRIANGLE }
	
	private Path path = null;
	
	private int width = -1;
	private int height = -1;
	private int centerX = -1;
	private int centerY = -1;
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public Boundary getBoundary() {
		return path.getBoundary();
	}
	
	public Path getPath() {
		return path;
	}
	
	//-- package use only
	void setPath(Path path) {
		this.path = path;
		Boundary b = path.getBoundary();
		this.width = b.getWidth();
		this.height = b.getHeight();
		this.centerX = b.getX() + width/2;
		this.centerY = b.getY() + height/2;
	}
	
	abstract public ShapeType getType(); 
	abstract public int getArea();
}
