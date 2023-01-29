package core;

/**
 * Represents a two dimensional model of a Checkers game. 
 * @author  Joseph Okonoboh
 * @version 1.0
 */
public class Board {
    /**
     * The Board will be a two dimensional BOARD_SIZE by BOARD_SIZE square
     */
    public final static int BOARD_SIZE = 8;
    
    /**
     * Represents the direction of a Pawn.
     */
    public static enum Direction {
        /**
         * Represents pawns moving from bottom to top,
         */
        UP,
        
        /**
         * Represents pawns moving from top to bottom,
         */
        DOWN;
        
        /**
         * Queries how many steps a pawn will take to make a capture move.
         *
         * @return      <code>positive</code> if this is {@link #UP UP};
         *              <code>negative</code> otherwise;
         */
        public int b_dy() {
            return this == UP ? 2 : -2;
        }

        /**
         * Queries how many steps a pawn will take to make noncapturing move.
         *
         * 
         *
         * @return      <code>positive</code> if this is {@link #UP UP} 
         *              <code>negative</code> otherwise;
         */
        public int s_dy() {
            return b_dy() / 2;
        }

        /**
         * Gets string representation of this direction.
         *
         * @return      <code>X</code> if this is {@link #UP UP};
         *              <code>O</code> otherwise;
         */
        public String str() {
            return this == UP ? "X" : "O";
        }
    }

    private Pawn[][] pawns;
    
    /**
     * Creates a new Board.
     */
    public Board() {
        pawns = new Pawn[BOARD_SIZE][BOARD_SIZE];

        clearBoard();
        fillBoard();
    }    

    /**
     * This inner class represents a read only view of the board. 
     *
     */
    public class BoardView {
        /**
         * Gets the string representation of a position on this board.
         *
         * @param row   row of position
         * @param col   col of position
         * @return      <code>_</code> if (row, col) is empty;
         *              <code>X</code> if pawn at (row, col) is moving up;
         *              <code>O</code> otherwise.
         */
        public String getPosStr(int row, int col) {
            if(isPositionEmpty(row, col)) {
                return "_";
            }

            return pawns[row][col].getDirection().str();
        }
    }

    /**
     * Checks if a pawn at a position can currently capture an opponent's pawn
     *
     * @param row   The row of the attacking pawn
     * @param col   The column of the attacking pawn
     *
     * @return      <code>true</code> if the pawn at (row, col) can
     *              capture an opponent's pawn; <code>false</code> otherwise;
     */
    public boolean canCapture(int row, int col) {
        if(!isPositionEmpty(row, col)) {
            Pawn p = pawns[row][col];

            int b_dy = p.getDirection().b_dy();
            int s_dy = p.getDirection().s_dy();

            // Check forward left capture
            if(isPositionEmpty(row + b_dy, col - b_dy) &&
               positionHasOpponent(p, row + s_dy, col - s_dy)) {
                return true;
            }

            // Check forward right capture
            if(isPositionEmpty(row + b_dy, col + b_dy) &&
               positionHasOpponent(p, row + s_dy, col + s_dy)) {
                return true;
            }
        }

        return false;
    }    

    /**
     * Checks if a pawn at a position has the ability to make a non-capturing
     * forward move.
     *
     * @param row   The row of the pawn
     * @param col   The col of the pawn
     *
     * @return      <code>true</code> if the pawn at (row, col) can make a
     *              non-capture move; <code>false</code> otherwise;
     */
    public boolean canMove(int row, int col) {
        if(!isPositionEmpty(row, col)) {
            Pawn p = pawns[row][col];
            int s_dy = p.getDirection().s_dy();

            return isPositionEmpty(row + s_dy, col - s_dy) ||
                   isPositionEmpty(row + s_dy, col + s_dy);
        }

        return false;
    }
    
    /**
     * Gets the direction of a pawn on the board.
     *
     * @param row   row of the pawn
     * @param col   column of the pawn
     *
     * @throws      NullPointerException if there is no pawn at (row, col)
     *
     * @return      The direction of pawn at (row, col)
     */
    public Board.Direction getDirection(int row, int col) {
        return pawns[row][col].getDirection();
    }
    
    
    
    
   
    
    
    
    
    

    /**
     * Checks if a board position is empty.
     *
     * @param row   The zero indexed row to check
     * @param col   The zero-indexed col to check
     *
     * @return      <code>true</code> if there is no pawn at (row, col);
     *              <code>false</code> otherwise;
     */
    public boolean isPositionEmpty(int row, int col) {
        return isPositionValid(row, col) && pawns[row][col] == null;
    }    

    /**
     * Checks if a board position is valid.
     *
     * @param row   The zero-indexed row of the board
     * @param col   The zero-indexed col of the board
     *
     * @return      <code>true</code> if (row, col) is a valid position;
     *              <code>false</code> otherwise;
     */
    public boolean isPositionValid(int row, int col) {
        return 0 <= row && row < BOARD_SIZE &&
               0 <= col && col < BOARD_SIZE;
    }    

    /**
     * Moves a pawn to another position. 
     *
     * @param cr   Current row of the pawn
     * @param cc   Current column of the pawn
     * @param dr   Destination row of the pawn
     * @param dc   Destination column of the pawn
     */
    public void makeMove(int cr, int cc, int dr, int dc) {
        if(!isPositionEmpty(cr, cc) && isPositionEmpty(dr, dc)) {
            Pawn p = pawns[cr][cc];
            pawns[cr][cc] = null;
            pawns[dr][dc] = p;
        }
    }
   

    /**
     * Removes a pawn from the board.
     *
     * @param row row of target pawn
     * @param col column of target pawn
     */
    public void removePawn(int row, int col) {
        if(isPositionValid(row, col)) {
            pawns[row][col] = null;
        }
    }

    /**
     * Adds a pawn to the board.
     *
     * @param p     Pawn to be added
     * @param row   row of target pawn
     * @param col   column of target pawn
     */
    private void addPawnToBoard(Pawn p, int row, int col) {
        if(isPositionEmpty(row, col)) {
            pawns[row][col] = p;
        }
    }

    private void clearBoard() {
        for(int row = 0; row < BOARD_SIZE; ++row) {
            for(int col = 0; col < BOARD_SIZE; ++col) {
                pawns[row][col] = null;
            }
        }
    }

    private void fillBoard() {
        addPawnToBoard(new Pawn(Board.Direction.UP), 0, 0);
        addPawnToBoard(new Pawn(Board.Direction.UP), 0, 2);
        addPawnToBoard(new Pawn(Board.Direction.UP), 0, 4);
        addPawnToBoard(new Pawn(Board.Direction.UP), 0, 6);

        addPawnToBoard(new Pawn(Board.Direction.UP), 1, 1);
        addPawnToBoard(new Pawn(Board.Direction.UP), 1, 3);
        addPawnToBoard(new Pawn(Board.Direction.UP), 1, 5);
        addPawnToBoard(new Pawn(Board.Direction.UP), 1, 7);

        addPawnToBoard(new Pawn(Board.Direction.UP), 2, 0);
        addPawnToBoard(new Pawn(Board.Direction.UP), 2, 2);
        addPawnToBoard(new Pawn(Board.Direction.UP), 2, 4);
        addPawnToBoard(new Pawn(Board.Direction.UP), 2, 6);

        addPawnToBoard(new Pawn(Board.Direction.DOWN), 7, 1);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 7, 3);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 7, 5);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 7, 7);

        addPawnToBoard(new Pawn(Board.Direction.DOWN), 6, 0);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 6, 2);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 6, 4);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 6, 6);

        addPawnToBoard(new Pawn(Board.Direction.DOWN), 5, 1);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 5, 3);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 5, 5);
        addPawnToBoard(new Pawn(Board.Direction.DOWN), 5, 7);
    }

    private boolean positionHasOpponent(Pawn p, int row, int col) {
        return !isPositionEmpty(row, col) &&
            pawns[row][col].getDirection() != p.getDirection();
    }
}
