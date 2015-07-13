import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.*;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import uwcse.graphics.*;

public class BadCabbage extends Cabbage {
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
	public BadCabbage(GWindow window, Point center) {
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
		getLocation();

		this.circle = new Oval(x - CABBAGE_RADIUS, y - CABBAGE_RADIUS,
				CABBAGE_RADIUS * 2, CABBAGE_RADIUS * 2, Color.RED, true);
		window.add(circle);
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
		// Remove cabbages
		window.remove(this.circle);
		window.stopTimerEvents();
		int n = JOptionPane.showConfirmDialog(null,
				"You ate a bad Cabbage! Would you like to play again?",
				"GAME OVER", JOptionPane.YES_NO_OPTION);
		if (n == 0) {
			window.setExitOnClose();
			new CaterpillarGame();
		} else {
			// exit game
			System.exit(0);
		}
	}

	/**
	 * Is this Point in this Cabbage?
	 * 
	 * @param p
	 *            the Point to check
	 * @return true if p in within the cabbage and false otherwise.
	 * 
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