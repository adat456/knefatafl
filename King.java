package knefatafl;

public class King extends Piece {

    public King(Square square) {
        super(square);
    }
    
    public String determineStatus(Player attacker) {
        int[] kingCoords = getCurrentSquare().getCoords();

        if (
                isTrappedOnSide(kingCoords, "x", "+", attacker) &&
                isTrappedOnSide(kingCoords, "x", "-", attacker) &&
                isTrappedOnSide(kingCoords, "y", "+", attacker) &&
                isTrappedOnSide(kingCoords, "y", "-", attacker)
        ) {
            return "Attackers win.";
        }

        if (getCurrentSquare().isRefuge()) {
            return "Defenders win.";
        }

        return "";
    }

    private boolean isTrappedOnSide(int[] kingCoords, String axis, String operator, Player attacker) {
        // if there is a border, it is trapped
        if (!Square.squareExists(Square.changeCoords(kingCoords, axis, operator))) return true;
        Square sideSquare = Square.getSquare(Square.changeCoords(kingCoords, axis, operator));
        // if there is an attacker (true) or the square is hostile (true), it is trapped
        return (!attacker.getPieceBySquare(sideSquare).isEmpty() || sideSquare.isHostile());
    }
}
