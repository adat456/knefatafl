package knefatafl;

import java.util.ArrayList;

public abstract class Piece {
    private Square currentSquare;

    public Piece(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public ArrayList<Square> findAllValidSquares() {
        ArrayList<Square> allValidSquares = new ArrayList<>();
        int[] startingCoords = currentSquare.getCoords();

        if (this instanceof Minion) {
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "y", "-", new ArrayList<Square>(), false));
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "y", "+", new ArrayList<Square>(), false));
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "x", "-", new ArrayList<Square>(), false));
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "x", "+", new ArrayList<Square>(), false));
        } else if (this instanceof King) {
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "y", "-", new ArrayList<Square>(), true));
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "y", "+", new ArrayList<Square>(), true));
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "x", "-", new ArrayList<Square>(), true));
            allValidSquares.addAll(findValidSquaresInOneDirection(startingCoords, "x", "+", new ArrayList<Square>(), true));
        }

        return allValidSquares;
    }

    private ArrayList<Square> findValidSquaresInOneDirection(int[] startingCoords, String axis, String operation, ArrayList<Square> validSquares, boolean isKing) {
        int[] adjustedCoords = Square.changeCoords(startingCoords, axis, operation);
        // base case -- if the square does not exist
        if (!Square.squareExists(adjustedCoords)) return validSquares;
        Square adjustedSquare = Square.getSquare(adjustedCoords);
        // 2nd base case -- if the square is hostile, occupied, or a refuge and the piece is not a king
        if (adjustedSquare.isHostile() || adjustedSquare.isOccupied() || (!isKing && adjustedSquare.isRefuge())) {
            return validSquares;
        } else {
            validSquares.add(adjustedSquare);
            return findValidSquaresInOneDirection(adjustedCoords, axis, operation, validSquares, isKing);
        }
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
