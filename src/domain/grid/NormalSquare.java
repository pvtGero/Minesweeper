package domain.grid;



public class NormalSquare extends Square{

	private final int minesAround;
	
	public NormalSquare(int minesAround) {
		this.minesAround = minesAround;
	}

	public int getNumOfMinesAround() {
		return minesAround;
	}
	
	
}
