package knefatafl;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class Eliminate {

    public static void eliminateOpponents(Square currentSquare, Player opponent) {
        int[] currentSquareCoords = currentSquare.getCoords();
        int x = currentSquareCoords[0];
        int y = currentSquareCoords[1];

        Piece[] belowEliminees = findAnAdjacentEliminee(opponent, x, y, "y", "+");
        Piece[] aboveEliminees = findAnAdjacentEliminee(opponent, x, y, "y", "-");
        Piece[] leftEliminees = findAnAdjacentEliminee(opponent, x, y, "x", "-");
        Piece[] rightEliminees = findAnAdjacentEliminee(opponent, x, y, "x", "+");

        var verticalEliminees = ArrayUtils.addAll(belowEliminees, aboveEliminees);
        var horizontalEliminees = ArrayUtils.addAll(leftEliminees, rightEliminees);
        var allEliminees = ArrayUtils.addAll(verticalEliminees, horizontalEliminees);
        for (Object eliminee : allEliminees) {
            if (eliminee != null) {
                Minion minion = (Minion)eliminee;
                System.out.println(minion.getID() + " has been eliminated.");
                minion.eliminate();
            }
        }
    }

    private static Piece[] findAnAdjacentEliminee(Player opponent, int x, int y, String axis, String operation) {
        Piece[] capturedPiece = new Piece[1];

        // is there a square?
        int[] firstSquareCoords = changeCoords(x, y, axis, operation);
        if (!Square.squareExists(firstSquareCoords)) return capturedPiece;
        // does the square have a piece?
        Square firstSquare = Square.getSquare(firstSquareCoords);
        if (!firstSquare.isOccupied()) return capturedPiece;
        // is there an opponent on the square?
        Piece[] potentialOpponentPiece = opponent.getPieceBySquare(firstSquare);
        if (potentialOpponentPiece[0] == null) return capturedPiece;
        // is there a second square?
        int[] secondSquareCoords = changeCoords(firstSquareCoords[0], firstSquareCoords[1], axis, operation);
        // no second square --> border
        if (!Square.squareExists(secondSquareCoords)) {
            capturedPiece = potentialOpponentPiece;
        } else {
            // is the second square a refuge/hostile square? is there an opponent or an ally on the square?
            Square secondSquare = Square.getSquare(secondSquareCoords);
            if (secondSquare.isRefuge() || secondSquare.isHostile()) capturedPiece = potentialOpponentPiece;
            if (secondSquare.isOccupied() && opponent.getPieceBySquare(secondSquare)[0] == null) capturedPiece = potentialOpponentPiece;
        }

        return capturedPiece;
    }

    private static int[] changeCoords(int x, int y, String axis, String operation) {
        int[] newCoords = {x, y};
        if (axis.equals("x")) {
            if (operation.equals("+")) {
                newCoords[0] = x + 1;
            } else if (operation.equals("-")) {
               newCoords[0] = x - 1;
            }
        } else if (axis.equals("y")) {
            if (operation.equals("+")) {
                newCoords[1] = y + 1;
            } else if (operation.equals("-")) {
                newCoords[1] = y - 1;
            }
        }
        return newCoords;
    }
}
