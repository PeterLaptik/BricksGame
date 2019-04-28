package game.petruccio.bricksgame;

import android.graphics.Color;

/**
 * The class describes a tetromino-figure.
 * The tetromino can move left, right, down and rotate.
 * If a next figure has to be known the current figure can contain a child tetromino that describes a next figure.
 */
public class Tetromino {
    private  final int CELLS_NUM = 4;   // number of cells
    private final int NO_ROTATION = -1; // gravity-point for non-rotational figure (O-brick)
    private final int FORM_NM = 7;      // number of shapes
    // Shape types:
    private final int FORM_O = 0;
    private final int FORM_I = 1;
    private final int FORM_J = 2;
    private final int FORM_L = 3;
    private final int FORM_S = 4;
    private final int FORM_T = 5;
    private final int FORM_Z = 6;
    // Get matrix (GlassNetArray singleton)
    private int GLASS_X = GlassNetArray.getInstance().getWidth();
    private int GLASS_Y = GlassNetArray.getInstance().getHeight();
    private int glass[][] = GlassNetArray.getInstance().getArray();
    private Tetromino nextFigure = null;    // next figure
    private Point[] figurePoints = new Point[CELLS_NUM];    // figure bricks coordinates
    private Point[] nextState = new Point[CELLS_NUM];       // temporary figure bricks coordinates
    private int colour;                 // colour of current figure
    private int gravityPoint;           // pivot point for current figure rotation
    private boolean isSettled = false;  // shows whether the brick is settled on a heap / a case of colliding
    private boolean isGameOver = false; // true - if the matrix is full

    public Tetromino(){
        // Current figure coordinates
        for(int i=0; i<CELLS_NUM; i++){
            figurePoints[i] = new Point();
        }
        // Temporary figure coordinates
        // The temporary figure is used to check next figure position after rotation
        for(int i=0; i<CELLS_NUM; i++){
            nextState[i] = new Point();
        }
        this.createNew();
    }

    /**
     * Creates a child tetromino.
     * The child tetromino is used as a next figure generator and container.
     */
    public void createNextTetromino(){
        if (nextFigure==null)
            nextFigure = new Tetromino();
    }

    public void refreshState(){
        isGameOver = false;
    }

    public boolean moveLeft(){
        for (int i=0; i<CELLS_NUM; i++){
            if(figurePoints[i].y>=0)   // avoid crashes during rotation at the top of the matrix
                if ((figurePoints[i].x-1<0)||(glass[figurePoints[i].x-1][figurePoints[i].y]!=0))
                    return false;
        }
        for (int i=0; i<CELLS_NUM; i++){
            figurePoints[i].x -= 1;
        }
        return true;
    }

    public boolean moveRight(){
        for (int i=0; i<CELLS_NUM; i++){
            if(figurePoints[i].y>=0)   // avoid crashes during rotation at the top of the matrix
                if ((figurePoints[i].x+1>GLASS_X-1)||(glass[figurePoints[i].x+1][figurePoints[i].y]!=0))
                    return false;
        }
        for (int i=0; i<CELLS_NUM; i++){
            figurePoints[i].x += 1;
        }
        return true;
    }

    /**
     * Move figure one step down.
     * If figure bricks collide with occupied cell then flag 'isSettled' changes to true value.
     */
    public void moveDown(){
        for (int i=0; i<CELLS_NUM; i++){
            if ((figurePoints[i].y+1>GLASS_Y-1)||(glass[figurePoints[i].x][figurePoints[i].y+1]!=0)) {  // collides
                isSettled = true;
                // Fill settled place
                for(int j=0; j<CELLS_NUM; j++){
                    if(figurePoints[j].y<0)   // avoid crashes during rotation at the top of the matrix
                        continue;
                    glass[figurePoints[j].x][figurePoints[j].y] = colour;
                }
                return; // do not move if the cells are not free
            }
        }
        // Move down
        for (int i=0; i<CELLS_NUM; i++){
            figurePoints[i].y += 1;
        }
    }

    /**
     * Rotates figure in counter-clockwise direction.
     * Creates temporary brick-coordinates array to check for collides and intersections in rotated state.
     * If intersection checks are OK, inserts temporary coordinates values into current figure coordinates
     */
    public boolean rotate(){
        if (gravityPoint==NO_ROTATION)
            return false; // no rotation for for O-brick
        // Rotation of the temporary figure:
        // x2 = px + py - y1 - q
        // y2 = x1 + py - px
        // where    x1, y1 - initial coordinates
        //          px, py - pivot point coordinates
        //          q - brick width and height
        for (int i=0; i<CELLS_NUM; i++){
            nextState[i].x = figurePoints[gravityPoint].x + figurePoints[gravityPoint].y - figurePoints[i].y;
            nextState[i].y = figurePoints[i].x + figurePoints[gravityPoint].y - figurePoints[gravityPoint].x;
        }
        // Borders intersection check
        int incrementValue = 0; // value to align by vertical borders
        // Value of the left border intersection
        for (int i=0; i<CELLS_NUM; i++){
            if (nextState[i].x<0) {
                if(incrementValue<0-nextState[i].x)
                    incrementValue = 0-nextState[i].x;
            }
        }
        // Value of the right border intersection
        for (int i=0; i<CELLS_NUM; i++){
            if (nextState[i].x>GLASS_X-1) {
                if(incrementValue>GLASS_X - 1 - nextState[i].x)
                    incrementValue = GLASS_X - 1 - nextState[i].x;
            }
        }
        // Shift figure if intersects borders
        if(incrementValue!=0){
            for (int i=0; i<CELLS_NUM; i++){
                nextState[i].x += incrementValue;
            }
        }
        // Check for void cells for the rotated state
        for (int i=0; i<CELLS_NUM; i++){
            if(nextState[i].y<0)   // avoid crashes during rotation at the top of the matrix
                continue;
            if (nextState[i].y>GLASS_Y-1)
                return false;
            if (glass[nextState[i].x][nextState[i].y]!=0) {
                // Cannot be rotated
                return false;
            }
        }
        // If OK - exchange the coordinates
        for (int i=0; i<CELLS_NUM; i++){
            figurePoints[i].x = nextState[i].x;
            figurePoints[i].y = nextState[i].y;
        }
        return true;
    }

    /**
     * Creates new figure.
     * If the current tetromino has a child then the coordinats and colour for the figure
     * are being taken from the child. The same method is called for the child.
     */
    public void createNew(){
        // Take bricks from a child tetromino if exists
        if(nextFigure!=null){
            for(int i=0; i<CELLS_NUM; i++){
                this.figurePoints[i].x = nextFigure.figurePoints[i].x;
                this.figurePoints[i].y = nextFigure.figurePoints[i].y;
            }
            this.colour = nextFigure.colour;
            this.gravityPoint = nextFigure.gravityPoint;
            nextFigure.createNew(); // create new next figure
            checkForGameOver();     // is there enough place for the new figure?
            isSettled = false;
            return;
        }
        // Create random colour and figure
        setFigureColor();
        // Set brick type
        int type = (int)(Math.random()*FORM_NM);
        switch (type){
            case FORM_O:
                figurePoints[0].x = 0; figurePoints[0].y = 0;
                figurePoints[1].x = 1; figurePoints[1].y = 1;
                figurePoints[2].x = 0; figurePoints[2].y = 1;
                figurePoints[3].x = 1; figurePoints[3].y = 0;
                gravityPoint = NO_ROTATION;
                break;
            case FORM_I:
                figurePoints[0].x = 0; figurePoints[0].y = 0;
                figurePoints[1].x = 1; figurePoints[1].y = 0;
                figurePoints[2].x = 2; figurePoints[2].y = 0;
                figurePoints[3].x = 3; figurePoints[3].y = 0;
                gravityPoint = 1;
                break;
            case FORM_J:
                figurePoints[0].x = 0; figurePoints[0].y = 0;
                figurePoints[1].x = 0; figurePoints[1].y = 1;
                figurePoints[2].x = 1; figurePoints[2].y = 1;
                figurePoints[3].x = 2; figurePoints[3].y = 1;
                gravityPoint = 2;
                break;
            case FORM_L:
                figurePoints[0].x = 0; figurePoints[0].y = 1;
                figurePoints[1].x = 1; figurePoints[1].y = 1;
                figurePoints[2].x = 2; figurePoints[2].y = 1;
                figurePoints[3].x = 2; figurePoints[3].y = 0;
                gravityPoint = 1;
                break;
            case FORM_S:
                figurePoints[0].x = 1; figurePoints[0].y = 0;
                figurePoints[1].x = 2; figurePoints[1].y = 0;
                figurePoints[2].x = 0; figurePoints[2].y = 1;
                figurePoints[3].x = 1; figurePoints[3].y = 1;
                gravityPoint = 0;
                break;
            case FORM_T:
                figurePoints[0].x = 0; figurePoints[0].y = 1;
                figurePoints[1].x = 1; figurePoints[1].y = 1;
                figurePoints[2].x = 2; figurePoints[2].y = 1;
                figurePoints[3].x = 1; figurePoints[3].y = 0;
                gravityPoint = 1;
                break;
            case FORM_Z:
                figurePoints[0].x = 0; figurePoints[0].y = 0;
                figurePoints[1].x = 1; figurePoints[1].y = 0;
                figurePoints[2].x = 1; figurePoints[2].y = 1;
                figurePoints[3].x = 2; figurePoints[3].y = 1;
                gravityPoint = 1;
                break;
        }
        centerFigure();
        checkForGameOver(); // is there enough place for the new figure?
        isSettled = false;
    }

    /**
     * Random colour for the new figure.
     */
    private void setFigureColor(){
        // Set brick color
        int color = (int)(Math.random()*7);
        switch(color){
            case 0:
                colour = Color.WHITE;
                break;
            case 1:
                colour = Color.RED;
                break;
            case 2:
                colour = Color.GREEN;
                break;
            case 3:
                colour = Color.BLUE;
                break;
            case 4:
                colour = Color.YELLOW;
                break;
            case 5:
                colour = Color.CYAN;
                break;
            case 6:
                colour = Color.MAGENTA;
                break;
        }
    }

    /**
     * Checks a free space for the new figure.
     */
    private void checkForGameOver(){
        for (int i=0; i<CELLS_NUM; i++){
            if(figurePoints[i].y>=0)   // avoid crashes during rotation at the top of the matrix
                if((glass[figurePoints[i].x][figurePoints[i].y+1]!=0)||(glass[figurePoints[i].x][figurePoints[i].y]!=0)){
                    isGameOver = true;
                }
        }
    }

    /**
     * Change figure coordinates to settle it in the center of the matrix X-axis.
     */
    private void centerFigure(){
        for (int i=0; i<CELLS_NUM; i++){
            figurePoints[i].x += (GLASS_X-1)/2;
        }
    }

    /**
     * Getters:
     */
    public int getFigureColour(){
        return colour;
    }

    public Point[] getCoordinates(){
        return figurePoints;
    }

    public Point[] getNextFigureCoordinates(){
        if(nextFigure!=null)
            return nextFigure.figurePoints;
        return this.figurePoints;
    }

    public int getNextFigureColor(){
        if(nextFigure!=null)
            return nextFigure.colour;
        return Color.WHITE;
    }

    public boolean getIsSettled(){
        return isSettled;
    }

    public boolean getIsGameOver(){
        return isGameOver;
    }

    public int getFigureGravity(){
        return this.gravityPoint;
    }

    public int getNextFigureGravity(){
        if(nextFigure!=null)
            return nextFigure.gravityPoint;
        return this.gravityPoint;
    }

    /**
     * Setters:
     * current and next figure properties can be changed from outside of the class.
     */
    public void setFigureColour(int clr){
        this.colour = clr;
    }

    public void setNextFigureColour(int clr){
        if(nextFigure!=null)
            nextFigure.colour = clr;
    }

    public void setFigureCoord(Point[] coord){
        for(int i=0; i<CELLS_NUM; i++){
            this.figurePoints[i].x = coord[i].x;
            this.figurePoints[i].y = coord[i].y;
        }
    }

    public void setNextFigureCoord(Point[] coord){
        if(nextFigure!=null)
            for(int i=0; i<CELLS_NUM; i++){
                nextFigure.figurePoints[i].x = coord[i].x;
                nextFigure.figurePoints[i].y = coord[i].y;
            }
    }

    void setFigureGravity(int i){
        this.gravityPoint = i;
    }

    void setNextFigureGravity(int i){
        nextFigure.gravityPoint = i;
    }
}
