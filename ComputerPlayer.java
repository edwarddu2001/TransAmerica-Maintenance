import java.awt.Color;
import java.util.ArrayList;
/**
 * To create new strategy: extend ComputerPlayer and have its
 * constructor call the superclass with a color, cities, and a
 * name. The subclass you create will implement the method runTurn.
 * The computer player is informed of the hand, state of board, 
 */
public abstract class ComputerPlayer extends Player {
	
	private ReadOnlyGrid grid;
	
	public ComputerPlayer(Color c, String name) {
		super(c, name);
	}

	abstract Object runTurn(boolean firstturn,boolean firstRailPlaced, Object grid);
}