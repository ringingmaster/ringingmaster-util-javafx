package com.concurrentperformance.fxutils.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class ShapeDrawer {

	public static void drawTriangle(GraphicsContext gc, double xCenter, double yCenter, double size, Direction direction) {
		switch (direction) {
			case RIGHT:
				gc.fillPolygon(new double[]{xCenter+0,xCenter-size,xCenter+size},new double[]{yCenter+size,yCenter-size,yCenter-size},3 );
				return;
			case DOWN:
				gc.fillPolygon(new double[]{xCenter+size,xCenter-size,xCenter-size},new double[]{yCenter+0,yCenter+size,yCenter-size},3 );
				return;
			default:
				throw new UnsupportedOperationException();
		}
	}
}
