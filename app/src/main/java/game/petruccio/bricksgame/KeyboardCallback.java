package game.petruccio.bricksgame;

/**
 * Interface for connecting buttons to the main game activity.
 */
public interface KeyboardCallback {
    void pressedLeft();
    void pressedRight();
    void pressedDown();
    void pressedRotate();
}
