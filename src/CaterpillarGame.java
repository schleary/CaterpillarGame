import uwcse.graphics.*;

import java.util.*;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JOptionPane;

/**
 * A CaterpillarGame displays a garden that contains good and bad cabbages and a
 * constantly moving caterpillar. The player directs the moves of the
 * caterpillar. Every time the caterpillar eats a cabbage, the caterpillar
 * grows. The player wins when all of the good cabbages are eaten and the
 * caterpillar has left the garden. The player loses if the caterpillar eats a
 * bad cabbage or crawls over itself.
 */

public class CaterpillarGame extends GWindowEventAdapter implements
		CaterpillarGameConstants
// The class inherits from GWindowEventAdapter so that it can handle key events
// (in the method keyPressed), and timer events.
// All of the code to make this class able to handle key events and perform
// some animation is already written.
{
	// Game window
	private GWindow window;

	// The caterpillar
	private Caterpillar cp;

	// Direction of motion given by the player
	private int dirFromKeyboard;

	// Do we have a keyboard event
	private boolean isKeyboardEventNew = false;

	// The list of all the cabbages
	private ArrayList<Cabbage> cabbages;

	private String messageGameOver;

	/**
	 * Constructs a CaterpillarGame
	 */
	public CaterpillarGame() {
		// Create the graphics window
		window = new GWindow("Caterpillar game", WINDOW_WIDTH, WINDOW_HEIGHT);

		// Any key or timer event while the window is active is sent to this
		// CaterpillarGame
		window.addEventHandler(this);

		// Set up the game (fence, cabbages, caterpillar)
		initializeGame();

		// Display the game rules
		// ...
		// Rules
		JOptionPane
				.showMessageDialog(
						null,
						"          WELCOME TO CATERPILLAR GAME! \n\n "
								+ "To win, you must eat all of the white and psychedelic   \n"
								+ "            cabbage heads and then exit the garden \n \n"
								+ "             Do not eat the poisonous red cabbages \n"
								+ "                       Do not eat yourself \n \n"
								+ "                      Use 'J' to move left \n"
								+ "                      Use 'K' to move right \n"
								+ "                      Use 'I' to move up \n"
								+ "                      Use 'M' to move down \n \n"
								+ "                     Are you ready to play? \n");

		// start timer events (to do the animation)

		this.window.startTimerEvents(ANIMATION_PERIOD);
	}

	/**
	 * Initializes the game (draw the garden, garden fence, cabbages,
	 * caterpillar)
	 */
	private void initializeGame() {
		// Clear the window
		window.erase();

		// No keyboard event yet
		isKeyboardEventNew = false;

		// Background (the garden)
		window.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT,
				Color.green, true));

		// Create the fence around the garden
		Rectangle fence1 = new Rectangle(150, 0, 10, 200, Color.BLACK, true);
		Rectangle fence2 = new Rectangle(150, 300, 10, 200, Color.BLACK, true);
		Rectangle fence3 = new Rectangle(150, 0, 340, 10, Color.BLACK, true);
		Rectangle fence4 = new Rectangle(490, 0, 10, 500, Color.BLACK, true);
		Rectangle fence5 = new Rectangle(150, 490, 340, 10, Color.BLACK, true);
		window.add(fence1);
		window.add(fence2);
		window.add(fence3);
		window.add(fence4);
		window.add(fence5);

		// Cabbages
		// ...
		cabbages = new ArrayList<Cabbage>(N_GOOD_CABBAGES + N_BAD_CABBAGES);

		// Initialize the elements of the ArrayList = cabbages
		// (they should not overlap and be in the garden) ....
		Point center = new Point();
		boolean locationIsCabbageFree;
		for (int i = 0; i < N_BAD_CABBAGES + N_GOOD_CABBAGES; i++) {
			locationIsCabbageFree = false;
			while (!locationIsCabbageFree) {
				int x = (int) (Math.random() * 310) + 170;
				int y = (int) (Math.random() * 460) + 20;
				center.setLocation(x, y);

				locationIsCabbageFree = true;
				for (Cabbage cabbage : cabbages) {
					if (cabbage.isCabbageInCabbage(center)) {
						locationIsCabbageFree = false;
					}
				}
			}

			if (i < N_BAD_CABBAGES) {
				Cabbage c = new BadCabbage(window, center);
				cabbages.add(c);
			} else if (i < (N_BAD_CABBAGES + (N_GOOD_CABBAGES / 2))) {
				Cabbage c = new GoodCabbage(window, center);
				cabbages.add(c);
			} else if (i < N_BAD_CABBAGES + N_GOOD_CABBAGES) {
				Cabbage c = new PsychedelicCabbage(window, center);
				cabbages.add(c);
			}

		}
		// Create the caterpillar
		cp = new Caterpillar(window, cabbages);
	}

	/**
	 * Moves the caterpillar within the graphics window every ANIMATION_PERIOD
	 * milliseconds.
	 * 
	 * @param e
	 *            the timer event
	 */
	public void timerExpired(GWindowEvent e) {
		// Did we get a new direction from the user?
		// Use isKeyboardEventNew to take the event into account
		// only once
		if (isKeyboardEventNew) {// if user presses keyboard, store key that is
									// pressed
			isKeyboardEventNew = false;
			cp.move(dirFromKeyboard);
		} else
			cp.move();// always keep moving until key is pressed
	}

	/**
	 * Moves the caterpillar according to the selection of the user i: NORTH, j:
	 * WEST, k: EAST, m: SOUTH
	 * 
	 * @param e
	 *            the keyboard event
	 */
	public void keyPressed(GWindowEvent e) {

		switch (Character.toLowerCase(e.getKey())) {
		case 'i':
			dirFromKeyboard = NORTH;
			break;
		case 'j':
			dirFromKeyboard = WEST;
			break;
		case 'k':
			dirFromKeyboard = EAST;
			break;
		case 'm':
			dirFromKeyboard = SOUTH;
			break;
		case 'q':
			System.exit(0); // abruptly ends the application
			break;
		default:
			return;
		}

		// new keyboard event
		isKeyboardEventNew = true;
	}

	/**
	 * The game is over. Starts a new game or ends the application
	 */
	private void endTheGame() {
		window.stopTimerEvents();
		// messageGameOver is an instance String that
		// describes the outcome of the game that just ended
		// (e.g. congratulations! you win)
		boolean again = anotherGame(messageGameOver);
		if (again) {
			initializeGame();
		} else {
			System.exit(0);
		}
	}

	/**
	 * Does the player want to play again?
	 */
	private boolean anotherGame(String s) {
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new CaterpillarGame();
	}
}
