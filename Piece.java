package knefatafl;

public abstract class Piece {
    private Square currentSquare;

    public Piece(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public void displayValidMoves() {

    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(Square newSquare) {
        this.currentSquare.toggleOccupied();
        newSquare.toggleOccupied();
        this.currentSquare = newSquare;
    }
}
