package game.petruccio.bricksgame;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Main activity that shows the game.
 * It shows matrix, next figure, buttons and game status.
 */
public class MainActivity extends AppCompatActivity implements KeyboardCallback, TetrisGameListener{
    TGlass fragGlass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check screen layout (horizontal or vertical)
        Configuration conf = getResources().getConfiguration();
        if(conf.orientation==Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);
        else
            setContentView(R.layout.activity_main_hor);
        // Restore matrix array if was saved
        if(savedInstanceState!=null){
            for(int i=0; i<GlassNetArray.getInstance().getWidth(); i++)
                for(int j=0; j<GlassNetArray.getInstance().getHeight(); j++)
                    GlassNetArray.getInstance().getArray()[i][j] =
                            savedInstanceState.getInt("line" + "[" + i + "," + j + "]", 0);
        }
        // Register callbacks
        if(conf.orientation==Configuration.ORIENTATION_PORTRAIT){
            ((TKeyboard)getSupportFragmentManager().findFragmentById(R.id.fragment)).setKeyboardListener(this);
            fragGlass = (TGlass) getSupportFragmentManager().findFragmentById(R.id.fragment3);
        } else {
            fragGlass = (TGlass) getSupportFragmentManager().findFragmentById(R.id.fragment3);
            ((TKeyboardLeft)getSupportFragmentManager().findFragmentById(R.id.fragmentKeyLeft)).setKeyboardListener(this);
            ((TKeyboardRight)getSupportFragmentManager().findFragmentById(R.id.fragmentKeyRight)).setKeyboardListener(this);
        }
        ScoreKeeper.getInstance().setContext(getApplicationContext());
        GameSettings.getInstance().setContext(getApplicationContext());
        fragGlass.setListener(this);
        fragGlass.continueGame();
    }

    @Override
    protected void onSaveInstanceState(Bundle stateToSave){
        super.onSaveInstanceState(stateToSave);
        // Save matrix array
        for(int i=0; i<GlassNetArray.getInstance().getWidth(); i++)
            for(int j=0; j<GlassNetArray.getInstance().getHeight(); j++)
                stateToSave.putInt("line" + "[" + i + "," + j + "]", GlassNetArray.getInstance().getArray()[i][j]);
    }

    @Override
    public void pressedLeft() {
        fragGlass.moveLeft();
    }

    @Override
    public void pressedRight() {
        fragGlass.moveRight();
    }

    @Override
    public void pressedDown() {
        fragGlass.moveDown();
    }

    @Override
    public void pressedRotate() {
        fragGlass.rotate();
    }

    @Override
    public void onGameOver() {
        fragGlass.onGameOver();
    }
}
