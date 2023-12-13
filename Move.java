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
        String userChoice = scanner.next().trim().toLowerCase();
        System.out.println(userChoice);
        if (!userChoice.equals("y") && !userChoice.equals("yes")) {
            moveLimit = Integer.MAX_VALUE;
            return;
        }

        while (moveLimit == 0) {
            System.out.print("Move limit: ");
            if (scanner.hasNextInt()) {
                if (scanner.nextInt() > 0) {
                    moveLimit = scanner.nextInt();
                } else {
                    System.out.print("Please enter an integer value greater than 0. ");
                }
            } else {
                System.out.print("Please enter an integer value greater than 0. ");
            }
        }

        scanner.nextLine();
    }

    public static boolean isMoveValid(Game game, Piece piece, Square startingSquare, Square endingSquare) throws IllegalArgumentException {
        int[] startingSquareCoords = startingSquare.getCoords();
        int[] endingSquareCoords = endingSquare.getCoords();

        // are the squares different?
        if (startingSquareCoords[0] == endingSquareCoords[0] && startingSquareCoords[1] == endingSquareCoords[1]) {
            game.displayErrorMessage("Starting and ending squares are the same.");
            return false;
        }
        // is only one dimension changing (x or y)?
        if (startingSquareCoords[0] != endingSquareCoords[0] && startingSquareCoords[1] != endingSquareCoords[1]) {
            game.displayErrorMessage("Diagonal moves are not allowed.");
            return false;
        }
        // is the square a refuge (and, if so, can the piece move into the refuge) or hostile?
        if ((endingSquare.isRefuge() && piece instanceof Minion) || endingSquare.isHostile()) {
            game.displayErrorMessage("The square is a hostile square for this particular piece.");
            return false;
        }
        // is there a piece there?
        if (endingSquare.isOccupied()) {
            game.displayErrorMessage("The square is already occupied.");
            return false;
        }
        // are there any pieces in between?
        int numPiecesInMiddle = getNumPiecesInMiddle(startingSquareCoords, endingSquareCoords);
        if (numPiecesInMiddle > 0) {
            game.displayErrorMessage("There are " + numPiecesInMiddle + " pieces between the starting and ending squares.");
            return false;
        }

        return true;
    }

    private static int getNumPiecesInMiddle(int[] startingSquareCoords, int[] endingSquareCoords) {
        int numPiecesInMiddle = 0;
        // horizontal edition
        if (startingSquareCoords[0] != endingSquareCoords[0]) {
            // is the starting x less than the ending x? work up
            if (startingSquareCoords[0] < endingSquareCoords[1]) {
                for (int i = startingSquareCoords[0] + 1; i < endingSquareCoords[0]; i++) {
                    if (Square.getSquare(new int[] {i, startingSquareCoords[1]}).isOccupied()) numPiecesInMiddle += 1;
                }
            // is the starting x greater than the ending x? work down
            } else {
                for (int i = startingSquareCoords[0] - 1; i > endingSquareCoords[0]; i--) {
                    if (Square.getSquare(new int[] {i, startingSquareCoords[1]}).isOccupied()) numPiecesInMiddle += 1;
                }
            }
        }
        // vertical edition
        if (startingSquareCoords[1] != endingSquareCoords[1]) {
            if (startingSquareCoords[1] < endingSquareCoords[1]) {
                for (int i = startingSquareCoords[1] + 1; i < endingSquareCoords[1]; i++) {
                    if (Square.getSquare(new int[] {startingSquareCoords[0], i}).isOccupied()) numPiecesInMiddle += 1;
                }
            } else {
                for (int i = startingSquareCoords[1] - 1; i > endingSquareCoords[1]; i--) {
                    if (Square.getSquare(new int[] {startingSquareCoords[0], i}).isOccupied()) numPiecesInMiddle += 1;
                }
            }

        }
        return numPiecesInMiddle;
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
