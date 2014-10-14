package domain.grid;

/**
 * This class represents a safe square.
 * 
 * @author Ludgero
 * 
 */
public class NormalSquare extends Square {

	private final int minesAround;

	/**
	 * Constructs and initializes a new safe square.
	 * 
	 * @param minesAround
	 *            The number of mines surrounding the square to be created
	 */
	public NormalSquare(int minesAround) {
		this.minesAround = minesAround;
	}

	/**
	 * @return The number of mines around this square
	 */
	public int getNumOfMinesAround() {
		return minesAround;
	}

}