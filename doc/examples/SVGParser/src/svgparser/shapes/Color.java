package svgparser.shapes;

public class Color {

	public static final Color RED = new Color(255,0,0);
	public static final Color YELLOW = new Color(255,255,0);
	public static final Color BLACK = new Color(0,0,0);
	
	private int red;
	private int green;
	private int blue;

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	@Override
	public String toString() {
		return "rgb("+red+","+green+","+blue+")";
	}
}
