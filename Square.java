package knefatafl;

public class Square {
    private int[] coords;
    private boolean occupied = false;
    private boolean refuge;
    private boolean hostile;
    private static Square[][] board = new Square[11][11];

    public Square(int[] coords, boolean refuge, boolean hostile) {
        this.coords = coords;
        this.refuge = refuge;
        this.hostile = hostile;
    }

    public static void createBoard() {
        Square[][] newBoard = new Square[11][11];

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if ((i == 0 || i == 10) && (j == 0 || j == 10)) {
                    newBoard[i][j] = new Square(new int[] {i, j}, true, false);
                } else if (i == 5 && j == 5) {
                    newBoard[i][j] = new Square(new int[] {i, j}, false, true);
                } else {
                    newBoard[i][j] = new Square(new int[] {i, j}, false, false);
                }
            }
        }
        board = newBoard;
    }

    public static Square getSquare(int[] coords) throws IllegalArgumentException {
        int x = coords[0];
        int y = coords[1];

        if (x < 0 || x > 10 || y < 0 || y > 10) 
            throw new IllegalArgumentException("This square falls outside the bounds of the board.");
        
        return board[x][y];
    }

    // public Square[] getSurroundingSquares() {}

    public void toggleOccupied() {
        occupied = !occupied;
    }

    public int[] getCoords() {
        return coords;
    }
    public boolean isOccupied() {
        return occupied;
    }
    public boolean isRefuge() {
        return refuge;
    }
    public boolean isHostile() {
        return hostile;
    }
}
