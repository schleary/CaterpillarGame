import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import uwcse.graphics.*;

public class GoodCabbage extends Cabbage {
	Oval circle;

	private ArrayList<Point> body = new ArrayList<Point>();
	private ArrayList<Rectangle> bodyUnits;

	/**
	 * Creates a cabbage in the graphics window
	 * 
	 * @param window
	 *            the GWindow this Cabbage belongs to
	 * @param center
	 *            the center Point of this Cabbage
	 */
	public GoodCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
	}

	/**
	 * Displays this Cabbage in the graphics window
	 */
	protected void draw() {

		int x = (int) center.getX();
		int y = (int) center.getY();
		// Make sure cabbage does not already exist here

		ArrayList<Point> centerPoints = new ArrayList<Point>();

		this.circle = new Oval(x - CABBAGE_RADIUS, y - CABBAGE_RADIUS,
				CABBAGE_RADIUS * 2, CABBAGE_RADIUS * 2, Color.WHITE, true);
		window.add(circle);
		centerPoints.add(center);
	}

	/**
	 * Checks for overlapping cabbages
	 * 
	 * @param Point
	 *            p
	 * @return boolean is cabbage overlapping
	 * 
	 */
	public boolean isCabbageInCabbage(Point p) {

		return (p.distance(center) <= CABBAGE_RADIUS * 2);
	}

	/**
	 * This cabbage is eaten by a caterpillar
	 * 
	 * @param cp
	 *            the caterpillar that is eating this cabbage
	 */
	public void isEatenBy(Caterpillar cp, ArrayList<Point> body,
			ArrayList<Rectangle> bodyUnits) {
		this.body = body;
		this.bodyUnits = bodyUnits;
		window.remove(this.circle);
		Point p = body.get(body.size() - 1);
		Point q = body.get(body.size() - 2);
		cp.addBodyUnit(p, q, bodyUnits.size(), Color.RED);

	}

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