package svgparser;

public class Writer {
	private final String newLine = System.getProperty("line.separator");
	
	private StringBuffer sb = new StringBuffer();
	private int currentIndent = 0;
	
	private void printIndents() {
		for (int i = 0; i < currentIndent; i++)
			sb.append("  ");
	}
	
	public void indent() {
		currentIndent++;
		if (currentIndent < 0)
			currentIndent = 0;
	}
	public void unindent() {
		currentIndent--;
	}
	
	public void print(String s) {
		printIndents();
		sb.append(s);
	}
	
	public void newLine() {
		sb.append(newLine);
	}
	
	public void printLn(String s) {
		print(s);
		sb.append(newLine);
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
}