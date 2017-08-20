package org.ringingmaster.fxutils.color;

import javafx.scene.paint.Color;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class ColorUtil {

	public static String toWeb(Color color) {
		if (color == null) {
			return "";
		}
		int r = (int)Math.round(color.getRed() * 255.0);
		int g = (int)Math.round(color.getGreen() * 255.0);
		int b = (int)Math.round(color.getBlue() * 255.0);
		return String.format("#%02x%02x%02x", r, g, b);
	}
}
