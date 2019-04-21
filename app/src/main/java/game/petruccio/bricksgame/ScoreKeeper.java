package game.petruccio.bricksgame;

import android.content.Context;
import java.io.*;

/**
 * A singleton that keeps scores and drives read/write scores operations.
 */
public class ScoreKeeper {
    private static ScoreKeeper instance = null;
    private Context context = null;
    private final int TABLE_SIZE = 9;
    private final String DEFAULT_NAME = "PLAYER";
    private final String FILE_NAME = "scores";
    private int[] scores = new int[TABLE_SIZE];         // scores values
    private String[] names = new String[TABLE_SIZE];    // name values

    private ScoreKeeper(){
        for(int i=0; i<TABLE_SIZE; i++)
            names[i] = DEFAULT_NAME + Integer.toString(i);
    }

    public static ScoreKeeper getInstance(){
        if(instance==null)
            instance = new ScoreKeeper();
        return instance;
    }

    public void setContext(Context c){
        context = c;
        // File opening / creating for score table
        try{
            if(context!=null){
                FileInputStream fos = context.openFileInput(FILE_NAME);
                if (fos!=null){
                    DataInputStream data = new DataInputStream(fos);
                    for(int i=0; i<TABLE_SIZE; i++){
                        names[i] = data.readUTF();
                        scores[i] = data.readInt();
                    }
                } else {
                    // Create and write file if does not exist
                    FileOutputStream fis = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
                    DataOutputStream out = new DataOutputStream(fis);
                    for(int i=0; i<TABLE_SIZE; i++){
                        out.writeUTF(names[i]);
                        out.writeInt(scores[i]);
                    }
                }
            }
        } catch (Exception e) {
            // cannot handle data file
            // nothing to do: table will be filled with default values
        }
    }

    /**
     * Checks if the scores are to be added in the scores table.
     * @param newScores - scores value to check.
     * @return - add scores to table or not.
     */
    public boolean isNewRecord(int newScores){
        for(int i=0; i<TABLE_SIZE; i++){
            if(scores[i]<newScores)
                return true;
        }
        return false;
    }

    /**
     * Inserts player score results in a table.
     * Saves the table.
     * @param name - player name
     * @param value - player scores
     */
    public void insertScores(String name, int value){
        for(int i=TABLE_SIZE-1; i>=0; i--){
            if((i==0) || (value<=scores[i-1])){
                scores[i] = value;
                names[i] = name;
                break;
            }
            scores[i] = scores[i-1];
            names[i] = names[i-1];
        }
        // Write table
        try {
            FileOutputStream fis = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
            DataOutputStream out = new DataOutputStream(fis);
            for(int i=0; i<TABLE_SIZE; i++) {
                out.writeUTF(names[i]);
                out.writeInt(scores[i]);
            }
        } catch (Exception e) {
            // do nothing: no file - no record
        }
    }

    public int getTableSize(){
        return TABLE_SIZE;
    }

    public int[] getScores(){
        return scores;
    }

    public String[] getNames(){
        return names;
    }
}
