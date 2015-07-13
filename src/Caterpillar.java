import uwcse.graphics.*;

import java.awt.Point;
import java.util.*;
//import java.util.concurrent.RecursiveTask;
import java.awt.Color;

import javax.swing.JOptionPane;

import org.w3c.dom.css.Rect;

/**
 * A Caterpillar is the representation and the display of a caterpillar
 */

public class Caterpillar implements CaterpillarGameConstants {
	// The body of a caterpillar is made of Points stored
	// in an ArrayList
	private ArrayList<Point> body = new ArrayList<Point>();

	// Store the graphical elements of the caterpillar body
	// Useful to erase the body of the caterpillar on the screen
	private ArrayList<Rectangle> bodyUnits = new ArrayList<Rectangle>();

	// The window the caterpillar belongs to
	private GWindow window;

	// Direction of motion of the caterpillar (NORTH initially)
	private int dir = NORTH;

	// Length of a unit of the body of the caterpillar
	// MUST be equal to the distance covered by the caterpillar
	// at every step of the animation.
	private final int bodyUnitLength = STEP;
	// Width of a unit of the body of the caterpillar
	private final int bodyUnitWidth = CATERPILLAR_WIDTH;

	private ArrayList<Cabbage> cabbages;

	/**
	 * Constructs a caterpillar
	 * 
	 * @param window
	 *            the graphics where to draw the caterpillar.
	 */
	public Caterpillar(GWindow window, ArrayList<Cabbage> cabbages) {
		this.cabbages = cabbages;
		// Initialize the graphics window for this Caterpillar
		this.window = window;
		// Create the caterpillar (10 points initially)
		// First point
		Point p = new Point();
		p.x = 5;
		p.y = WINDOW_HEIGHT / 2;
		body.add(p);

		// Other points
		for (int i = 0; i < 9; i++) {
			Point q = new Point(p);
			q.translate(STEP, 0);
			body.add(q);
			p = q;
		}

		// Other initializations (if you have more instance fields)

		// Display the caterpillar (call a private method)

		// Show the caterpillar
		draw();
	}

	/**
	 * Draw the caterpillar in the graphics window
	 */
	private void draw() {
		// Connect with Rectangles the points of the body
		Point p = body.get(0);

		for (int i = 1; i < body.size(); i++) {
			Point q = body.get(i);
			// add a body unit between p and q
			addBodyUnit(p, q, bodyUnits.size(), Color.RED);
			p = q;
		}

		window.doRepaint();
	}

	/**
	 * Moves the caterpillar in the current direction (complete)
	 */
	public void move() {
		move(dir);
	}

	/**
	 * Move the caterpillar in the direction newDir. <br>
	 * If the new direction is illegal, select randomly a legal direction of
	 * motion and move in that direction.<br>
	 * 
	 * @param newDir
	 *            the new direction.
	 */

	public void move(int newDir) {

		// while caterpillar is trying to do anything invalid
		while (!isPointInTheWindow(newDir) || isApproachingFence(newDir)
				|| !validDirection(newDir) || isCrawlingOverItself(newDir)) {
			// if point is not in window
			if (!isPointInTheWindow(newDir)) {
				int[] directions = { NORTH, SOUTH, WEST, EAST };
				newDir = directions[(int) (Math.random() * directions.length)];
			}
			// if about to run into fence
			if (isApproachingFence(newDir)) {
				if (newDir == NORTH || newDir == SOUTH) {
					int[] direction = { WEST, EAST };
					newDir = direction[(int) (Math.random() * direction.length)];

				} else if (newDir == EAST || newDir == WEST) {
					int[] direction = { NORTH, SOUTH };
					newDir = direction[(int) (Math.random() * direction.length)];
				}
			}
			// if attempting to go backwards
			if (!validDirection(newDir)) {
				// keep caterpillar moving in same direction
				newDir = dir;
			}
			// if eats self
			if (isCrawlingOverItself(newDir)) {
				// You lose!;
			}
		}

		// If caterpillar eats all good cabbages and leaves window
		if (isOutsideGarden() && this.cabbages.size() < 11) {
			window.stopTimerEvents();
			int n = JOptionPane.showConfirmDialog(null,
					"You won the game! Would you like to play again?",
					"YOU WIN!", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				window.setExitOnClose();
				new CaterpillarGame();
			} else {
				// exit game
				System.exit(0);
			}
		}

		// If caterpillar eats cabbages
		Point head = getHead();
		for (int i = 0; i < cabbages.size(); i++) {
			Cabbage c = cabbages.get(i);
			boolean eaten = c.isPointInCabbage(head);
			if (eaten) {
				c.isEatenBy(this, body, bodyUnits);
				cabbages.remove(c);
			}
		}

		// new position of the head
		head = new Point(body.get(body.size() - 1));
		switch (newDir) {
		case NORTH:
			head.y -= STEP;
			break;
		case EAST:
			head.x += STEP;
			break;
		case SOUTH:
			head.y += STEP;
			break;
		case WEST:
			head.x -= STEP;
			break;
		}

		// Is the new position in the window?
		if (isPointInTheWindow(newDir)) {
			// update the position of the caterpillar
			body.add(head);
			body.remove(0);
		}

		// Update the current direction of motion
		dir = newDir;

		// Show the new location of the caterpillar
		moveCaterpillarOnScreen();

	}

	/**
	 * Move the caterpillar on the screen
	 */
	private void moveCaterpillarOnScreen() {
		// Erase the body unit at the tail
		Rectangle r = bodyUnits.remove(0);
		window.remove(r);
		// Add a new body unit at the head
		Point p = body.get(body.size() - 1);
		Point q = body.get(body.size() - 2);
		addBodyUnit(p, q, bodyUnits.size(), Color.RED);
		// show it
		window.doRepaint();
	}

	/**
	 * Add a body unit to the caterpillar. The body unit connects Point p and
	 * Point q.<br>
	 * Insert this body unit at position index in bodyUnits.<br>
	 * e.g. 0 to insert at the tail and bodyUnits.size() to insert at the head.
	 */
	public void addBodyUnit(Point p, Point q, int index, Color c) {
		// Connect p and q with a rectangle.
		// To allow for a smooth look of the caterpillar, p and q
		// are not on the edges of the Rectangle
		// Upper left corner of the rectangle
		int x = Math.min(q.x, p.x) - CATERPILLAR_WIDTH / 2;
		int y = Math.min(q.y, p.y) - CATERPILLAR_WIDTH / 2;

		// Width and height of the rectangle (vertical or horizontal rectangle?)
		int width = ((q.y == p.y) ? (STEP + CATERPILLAR_WIDTH)
				: CATERPILLAR_WIDTH);
		int height = ((q.x == p.x) ? (STEP + CATERPILLAR_WIDTH)
				: CATERPILLAR_WIDTH);

		// Create the rectangle and place it in the window
		Rectangle r = new Rectangle(x, y, width, height, c, true);
		window.add(r);

		// keep track of that rectangle (we will erase it at some point)
		bodyUnits.add(index, r);
	}

	/**
	 * Is the caterpillar crawling over itself?
	 * 
	 * @return true if the caterpillar is crawling over itself and false
	 *         otherwise.
	 */
	public boolean isCrawlingOverItself(int newDir) {
		// Is the head point equal to any other point of the caterpillar?
		Point head = getHead();
		int x = (int) head.getX();
		int y = (int) head.getY();

		if (newDir == NORTH) {
			y -= STEP;
		} else if (newDir == EAST) {
			x += STEP;
		} else if (newDir == WEST) {
			x -= STEP;
		} else { // SOUTH
			y += STEP;
		}
		// Check each rectangle in caterpillar body; if head points are inside
		// any rectangle, end game.
		for (int i = 0; i < bodyUnits.size(); i++) {
			int w = bodyUnits.get(i).getWidth();
			int h = bodyUnits.get(i).getHeight();
			int bx = bodyUnits.get(i).getX();
			int by = bodyUnits.get(i).getY();

			if ((x > bx && x < (bx + w)) && (y > by && y < (by + h))) {
				window.stopTimerEvents();
				int n = JOptionPane.showConfirmDialog(null,
						"You ate yourself! Would you like to play again?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					window.setExitOnClose();
					new CaterpillarGame();
				} else {
					// exit game
					System.exit(0);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Are all of the points of the caterpillar outside the garden
	 * 
	 * @return true if the caterpillar is outside the garden and false
	 *         otherwise.
	 */
	public boolean isApproachingFence(int newDir) {
		// Get head; get direction. Find future head.
		Point head = getHead();
		int x = (int) head.getX();
		int y = (int) head.getY();

		if (newDir == NORTH) {
			y -= STEP;
		} else if (newDir == EAST) {
			x += STEP;
		} else if (newDir == WEST) {
			x -= STEP;
		} else { // SOUTH
			y += STEP;
		}

		// If future head overlaps with body
		if ((x > 149 && x < 161)
				&& ((y > 0 && y < 201) || (y > 299 && y < 500))) {
			return true;
		}
		if (x > 491) {
			return true;
		}
		if (y < 11 || y > 491) {
			return true;
		}
		return false;
	}

	public boolean isOutsideGarden() {
		// Check head
		Point head = getHead();
		int x = (int) head.getX();

		// Check tail
		Point tail = new Point(body.get(0));
		int x2 = (int) tail.getX();

		// If head and tail are outside the garden
		if (x < 140 && x2 < 140) {
			return true;
		}
		return false;
	}
	
	/**
	 * Is newDir valid?
	 *@param int newDir
	 *@returns boolean validity
	 */
	public boolean validDirection(int newDir) {
		if ((dir == NORTH && newDir == SOUTH)
				|| (dir == SOUTH && newDir == NORTH)
				|| (dir == EAST && newDir == WEST)
				|| (dir == WEST && newDir == EAST)) {
			return false;
		}
		return true;
	}

	/**
	 * Is Point p in the window?
	 * @ param newDir
	 * @returns boolean 
	 */
	private boolean isPointInTheWindow(int newDir) {
		Point head = getHead();
		int x = (int) head.getX();
		int y = (int) head.getY();

		if (newDir == NORTH) {
			y -= STEP;
		} else if (newDir == EAST) {
			x += STEP;
		} else if (newDir == WEST) {
			x -= STEP;
		} else { // SOUTH
			y += STEP;
		}

		// Checks to see if point is in the window
		if ((x < 1 || x > 499) || (y < 1 || y > 499)) {
			return false;
		}
		return true;
	}

	/**
	 * Return the location of the head of the caterpillar (complete)
	 * 
	 * @return the location of the head of the caterpillar.
	 */
	public Point getHead() {
		return new Point((Point) body.get(body.size() - 1));
	}

}
