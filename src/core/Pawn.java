package core;

/**
 * Represents a Pawn in a Checker's game
 *
 * @author  Joseph Okonoboh
 * @version 1.0
 */
class Pawn {
    private final Board.Direction direction;

    /**
     * Creates a new Pawn.
     *
     * @param direction The direction of the pawn
     *
     * @see #Board.Direction
     */
    public Pawn(Board.Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the direction of this pawn.
     *
     * @return <code>UP</code> if this is {@link Board.Direction#UP UP};
     *         <code>DOWN</code> otherwise;
     */
    public Board.Direction getDirection() {
        return direction;
    }
}
