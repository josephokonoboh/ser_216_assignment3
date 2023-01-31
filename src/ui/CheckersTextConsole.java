package ui;

import core.Board.BoardView;
import java.io.PrintStream;

/**
 * Provides a view of the Checkers game. 
 * @author  Joseph Okonoboh
 * @version 1.0
 */
public class CheckersTextConsole {
    private PrintStream stream;
    private BoardView view;
    private int size;

    /**
     * Creates an output stream where the board will be drawn
     *
     * @param   stream  The board will be output to this stream
     * @param   view    A readonly view of the pawns on the board
     * @param   size    The size of the Board
     */
    public CheckersTextConsole(PrintStream stream, BoardView view, int size) {
        this.stream = stream;
        this.view = view;
        this.size = size;
    }

    /**
     * Sends the board representation to the stream
     */
    public void draw() {
        for(int row = size - 1; row >= 0; --row) {
            stream.format("\n%d |", row + 1);
            for(int col = 0; col < size; ++col) {
                stream.format(" %s |", view.getPosStr(row, col).toLowerCase());
            }
        }

        stream.print("\n   ");
        for(char c = 'a'; c < 'a' + size; ++c) {
            stream.format(" %c  ", c);
        }

        stream.println();
    }
    
    /**
     * Prints message to the stream
     *
     * @param message   The string to be printed to stream. Can contain the
     *                  standard {@link String#format format specifiers}.
     * @param obj      The values of the specifiers in message
     */
    public void print(String message, Object... obj) {
        stream.format(message, obj);
    }
}
