package knefatafl;

import java.util.ArrayList;

public class Eliminate {

    public static void eliminateOpponents(Square currentSquare, Player ally, Player opponent) {
        int[] coords = currentSquare.getCoords();
        ArrayList<Piece> allEliminees = new ArrayList<>(10);

        // sandwich elimination
        allEliminees.addAll(findAnAdjacentEliminee(opponent, coords, "y", "+"));
        allEliminees.addAll(findAnAdjacentEliminee(opponent, coords, "y", "-"));
        allEliminees.addAll(findAnAdjacentEliminee(opponent, coords, "x", "-"));
        allEliminees.addAll(findAnAdjacentEliminee(opponent, coords, "x", "+"));

        // shield wall elimination
        allEliminees.addAll(findShieldWallEliminees(ally, opponent, coords));

        for (Piece eliminee : allEliminees) {
            Minion minion = (Minion)eliminee;
            System.out.println(minion.getID() + " has been eliminated.");
            minion.eliminate();
        }
    }

    private static ArrayList<Piece> findAnAdjacentEliminee(Player opponent, int[] coords, String axis, String operation) {
        ArrayList<Piece> capturedPiece = new ArrayList<>(1);

        // is there a square?
        int[] firstSquareCoords = changeCoords(coords, axis, operation);
        if (!Square.squareExists(firstSquareCoords)) return capturedPiece;
        // does the square have a piece?
        Square firstSquare = Square.getSquare(firstSquareCoords);
        if (!firstSquare.isOccupied()) return capturedPiece;
        // is there an opponent on the square?
        ArrayList<Piece> potentialOpponentPiece = opponent.getPieceBySquare(firstSquare);
        if (potentialOpponentPiece.isEmpty()) return capturedPiece;
        // is there a second square?
        int[] secondSquareCoords = changeCoords(firstSquareCoords, axis, operation);
        if (!Square.squareExists(secondSquareCoords)) return capturedPiece;

        // is the second square a refuge/hostile square? is there an opponent or an ally on the square? if so, capture >:)
        Square secondSquare = Square.getSquare(secondSquareCoords);
        if (secondSquare.isRefuge() || secondSquare.isHostile()) capturedPiece.addAll(potentialOpponentPiece);
        if (secondSquare.isOccupied() && opponent.getPieceBySquare(secondSquare).isEmpty()) capturedPiece.addAll(potentialOpponentPiece);

        return capturedPiece;
    }

    private static ArrayList<Piece> findShieldWallEliminees(Player ally, Player opponent, int[] pieceCoords) {
        ArrayList<Piece> eliminees = new ArrayList<>(7);
        int x = pieceCoords[0];
        int y = pieceCoords[1];

        // finds shield wall eliminees flush against the left or right walls
        if (x == 0 || x == 10) {
            eliminees.addAll(findPotentialShieldWallElimineesAndVerifyShieldWall(ally, opponent, pieceCoords, "y"));
        }
        // finds shield wall eliminees flush against the top or bottom walls
        if (y == 0 || y == 10) {
            eliminees.addAll(findPotentialShieldWallElimineesAndVerifyShieldWall(ally, opponent, pieceCoords, "x"));
        }

        return eliminees;
    }

    private static ArrayList<Piece> findPotentialShieldWallElimineesAndVerifyShieldWall(Player ally, Player opponent, int[] pieceCoords, String axis) {
        ArrayList<Piece> eliminees = new ArrayList<>(7);

        ArrayList<Piece> potentialGreaterEliminees = findPotentialShieldWallEliminees(opponent, pieceCoords, axis, "+", new ArrayList<Piece>(7));
        ArrayList<Piece> potentialLesserEliminees = findPotentialShieldWallEliminees(opponent, pieceCoords, axis, "-", new ArrayList<Piece>(7));
        // checks that there are potential eliminees before checking for a shield wall
        if (!potentialGreaterEliminees.isEmpty() && isShieldWallIntact(potentialGreaterEliminees, ally)) eliminees.addAll(potentialGreaterEliminees);
        if (!potentialLesserEliminees.isEmpty() && isShieldWallIntact(potentialLesserEliminees, ally)) eliminees.addAll(potentialLesserEliminees);

        return eliminees;
    }

    private static ArrayList<Piece> findPotentialShieldWallEliminees(Player opponent, int[] coords, String axis, String operator, ArrayList<Piece> potentialEliminees) {
        Square square = Square.getSquare(changeCoords(coords, axis, operator));
        // base case -- must not be a refuge square or an unoccupied square
        if (square.isRefuge() || !square.isOccupied()) {
            return potentialEliminees;
        } else {
            ArrayList<Piece> potentialEliminee = opponent.getPieceBySquare(square);
            // another base case -- if the piece is an ally
            if (potentialEliminee.isEmpty()) {
                return potentialEliminees;
            } else {
                potentialEliminees.addAll(potentialEliminee);
                return findPotentialShieldWallEliminees(opponent, changeCoords(coords, axis, operator), axis, operator, potentialEliminees);
            }
        }
    }

    private static boolean isShieldWallIntact(ArrayList<Piece> potentialEliminees, Player ally) {
        int[] coordsOfFirstPotentialEliminee = potentialEliminees.get(0).getCurrentSquare().getCoords();
        int[] coordsOfLastPotentialEliminee = potentialEliminees.get(potentialEliminees.size() - 1).getCurrentSquare().getCoords();

        // determine axis and operator for coords changes for front wall/minions
        String axisOfFrontMinions = "";
        String operatorOfFrontMinions = "";
        if (coordsOfFirstPotentialEliminee[0] == 0) {
            axisOfFrontMinions = "x";
            operatorOfFrontMinions = "+";
        }
        if (coordsOfFirstPotentialEliminee[0] == 10) {
            axisOfFrontMinions = "x";
            operatorOfFrontMinions = "-";
        }
        if (coordsOfFirstPotentialEliminee[1] == 0) {
            axisOfFrontMinions = "y";
            operatorOfFrontMinions = "+";
        }
        if (coordsOfFirstPotentialEliminee[1] == 10) {
            axisOfFrontMinions = "y";
            operatorOfFrontMinions = "-";
        }

        // determine axis and operators for coords changes of flanking walls/minions
        String axisOfFlankingMinions = axisOfFrontMinions.equals("x") ? "y" : "x";
        String operatorOfFirstMinion;
        if ((coordsOfFirstPotentialEliminee[0] + coordsOfFirstPotentialEliminee[1]) > (coordsOfLastPotentialEliminee[0] + coordsOfLastPotentialEliminee[1])) {
            operatorOfFirstMinion = "+";
        } else {
            operatorOfFirstMinion = "-";
        }
        String operatorOfLastMinion = operatorOfFirstMinion.equals("+") ? "-" : "+";

        // determine if flanking walls/squares contain ally pieces or at least a refuge square
        boolean flankingWallsIntact = true;
        Square firstFlankingSquare = Square.getSquare(changeCoords(coordsOfFirstPotentialEliminee, axisOfFlankingMinions, operatorOfFirstMinion));
        Square lastFlankingSquare = Square.getSquare(changeCoords(coordsOfLastPotentialEliminee, axisOfFlankingMinions, operatorOfLastMinion));
        if ((ally.getPieceBySquare(firstFlankingSquare).isEmpty() && !firstFlankingSquare.isRefuge()) || (ally.getPieceBySquare(lastFlankingSquare).isEmpty() && !lastFlankingSquare.isRefuge())) flankingWallsIntact = false;

        // determine if front wall/squares contain ally pieces
        boolean frontWallIntact = true;
        for (Piece potentialEliminee : potentialEliminees) {
            Square frontSquare = Square.getSquare(changeCoords(potentialEliminee.getCurrentSquare().getCoords(), axisOfFrontMinions, operatorOfFrontMinions));
            if (ally.getPieceBySquare(frontSquare).isEmpty()) frontWallIntact = false;
        }

        return flankingWallsIntact && frontWallIntact;
    }

    private static int[] changeCoords(int[] coords, String axis, String operation) {
        int[] newCoords = new int[2];
        if (axis.equals("x")) {
            if (operation.equals("+")) {
                newCoords[0] = coords[0] + 1;
                newCoords[1] = coords[1];
            } else if (operation.equals("-")) {
                newCoords[0] = coords[0] - 1;
                newCoords[1] = coords[1];
            }
        } else if (axis.equals("y")) {
            if (operation.equals("+")) {
                newCoords[0] = coords[0];
                newCoords[1] = coords[1] + 1;
            } else if (operation.equals("-")) {
                newCoords[0] = coords[0];
                newCoords[1] = coords[1] - 1;
            }
        }
        return newCoords;
    }
}
