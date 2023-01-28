package core;

import java.awt.Point;
import java.util.Scanner;
    
import java.util.ArrayList;

/**
 * Defines the game logic of a Checkers game.
 *
 * In this variation, every pawn can only move forward from its player's
 * perspective.
 *
 * <p> The rules are as follows: </p>
 * <ul>
 *  <li>Every capturing move, if available, must be made</li>
 *  <li>Consecutive capturing moves must be made until there are no more left</li>
 *  <li>No pawn can move backward</li>
 *  <li>Kinging is not allowed</li>
 *  <li>A player loses when none of their pawns can make a move</li>
 * </ul> 
 * @author  Joseph Okonoboh
 * @version 1.0
 */
public class CheckersLogic {
    private Board board;
    private Board.Direction turn;
    private ui.CheckersTextConsole view;
    
    private Scanner input;

    private int cr, cc, dr, dc;

    private boolean moveLock = false;
    
    private ArrayList<String> cpuMoves;

    /**
     * Creates a new checker's game. Must call {@link #start start} to 
     * commence the game.
     *
     */
    public CheckersLogic() {
        board = new Board();
        view = new ui.CheckersTextConsole(System.out, board.new BoardView(),
            Board.BOARD_SIZE);
        turn = Board.Direction.UP;
        input = new Scanner(System.in);
        cpuMoves = new ArrayList<>();
    }

    private boolean canPlayerCapture() {
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {
                if(!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == turn) {
                    if(board.canCapture(row, col)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean canPlayerMove() {
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {
                if(!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == turn) {
                    if(board.canMove(row, col)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    /**
     * Starts a checker's game on the command line.
     *
     * The standard output and input are used to inform the player
     * of the game's state and the get moves from the players, respectively.
     */
    public void start() {
        String move;
        char choice;
        
        greetings();
        choice = getPlayingChoice();

        while (true) {
            updateView();
            
            if (!canPlayerCapture() && !canPlayerMove()) {
                System.out.format("\nPlayer %s -- You can't make a move." +
                    " Game over. Player %s wins.\n", turn.str(),
                turn == Board.Direction.UP ?
                        Board.Direction.DOWN.str() :
                        Board.Direction.UP.str());
                break;
            }
            
            System.out.format("\nPlayer %s -- your turn. Enter your move: ",
                turn.str());
            
            move = (choice == 'C' && turn == Board.Direction.DOWN) ?
                getComputerMove() : input.nextLine().strip();
                
            if (choice == 'C' && turn == Board.Direction.DOWN) {
                view.print(move + "\n");
            }

            if (!validateMove(move)) {
                System.err.println("\nYour move was invalid. " +
                    "Please enter a valid move.\n");
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private String getComputerMove() {
        cpuMoves.clear();
        
        if (moveLock) {
            String move = getLeftCaptureMove(cr, cc);
            if(!move.equals("")) cpuMoves.add(move);
            
            move = getRightCaptureMove(cr, cc);
            if(!move.equals("")) cpuMoves.add(move);
        } else if (canPlayerCapture()) {
            populateCPUCaptureMoves();
        } else {
            populateCPURegularMoves();
        }
         
        return cpuMoves.size() > 0 ? cpuMoves.get(0) : "";
        /*return cpuMoves.size() > 0 ? cpuMoves.get(
            (int) (Math.random() * cpuMoves.size())) : "";*/
    }
    

    private void populateCPURegularMoves() {
        String move;
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {            
                if(!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == Board.Direction.DOWN) {
                    
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
                    board.getDirection(row, col) == Board.Direction.DOWN) {
                    
                    move = getLeftCaptureMove(row, col);
                    if (!move.equals("")) { cpuMoves.add(move); }
                    
                    move = getRightCaptureMove(row, col);
                    if (!move.equals("")) { cpuMoves.add(move); }
                }
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private void changePlayerTurn() {
        turn = turn == Board.Direction.UP ?
            Board.Direction.DOWN : Board.Direction.UP;
    }

    private Point getCaptee(int cr, int cc, int dr, int dc) {
        if(dr - cr != turn.b_dy()) {
            return null;
        }

        if(dc < cc) {
            if(dc - cc != -2 ||
                board.isPositionEmpty(cr + turn.s_dy(), cc - 1) ||
                board.getDirection(cr + turn.s_dy(), cc - 1) == turn) 
                return null;
            

            return new Point(cr + turn.s_dy(), cc - 1);
        } else {
            if(dc - cc != 2 ||
                board.isPositionEmpty(cr + turn.s_dy(), cc + 1) ||
                board.getDirection(cr + turn.s_dy(), cc + 1) == turn)
                return null;

            return new Point(cr + turn.s_dy(), cc + 1);
        }
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
    
    private String getLeftCaptureMove(int row, int col) {
        if(!board.isPositionEmpty(row, col)) {            
            int b_dy = board.getDirection(row, col).b_dy();           
            int s_dy = board.getDirection(row, col).s_dy();

            return board.isPositionEmpty(row + b_dy, col - b_dy) &&
                !board.isPositionEmpty(row + s_dy, col - s_dy) &&
                (board.getDirection(row + s_dy, col - s_dy) != turn) ?
                String.format("%c%d-%c%d", col + 'a', row + 1,
                col - b_dy + 'a', row + b_dy + 1) : "";
        }

        return "";
    }
    
    private String getRightCaptureMove(int row, int col) {
        if(!board.isPositionEmpty(row, col)) {            
            int b_dy = board.getDirection(row, col).b_dy();           
            int s_dy = board.getDirection(row, col).s_dy();

            return board.isPositionEmpty(row + b_dy, col + b_dy) &&
                !board.isPositionEmpty(row + s_dy, col + s_dy) &&
                (board.getDirection(row + s_dy, col + s_dy) != turn) ?
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
    
    private char getPlayingChoice() {
        String choice = input.nextLine().strip();
        
        while(!choice.equals("C") && !choice.equals("P")) {
            view.print("\nWrong option. Please enter C to play against " +
                       "\nComputer or P to play against another human: ");
            choice = input.nextLine();
        }
        
        return choice.charAt(0);
    }
    
    private void greetings() {
        view.print("\nWelcome to Checkers:\n====================\n");
        view.print("Enter C to play against computer or P to play ");
        view.print("against another human: ");
    }

    private void updateView() {
        view.draw();
    }

    private boolean validateMove(String move) {
        if (move.length() != 5) {
            return false;
        }

        if(move.charAt(0) < 'a' || move.charAt(0) > 'h' || 
           move.charAt(3) < 'a' || move.charAt(3) > 'h' ||
           move.charAt(1) < '0' || move.charAt(1) > '9' || 
           move.charAt(4) < '0' || move.charAt(4) > '9' ||
           move.charAt(2) != '-')  {
            return false;
        }

        if(moveLock) {
            if(move.charAt(1) - 49 != cr || move.charAt(0) - 97 != cc) {
                return false;
            }
        }

        cr = move.charAt(1) - 49;
        cc = move.charAt(0) - 97;
        dr = move.charAt(4) - 49;
        dc = move.charAt(3) - 97;

        if(board.isPositionEmpty(cr, cc) ||
           board.getDirection(cr, cc) != turn ||
           !board.isPositionEmpty(dr, dc)) {
            return false;
        }

        if(!canPlayerCapture()) {
            // check regular move
            if(turn.s_dy() + cr != dr || Math.abs(cc - dc) != 1) {
                return false;
            }

            // make regular move
            
            board.makeMove(cr, cc, dr, dc);
            changePlayerTurn();
        } else {
            Point captee = getCaptee(cr, cc, dr, dc);
            if(captee != null) {
                board.makeMove(cr, cc, dr, dc);
                board.removePawn(captee.x, captee.y);

                if(board.canCapture(dr, dc)) {
                    moveLock = true;
                    cr = dr;
                    cc = dc;
                    System.out.format("\nPlayer %s -- it's still your turn." +
                        " Continue capturing with pawn at %c%d: \n",
                        turn.str(), cc + 97, cr + 1);
                } else {
                    moveLock = false;
                    changePlayerTurn();
                }
            } else {
                System.err.println("\nYou have to capture your " +
                    "opponent's piece.\n");
                return false;
            }
        }

        return true;
    }
}
