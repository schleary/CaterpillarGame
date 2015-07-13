import uwcse.graphics.*;

import java.awt.Point;
import java.util.ArrayList;

/**
 * An abstract class that contains all of the basic features of a cabbage. It
 * should be derived by any class that represents a cabbage. (complete, but you
 * can add other methods if you want)
 */

public abstract class Cabbage implements CaterpillarGameConstants {
	// The graphics window this Cabbage belongs to
	protected GWindow window;

	// The location of the center of this Cabbage in the graphics window
	protected Point center;

	/**
	 * Creates a cabbage in the graphics window
	 * 
	 * @param window
	 *            the GWindow this Cabbage belongs to
	 * @param center
	 *            the center Point of this Cabbage
	 */
	public Cabbage(GWindow window, Point center) {
		this.window = window;
		this.center = new Point(center);
	}

	/**
	 * Displays this Cabbage in the graphics window
	 */
	protected abstract void draw();

	/**
	 * This cabbage is eaten by a caterpillar
	 * 
	 * @param cp
	 *            the caterpillar that is eating this cabbage
	 */
	public abstract void isEatenBy(Caterpillar cp, ArrayList<Point> body,
			ArrayList<Rectangle> bodyUnits);

	/**
	 * Is this Point in this Cabbage?
	 * 
	 * @param p
	 *            the Point to check
	 * @return true if p in within the cabbage and false otherwise.
	 */
	public boolean isPointInCabbage(Point p) {

		return (p.distance(center) <= CABBAGE_RADIUS);
	}

	public boolean isCabbageInCabbage(Point p) {

		return (p.distance(center) <= CABBAGE_RADIUS * 2);
	}

	public double distance(Cabbage c) {
		return Math.sqrt((c.center.x - center.x) * (c.center.x - center.x)
				+ (c.center.y - center.y) * (c.center.y - center.y));
	}

	/**
	 * Returns the location of this Cabbage
	 * 
	 * @return the location of this Cabbage.
	 */
	public Point getLocation() {
		return new Point(center);
	}
}