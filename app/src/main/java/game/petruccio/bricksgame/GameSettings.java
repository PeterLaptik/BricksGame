package game.petruccio.bricksgame;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GameSettings {
    private static GameSettings thisInstance = null;
    private final int DEFAULT_NET_COLOR = 15;
    private final String FILE_NAME = "settings";
    private boolean isMuted = false;
    private int colourInt = DEFAULT_NET_COLOR*((int)Math.pow(16, 6)) + 0xFFFFFF; // default value
    Context context;

    private GameSettings(){

    }

    public static GameSettings getInstance(){
        if(thisInstance==null){
            thisInstance = new GameSettings();
        }
        return thisInstance;
    }

    public void setContext(Context c){
        context = c;
        // File opening / creating for score table
        try{
            if(context!=null){
                FileInputStream fos = context.openFileInput(FILE_NAME);
                if (fos!=null){
                    DataInputStream data = new DataInputStream(fos);
                    colourInt = data.readInt();
                    isMuted = data.readBoolean();
                } else {
                    // Create and write file if does not exist
                    FileOutputStream fis = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
                    DataOutputStream out = new DataOutputStream(fis);
                    out.writeInt(colourInt);
                    out.writeBoolean(isMuted);
                }
            }
        } catch (Exception e) {
            // cannot handle settings file
            // nothing to do: table will be filled with default values
        }
    }

    public void saveSettings(){
        try{
            FileOutputStream fis = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
            DataOutputStream out = new DataOutputStream(fis);
            out.writeInt(colourInt);
            out.writeBoolean(isMuted);
        } catch (Exception e){
            // cannot handle settings file
        }
    }

    public void setColour(int c){
        colourInt = c*((int)Math.pow(16, 6)) + 0xFFFFFF;
    }

    public void setMuted(boolean isMuted){
        this.isMuted = isMuted;
    }

    public int getColour(){
        return colourInt;
    }

    public boolean getMuted(){
        return isMuted;
    }
}
