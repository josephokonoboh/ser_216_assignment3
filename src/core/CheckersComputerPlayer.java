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
    
    public CheckersComputerPlayer(Board board, Board.Direction cpuDirection) {
        this.board = board;
        this.cpuDirection = cpuDirection;
        cpuMoves = new ArrayList<>();
    }
    
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
         
        // return cpuMoves.size() > 0 ? cpuMoves.get(0) : "";
        return cpuMoves.size() > 0 ? cpuMoves.get(
            (int) (Math.random() * cpuMoves.size())) : "";
    }
    
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

            return board.isPositionEmpty(row + b_dy, col - b_dy) &&
                !board.isPositionEmpty(row + s_dy, col - s_dy) &&
                (board.getDirection(row + s_dy, col - s_dy) != cpuDirection) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                col - b_dy + 'a', row + b_dy + 1) : "";
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

            return board.isPositionEmpty(row + dy, col - dy) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                col - dy + 'a', row + dy + 1) : "";
        }

        return "";
    }
    
    private String getRightCaptureMove(int row, int col) {
        if(!board.isPositionEmpty(row, col)) {            
            int b_dy = board.getDirection(row, col).b_dy();           
            int s_dy = board.getDirection(row, col).s_dy();

            return board.isPositionEmpty(row + b_dy, col + b_dy) &&
                !board.isPositionEmpty(row + s_dy, col + s_dy) &&
                (board.getDirection(row + s_dy, col + s_dy) != cpuDirection) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                col + b_dy + 'a', row + b_dy + 1) : "";
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

            return board.isPositionEmpty(row + dy, col + dy) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                col + dy + 'a', row + dy + 1) : "";
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
