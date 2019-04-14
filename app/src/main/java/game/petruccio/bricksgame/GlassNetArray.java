package game.petruccio.bricksgame;

/**
 * A Singleton that keeps a matrix cells.
 * Each cell of the array 'gArray' matches to the matrix cell.
 * Cell zero-value means void cell, non-zero value means integer value of a brick colour.
 */
public class GlassNetArray {
    private static GlassNetArray thisInstance = null;
    private final int GLASS_X = 10;
    private final int GLASS_Y = 20;
    private int[][] gArray = new int[GLASS_X][GLASS_Y]; // glass cells state

    private GlassNetArray(){

    }

    public static GlassNetArray getInstance(){
        if(thisInstance==null) {
            thisInstance = new GlassNetArray();
        }
            return thisInstance;
    }

    public void clear(){
        for(int i=0; i<GLASS_X; i++){
            for(int j=0; j<GLASS_Y; j++){
                gArray[i][j] = 0;
            }
        }
    }

    public int[][] getArray(){
        return gArray;
    }

    public int getWidth(){
        return GLASS_X;
    }

    public int getHeight(){
        return GLASS_Y;
    }
}
