package svgparser.shapes;

import java.util.StringTokenizer;

public class Style {

	private String definitionStr;
	private Color fillColor = null;
	private Color strokeColor = null;

	public Style(String definitionStr) {
		this.definitionStr = definitionStr;
		parseStyle();
	}
	
	public Color getFillColor() {
		return fillColor;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}
	
	private Color getColorComponent(String str) {
		int rgbPos = str.indexOf("rgb");
		if (rgbPos >= 0) {
			str = str.substring(str.indexOf('(')+1, str.indexOf(')'));
			StringTokenizer st = new StringTokenizer(str,",");
			int r = Integer.parseInt(st.nextToken());
			int g = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			return new Color(r, g, b);
		}
		return null;
	}
	
	private void parseStyle() {
		StringTokenizer st = new StringTokenizer(definitionStr, ";");
		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (str.startsWith("stroke:")) {
				strokeColor =  getColorComponent(str);
			} else if (str.startsWith("fill")) {
				fillColor = getColorComponent(str);
			}
		}
	}
}
