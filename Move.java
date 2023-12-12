package knefatafl;

import java.util.Scanner;

// public class Move implements Comparable<Move> {
public class Move {
    private Piece piece;
    private Square startingSquare;
    private Square endingSquare;
    private static int moveLimit;
    private static int totalMoves = 0;

    public Move(Piece piece, Square startingSquare, Square endingSquare) {
        this.piece = piece;
        this.startingSquare = startingSquare;
        this.endingSquare = endingSquare;
        incrementTotalMoves();
    }

    public static void determineMoveLimit(Scanner scanner) {
        System.out.println("Would you like to enforce a move limit? (Y/N)");
        String userChoice = scanner.nextLine().trim().toLowerCase();
        if (!userChoice.equals("y") && !userChoice.equals("yes")) {
            moveLimit = Integer.MAX_VALUE;
            return;
        }

        System.out.print("Enter a move limit: ");
        while (moveLimit == 0) {
            if (scanner.hasNextInt()) {
                if (scanner.nextInt() > 0) {
                    moveLimit = scanner.nextInt();
                } else {
                    System.out.print("Please enter an integer value greater than 0. Move limit: ");
                }
            } else {
                System.out.println("Please enter an integer value greater than 0. Move limit: ");
            }
        }

        scanner.nextLine();
    }

    public static void validate(Piece piece, Square startingSquare, Square endingSquare) throws IllegalArgumentException {
        int[] startingSquareCoords = startingSquare.getCoords();
        int[] endingSquareCoords = endingSquare.getCoords();

        // are the squares different?
        if (startingSquareCoords[0] == endingSquareCoords[0] && startingSquareCoords[1] == endingSquareCoords[1]) 
            throw new IllegalArgumentException("Starting and ending squares are the same.");
        // is only one dimension changing (x or y)?
        if (startingSquareCoords[0] != endingSquareCoords[0] && startingSquareCoords[1] != endingSquareCoords[1])
            throw new IllegalArgumentException("Diagonal moves are not allowed.");
        // is the square a refuge (and, if so, can the piece move into the refuge) or hostile?
        if ((endingSquare.isRefuge() && piece instanceof Minion) || endingSquare.isHostile())
            throw new IllegalArgumentException("The square is a hostile square for this particular piece.");
        // is there a piece there?
        if (endingSquare.isOccupied()) throw new IllegalArgumentException("The square is already occupied.");
    }

    // public Piece[] findEliminees() {}

    public void removeEliminees() {

    }

    private static void incrementTotalMoves() {
        totalMoves += 1;
    }

    // @Override public int compareTo(Move move2) {}

    public static int getMoveLimit() {
        return moveLimit;
    }
    public static int getTotalMoves() {
        return totalMoves;
    }
}
