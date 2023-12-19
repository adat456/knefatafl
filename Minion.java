package knefatafl;

public class Minion extends Piece {
    private String id;
    private boolean eliminated = false;

    public Minion(String id, Square square) {
        super(square);
        this.id = id;
    }

    public void eliminate() {
        eliminated = true;
        setCurrentSquare(new Square(new int[] {-1, -1}));
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public String getID() { return id; }
}
