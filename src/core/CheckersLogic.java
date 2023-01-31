package core;

import java.awt.Point;
import java.util.Scanner;

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
 * @version 1.1
 */
public class CheckersLogic {
    private Board board;                    // The Checkers board
    private Board.Direction turn;           // The direction of current player
    private ui.CheckersTextConsole view;    // The player's view of the board
    private CheckersComputerPlayer cpu;     // The computer player
    private Scanner input;                  // The input stream

    // c* - current, d* - destination, *r - row, *c - column
    // e.g. cr - current row
    private int cr, cc, dr, dc;

    // Indicates if a pawn should continue capturing
    private boolean mustCapture = false;

    /**
     * Creates a new checker's game. Must call {@link #start start}
     * to commence the game.
     */
    public CheckersLogic() {
        board = new Board();
        view = new ui.CheckersTextConsole(System.out, board.new BoardView(),
            Board.BOARD_SIZE);
        turn = Board.Direction.UP;
        input = new Scanner(System.in);
        cpu = new CheckersComputerPlayer(board, Board.Direction.DOWN);
    }

    /**
     * Checks if the current player can make a capturing move
     *
     * @return      <code>true/<code> if at least one of the pawns of the
     *              current player can capture an opponent's pawn;
     *              <code>false</code> otherwise;
     */
    private boolean canPlayerCapture() {
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {
                if (!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == turn) {
                    if (board.canCapture(row, col)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if the current player can move.
     *
     * @return      <code>true/<code> if at least one of the pawns of the
     *              current player can make a non-capturing move;
     *              <code>false</code> otherwise;
     */
    private boolean canPlayerMove() {
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {
                if (!board.isPositionEmpty(row, col) &&
                    board.getDirection(row, col) == turn) {
                    if (board.canMove(row, col)) {
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
        
        updateView();
        view.print("\nExample: to move pawn from a3 to b4, enter a3-b4\n");
        view.print("\nPress Enter to begin game...");
        input.nextLine();
        

        while (true) {
            updateView();
            
            if (!canPlayerCapture() && !canPlayerMove()) {
                changePlayerTurn();
                String cpuStr = getCPUStr(choice);
                changePlayerTurn();
                
                view.print("\nPlayer %s%s -- You can't make a move." +
                    " Game over. Player %s%s wins.\n", turn.str(),
                    getCPUStr(choice), turn == Board.Direction.UP ?
                    Board.Direction.DOWN.str() :
                    Board.Direction.UP.str(), cpuStr);
                break;
            }
            
            if(!mustCapture) {
                view.print("\nPlayer %s%s -- your turn. Enter your move: ",
                    turn.str(), getCPUStr(choice));
            } else {
                view.print("\nPlayer %s%s -- it's still your turn." +
                    " Continue capturing with pawn at %c%d.\nEnter your move: ",
                    turn.str(), getCPUStr(choice), cc + 'a', cr + 1);
            }
            
            move = (choice == 'C' && turn == cpu.getDirection()) ?
                cpu.getMove(mustCapture, canPlayerCapture(), cr, cc) :
                input.nextLine().strip();
                
            if (choice == 'C' && turn == cpu.getDirection()) {
                view.print(move + "\n");
            }

            if (!validateMove(move)) {
                view.print("\nYour move was invalid. " + (mustCapture ?
                    "".format("Continue capturing with pawn at %c%d\n",
                    cc + 'a', cr + 1) :"Please enter a valid move.\n"));
            }
        }
    }    

    /**
     * Changes to the next player after current player is done
     * with his/her move(s).
     */
    private void changePlayerTurn() {
        turn = turn == Board.Direction.UP ?
            Board.Direction.DOWN : Board.Direction.UP;
    }

    private Point getCaptee(int cr, int cc, int dr, int dc) {        
        if (dr - cr != turn.b_dy()) {
            return null;
        }

        if (dc < cc) {
            if(cc - dc != Math.abs(turn.b_dy()) ||
                board.isPositionEmpty(cr + turn.s_dy(), cc - 1) ||
                board.getDirection(cr + turn.s_dy(), cc - 1) == turn) 
                return null;
            

            return new Point(cr + turn.s_dy(), cc - 1);
        } else {
            if (dc - cc != Math.abs(turn.b_dy()) ||
                board.isPositionEmpty(cr + turn.s_dy(), cc + 1) ||
                board.getDirection(cr + turn.s_dy(), cc + 1) == turn)
                return null;

            return new Point(cr + turn.s_dy(), cc + 1);
        }
    }
    
    private String getCPUStr(char choice) {
        return (choice == 'C' && turn == cpu.getDirection()) ? " (CPU)" : "";
    }
    
    private char getPlayingChoice() {
        String choice = input.nextLine().strip();
        
        while (!choice.equals("C") && !choice.equals("P")) {
            view.print("\nWrong option. Please enter C to play against " +
                       "\nComputer or P to play against another human: ");
            choice = input.nextLine().strip();
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

        if (move.charAt(0) < 'a' || move.charAt(0) > 'h' || 
            move.charAt(3) < 'a' || move.charAt(3) > 'h' ||
            move.charAt(1) < '0' || move.charAt(1) > '9' || 
            move.charAt(4) < '0' || move.charAt(4) > '9' ||
            move.charAt(2) != '-')  {
            return false;
        }

        if (mustCapture) {
            if(move.charAt(1) - '1' != cr || move.charAt(0) - 'a' != cc) {
                return false;
            }
        }

        cr = move.charAt(1) - '1';
        cc = move.charAt(0) - 'a';
        dr = move.charAt(4) - '1';
        dc = move.charAt(3) - 'a';

        if (board.isPositionEmpty(cr, cc) ||
            board.getDirection(cr, cc) != turn ||
            !board.isPositionEmpty(dr, dc)) {
            return false;
        }

        if (!canPlayerCapture()) {
            // check regular move
            if (turn.s_dy() + cr != dr ||
                Math.abs(cc - dc) != Math.abs(turn.s_dy())) {
                return false;
            }

            // make regular move            
            board.makeMove(cr, cc, dr, dc);
            changePlayerTurn();
        } else {
            Point captee = getCaptee(cr, cc, dr, dc);
            if (captee != null) {
                board.makeMove(cr, cc, dr, dc);
                board.removePawn(captee.x, captee.y);

                if (board.canCapture(dr, dc)) {
                    mustCapture = true;
                    cr = dr;
                    cc = dc;
                } else {
                    mustCapture = false;
                    changePlayerTurn();
                }
            } else {
                view.print("\nYou have to capture your " +
                    "opponent's piece.\n");
                return false;
            }
        }

        return true;
    }
}
