package game.petruccio.bricksgame;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The class represents main game logic.
 * Drives tetromino, counts lines, scores etc.
 * Game thread is implemented in the method 'run'
 */
public class TGlass extends Fragment implements Runnable, View.OnClickListener {
    private final int CELLS_NUM = 4;        // number of cells in figure
    private final int GLASS_WIDTH = 336;    // Keep proportions for 12x21 cells
    private final int GLASS_HEIGHT = 588;   // see above (GLASS_WIDTH / GLASS_HEIGHT should be as 12 / 21)
    private final float CELLS_FOR_NEXT = 5.1f;  // number of cells for the widget for next figure output
    // Game settings
    private final int MAX_LEVEL = 10;
    private final int LINES_PER_LEVEL = 20;
    private final int PAUSE_ITERATIONS = 14;
    private final int TIME_SLEEP = 50;
    private final int DROP_PAUSE = 2;
    // Game sounds
    private SoundPool beep;
    private float VOLUME = 0.15f;
    private int SND_MAX_STREAMS = 2;
    private int SND_COLLIDE;
    private int SND_MOVE;
    private int SND_LINE_ERASE;
    // Scores values
    private final int S_SINGLE = 100;
    private final int S_DOUBLE = 300;
    private final int S_TRIPPLE = 700;
    private final int S_TETRIS = 1500;
    // Game data
    private int scores;
    private int lines;
    private int level;
    private boolean isDropping;
    private boolean isPaused;
    private boolean isGameOver;
    // Game thread
    private Thread game = null;
    // Graphic output widgets
    private DrawView glass;
    private DrawNextFigure nextFigure;
    private LinearLayout layoutGl, layoutNext;
    private TextView btnStart;
    private TextView labelScore, labelLine, labelLevel;
    private TetrisGameListener parent = null;   // parent activity
    private Tetromino figure = new Tetromino(); // tetromino-figure

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View result = inflater.inflate(R.layout.tetris_glass, container, false);
        // Main matrix drawing widget
        layoutGl = result.findViewById(R.id.layoutGl);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GLASS_WIDTH,GLASS_HEIGHT);
        glass = new DrawView(getActivity(), GLASS_WIDTH, GLASS_HEIGHT);
        glass.setFigure(figure);
        layoutGl.addView(glass, 0, params);
        // Next figure drawing widget
        figure.createNextTetromino();
        layoutNext = result.findViewById(R.id.nextFigureLayout);
        params = new LinearLayout.LayoutParams((int)(CELLS_FOR_NEXT*glass.getCellSize()),
                                                (int)(CELLS_FOR_NEXT*glass.getCellSize())); // keeps margins
        nextFigure = new DrawNextFigure(getActivity(), glass.getCellSize());
        nextFigure.setNextFigure(figure);
        layoutNext.addView(nextFigure, params);
        // Buttons
        btnStart = (TextView) result.findViewById(R.id.buttonStart);
        labelScore = (TextView)result.findViewById(R.id.txtScores);
        labelLine = (TextView)result.findViewById(R.id.txtLines);
        labelLevel = (TextView)result.findViewById(R.id.txtLevel);
        btnStart.setOnClickListener(this);
        glass.setListener(this);

        if(savedInstance!=null)
            loadInstanceState(savedInstance);

        if(isPaused){
            glass.setIsPaused(true);
            glass.postInvalidate();
        }
        else{
            glass.setIsPaused(false);
        }

        if(isGameOver)
            onGameOver();
        // Sounds
        // for API 21 or higher
//        beep = new SoundPool.Builder()
//                .setMaxStreams(SND_MAX_STREAMS)
//                .build();
        // for API lower than 21
        beep = new SoundPool(SND_MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try{
            SND_COLLIDE = beep.load(getActivity().getAssets().openFd("sfx_sounds_impact3.wav"), 1);
            SND_MOVE = beep.load(getActivity().getAssets().openFd("sfx_movement_ladder3a.wav"), 1);
            SND_LINE_ERASE = beep.load(getActivity().getAssets().openFd("sfx_movement_ladder3a.wav"), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Main game activity.
     * @param c - parent activity
     */
    public void setListener(TetrisGameListener c){
        parent = c;
    }

    public void continueGame(){
        if(isPaused)
            return;
        else
            startGame();
    }

    public void startGame(){
        game = new Thread(this);
        if(!isPaused){
            scores = 0;
            lines = 0;
            level = 0;
            isDropping = false;
        }
        btnStart.setText(R.string.strVoid);
        glass.setIsGameOver(false);
        glass.setIsPaused(false);
        game.start();
    }

    /**
     * Inmplements game thread.
     * Makes tetromino to move down step by step.
     * Thread is completed if a new tetromino has no place to move.
     */
    public void run(){
        isPaused = false;
        isGameOver = false;
        while(!figure.getIsGameOver()){ // do while there is space to move a new tetromino
                if (isDropping){        // If drop figure has been pressed
                    while (!figure.getIsSettled()){ // do until colliding occures
                        try {
                            Thread.sleep(TIME_SLEEP*DROP_PAUSE);
                        } catch (InterruptedException e) {
                            return;
                        }
                        figure.moveDown();
                        glass.postInvalidate();
                    }
                    //playCollide();  // sound
                    isDropping = false;
                    figure.createNew();
                    beep.play(SND_COLLIDE, VOLUME, VOLUME, 0, 0,0);
                    eliminateLines();
                    nextFigure.postInvalidate();
                    continue;
                }
                // Pause between two positions
                try {
                    for (int iteration=1; iteration<PAUSE_ITERATIONS - level; iteration++){
                        Thread.sleep(TIME_SLEEP);
                        if (isDropping)
                            break;   // if drop down has been pressed - go to dropping (loop beginning)
                    }
                } catch (InterruptedException e) {
                    return;
                }
                if(isDropping)  // drop if down has been pressed (go to loop beginning)
                    continue;
                figure.moveDown();
                if (figure.getIsSettled()){ // colliding
                    //playCollide();  // sound
                    figure.createNew();
                    beep.play(SND_COLLIDE, VOLUME, VOLUME, 0, 0,0);
                    eliminateLines();
                    nextFigure.postInvalidate();
                }
                glass.postInvalidate();
        }
        getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parent.onGameOver();
                }
            }); // call in main UI thread to be able to change widgets
        isGameOver = true;
        glass.setIsGameOver(true);
    }

    /**
     * Checks filled lines.
     * Eliminates them, counts scores and switch levels.
     */
    private void eliminateLines(){
        int lineCounter = 0;
        // Get matrix
        int bricksNet[][] = GlassNetArray.getInstance().getArray();
        int cols = GlassNetArray.getInstance().getWidth();
        int rows = GlassNetArray.getInstance().getHeight();
        // Check lines
        for (int i=rows-1; i>0; i--){   // going up from the bottom of the matrix
            boolean hasLine = true;
            for(int j=0; j<cols; j++){
                if (bricksNet[j][i]==0)
                    hasLine = false;
            }
            if (!hasLine)
                continue;
            for(int x = i; x>0; x--){   // move lines from upper to bottom
                for(int j=0; j<cols; j++){
                    bricksNet[j][x] = bricksNet[j][x-1];
                }
            }
            beep.play(SND_LINE_ERASE, VOLUME, VOLUME, 0, 0,0);
            try {   // pause before disappearing
                Thread.sleep(250);
            } catch(InterruptedException e) {
                // do nothing
            }
            glass.postInvalidate();
            lineCounter++;
            i++;
        }
        lines += lineCounter;
        if(lineCounter>0)
            calcScores(lineCounter); // calculate and refresh scores
        // Switch level if needs
        level = lines/LINES_PER_LEVEL;
        if (level>MAX_LEVEL)
            level = MAX_LEVEL;
        // Output scores and lines
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                labelScore.setText(Integer.toString(scores));
                labelLine.setText(Integer.toString(lines));
                labelLevel.setText(Integer.toString(level));
            }
        });
    }

    public void moveLeft(){
        if((game!=null) && (!game.isAlive()))
            return;
        beep.play(SND_MOVE, VOLUME, VOLUME, 0, 0,0);
        figure.moveLeft();
        glass.invalidate();
    }

    public void moveRight(){
        if((game!=null) && (!game.isAlive()))
            return;
        beep.play(SND_MOVE, VOLUME, VOLUME, 0, 0,0);
        figure.moveRight();
        glass.invalidate();
    }

    public void rotate(){
        if((game!=null) && (!game.isAlive()))
            return;
        beep.play(SND_MOVE, VOLUME, VOLUME, 0, 0,0);
        figure.rotate();
        glass.invalidate();
    }

    public void moveDown(){
        if((game!=null) && (!game.isAlive()))
            return;
        beep.play(SND_MOVE, VOLUME, VOLUME, 0, 0,0);
        isDropping = true;
    }

    @Override
    public void onClick(View v) {
        if(v==btnStart){
            // Restart game after game over
            if(isPaused)
                return;
            isGameOver = false;
            figure.refreshState();
            GlassNetArray.getInstance().clear();
            glass.postInvalidate();
            startGame();
        }
        // Pause if the glass has been clicked
        if (v==glass){
            if(!isPaused){
                isPaused = true;
                glass.setIsPaused(true);
                if((game!=null) && (game.isAlive()))
                    game.interrupt();
                glass.postInvalidate();
            } else {
                glass.postInvalidate();
                startGame();
            }
        }
    }

    public void calcScores(int lineNumber) {
        switch (lineNumber){
            case 1:
                scores += S_SINGLE;
                break;
            case 2:
                scores += S_DOUBLE;
                break;
            case 3:
                scores += S_TRIPPLE;
                break;
            case 4:
                scores += S_TETRIS;
                break;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        isPaused = true;
        glass.setIsPaused(true);
        if((game!=null) && (game.isAlive()))
            game.interrupt();
    }

    public void  onGameOver(){
        btnStart.setText(R.string.strRestart);
        glass.setIsGameOver(true);
    }

    /**
     * Save current game state:
     * Current and next figures coordinates, colours and gravity points, colours, and game flags.
     * @param saveState - bundle to save
     */
    @Override
    public void onSaveInstanceState(Bundle saveState){
        super.onSaveInstanceState(saveState);
        saveState.putInt("scores", scores);
        saveState.putInt("lines", lines);
        saveState.putInt("level", level);
        saveState.putBoolean("isDropping", isDropping);
        saveState.putBoolean("isPaused", isPaused);
        saveState.putBoolean("isGameOver", isGameOver);
        // Save figures coordinates
        Point[] coord = new Point[CELLS_NUM];
        for (int i=0; i<CELLS_NUM; i++)
            coord[i] = new Point();
        coord = figure.getCoordinates();
        for(int i=0; i<CELLS_NUM; i++){
            saveState.putInt("currentPointX" + i, coord[i].x);
            saveState.putInt("currentPointY" + i, coord[i].y);
        }
        coord = figure.getNextFigureCoordinates();
        for(int i=0; i<CELLS_NUM; i++){
            saveState.putInt("nextPointX" + i, coord[i].x);
            saveState.putInt("nextPointY" + i, coord[i].y);
        }
        // Save gravity points
        saveState.putInt("gravThis", figure.getFigureGravity());
        saveState.putInt("gravNext", figure.getNextFigureGravity());
        // Save figures colours
        saveState.putInt("colourFig", figure.getFigureColour());
        saveState.putInt("colourNext", figure.getNextFigureColor());
    }

    /**
     * Restore game state after reloading.
     * @param savedInstance - bundle to read.
     */
    public void loadInstanceState(Bundle savedInstance){
        scores = savedInstance.getInt("scores");
        lines = savedInstance.getInt("lines");
        level = savedInstance.getInt("level");
        isDropping = savedInstance.getBoolean("isDropping");
        isPaused = savedInstance.getBoolean("isPaused");
        isGameOver = savedInstance.getBoolean("isGameOver");
        // Bricks
        // Coordinates
        Point[] coord = new Point[CELLS_NUM];
        for (int i=0; i<CELLS_NUM; i++)
            coord[i] = new Point();
        for(int i=0; i<CELLS_NUM; i++){
            coord[i].x = savedInstance.getInt("currentPointX" + i);
            coord[i].y = savedInstance.getInt("currentPointY" + i);
        }
        figure.setFigureCoord(coord);
        coord = figure.getNextFigureCoordinates();
        for(int i=0; i<CELLS_NUM; i++){
            coord[i].x = savedInstance.getInt("nextPointX" + i);
            coord[i].y = savedInstance.getInt("nextPointY" + i);
        }
        figure.setNextFigureCoord(coord);
        // Colours
        figure.setFigureColour(savedInstance.getInt("colourFig"));
        figure.setNextFigureColour(savedInstance.getInt("colourNext"));
        // Gravity points
        figure.setFigureGravity(savedInstance.getInt("gravThis"));
        figure.setNextFigureGravity(savedInstance.getInt("gravNext"));
        // Refresh labels
        labelScore.setText(Integer.toString(scores));
        labelLine.setText(Integer.toString(lines));
        labelLevel.setText(Integer.toString(level));
    }
}
