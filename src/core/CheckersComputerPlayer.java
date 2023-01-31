package core;

import java.util.ArrayList;

/**
 * Represents a CPU Player. 
 * @author  Joseph Okonoboh
 * @version 1.0
 */
public class CheckersComputerPlayer {
    private Board board;
    private Board.Direction cpuDirection;
    
    private ArrayList<String> cpuMoves;
    
    /**
     * Creates a CPU Player. 
     * @param   board           The board of this checkers game
     * @param   cpuDirection    The direction of the CPU
     */
    public CheckersComputerPlayer(Board board, Board.Direction cpuDirection) {
        this.board = board;
        this.cpuDirection = cpuDirection;
        cpuMoves = new ArrayList<>();
    }
    
    /**
     * Returns a random valid move. If the CPU can capture, then this will be
     * a capture move.
     * @param   mustCapture     Set to true if and only if cpu pawn at
     *                          (row, col) captured an opponent's pawn in the
     *                          last move and can still capture the opponent's
     *                          pawn in the current move.
     * @param   canCapture      Set to true if at least one of the CPU's pawns
     *                          can capture an opponent's pawn
     * @param   row             The row of pawn
     * @param   col             The column of the pawn
     *
     * @return                  A random move. Capturing moves are given
     *                          precedence over non-capturing moves.
     */
    public String getMove(boolean mustCapture, boolean canCapture,
                          int row, int col) {
        cpuMoves.clear();
        
        if (mustCapture) {
            String move = getLeftCaptureMove(row, col);
            if(!move.equals("")) cpuMoves.add(move);
            
            move = getRightCaptureMove(row, col);
            if(!move.equals("")) cpuMoves.add(move);
        } else if (canCapture) {
            populateCPUCaptureMoves();
        } else {
            populateCPURegularMoves();
        }         
        
        return cpuMoves.size() > 0 ? cpuMoves.get(
            (int) (Math.random() * cpuMoves.size())) : "";
    }
    
    /**
     * Returns the direction of the computer player
     *
     * @return                  Direction of the computer player
     */
    public Board.Direction getDirection() { return cpuDirection; }
    
    /**
    * Gets a possible left capturing move for a pawn.
    *
    * @param row   The row of the pawn
    * @param col   The col of the pawn
    *
    * @return      <code>move</code> if the pawn at (row, col) can make a
    *              capture left move; <code>""</code> otherwise;
    */
    private String getLeftCaptureMove(int row, int col) {
        if(!board.isPositionEmpty(row, col)) {            
            int b_dy = board.getDirection(row, col).b_dy();           
            int s_dy = board.getDirection(row, col).s_dy();
            
            int dest_row = row + b_dy;
            int dest_col = col - b_dy;
            int enemy_row = row + s_dy;
            int enemy_col = col - s_dy;

            return
                board.isPositionValid(dest_row, dest_col) &&
                board.isPositionEmpty(dest_row, dest_col) &&
                !board.isPositionEmpty(enemy_row, enemy_col) &&
                (board.getDirection(enemy_row, enemy_col) != cpuDirection) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                dest_col + 'a', dest_row + 1) : "";
        }

        return "";
    }
    
    
    /**
    * Gets a possible left noncapturing move for a pawn.
    *
    * @param row   The row of the pawn
    * @param col   The col of the pawn
    *
    * @return      <code>move</code> if the pawn at (row, col) can make a
    *              non-capture left move; <code>""</code> otherwise;
    */
    private String getLeftMove(int row, int col) {
        
        if(!board.isPositionEmpty(row, col)) {            
            int dy = board.getDirection(row, col).s_dy();
            int dest_row = row + dy;
            int dest_col = col - dy;

            return board.isPositionValid(dest_row, dest_col) &&
                   board.isPositionEmpty(dest_row, dest_col) ?
                   String.format("%c%d-%c%d", col + 'a', row + 1,
                   dest_col + 'a', dest_row + 1) : "";
        }

        return "";
    }
    
    private String getRightCaptureMove(int row, int col) {
        if(!board.isPositionEmpty(row, col)) {            
            int b_dy = board.getDirection(row, col).b_dy();           
            int s_dy = board.getDirection(row, col).s_dy();
            
            int dest_row = row + b_dy;
            int dest_col = col + b_dy;
            int enemy_row = row + s_dy;
            int enemy_col = col + s_dy;

            return
                board.isPositionValid(dest_row, dest_col) &&
                board.isPositionEmpty(dest_row, dest_col) &&
                !board.isPositionEmpty(enemy_row, enemy_col) &&
                (board.getDirection(enemy_row, enemy_col) != cpuDirection) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                dest_col + 'a', dest_row + 1) : "";
        }

        return "";
    }
    
    /**
     * Gets a possible right noncapturing move for a pawn.
     *
     * @param row   The row of the pawn
     * @param col   The col of the pawn
     *
     * @return      <code>"xx-xx"</code> if the pawn at (row, col) can make a
     *              non-capture right move; <code>""</code> otherwise;
     */
    private String getRightMove(int row, int col) {
        if(!board.isPositionEmpty(row, col)) {            
            int dy = board.getDirection(row, col).s_dy();
            int dest_row = row + dy;
            int dest_col = col + dy;

            return board.isPositionValid(dest_row, dest_col) &&
                   board.isPositionEmpty(dest_row, dest_col) ?
                   String.format("%c%d-%c%d", col + 'a', row + 1,
                   dest_col + 'a', dest_row + 1) : "";
        }

        return "";
    }    

    private void populateCPURegularMoves() {
        String move;
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {            
                if(!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == cpuDirection) {
                    
                    move = getLeftMove(row, col);
                    if (!move.equals("")) { cpuMoves.add(move); }
                    
                    move = getRightMove(row, col);
                    if (!move.equals("")) { cpuMoves.add(move); }
                }
            }
        }
    }

    private void populateCPUCaptureMoves() {
        String move;
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {            
                if(!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == cpuDirection) {
                    
                    move = getLeftCaptureMove(row, col);
                    if (!move.equals("")) { cpuMoves.add(move); }
                    
                    move = getRightCaptureMove(row, col);
                    if (!move.equals("")) { cpuMoves.add(move); }
                }
            }
        }
    }
}
