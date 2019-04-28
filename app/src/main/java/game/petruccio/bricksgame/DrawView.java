package game.petruccio.bricksgame;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Widget to draw full matrix with bricks and current figure.
 */
public class DrawView extends View {
    // Colours
    private final int COLOUR_BACKGROUND = Color.BLACK;
    private final int COLOUR_BORDERS = Color.GREEN;
    private final int COLOUR_NETWORK = 0x15FFFFFF;
    private final int FONT_COLOUR = Color.WHITE;
    // Shapes properties
    private int CELL_SIZE = 20;                 // default brick size
    private final int THCK_GLASS = 4;
    private final int THCK_BRICK = 2;
    private final int THCK_NET = 1;
    private final int ZERO_POINT = 0;           // zero coordinate
    private final int BORDER_SHIFT = 1;         // matrix borders margin in brick units
    private final float BRICK_MARGIN = 0.2f;    // margin to inner brick from outer frame
    private final int FONT_SIZE = 48;
    private Typeface spectrumFont;
    // Game state
    private boolean isPaused;
    private boolean isGameOver;
    private int height, width;
    private Paint paint, fontPaint;
    private Point point[];      // figure bricks coordinates
    private Tetromino currentFigure = null;
    // Glass array values
    private int GLASS_X = GlassNetArray.getInstance().getWidth();
    private int GLASS_Y = GlassNetArray.getInstance().getHeight();
    private int glass[][] = GlassNetArray.getInstance().getArray();

    public DrawView(Context context, int width, int height){
        super(context);
        this.height = height;
        this.width = width;
        CELL_SIZE = width/(GLASS_X+2*BORDER_SHIFT);
        paint = new Paint();
        spectrumFont = ResourcesCompat.getFont(context, R.font.zxspectrum);
        fontPaint = new Paint();
        fontPaint.setTextSize(FONT_SIZE);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setColor(FONT_COLOUR);
        fontPaint.setTypeface(spectrumFont);
    }

    /**
     * Set link to tetromino that has to be drawn.
     * @param fig - figure
     */
    public void setFigure(Tetromino fig){
        currentFigure = fig;
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawColor(COLOUR_BACKGROUND);
//        paint.setColor(COLOUR_NETWORK);
        paint.setColor(GameSettings.getInstance().getColour());
        // Matrix network lines
        paint.setStrokeWidth(THCK_NET);
        for (int i=1 + BORDER_SHIFT; i<=GLASS_X; i++){
            canvas.drawLine(ZERO_POINT + CELL_SIZE*i, ZERO_POINT,
                    ZERO_POINT + CELL_SIZE*i, height - BORDER_SHIFT*CELL_SIZE, paint);
        }

        for (int i=0; i<GLASS_Y; i++){
            canvas.drawLine(ZERO_POINT + BORDER_SHIFT*CELL_SIZE, ZERO_POINT + CELL_SIZE*i,
                    width - BORDER_SHIFT*CELL_SIZE, ZERO_POINT + CELL_SIZE*i, paint);
        }
        // Refresh all elements
        refreshElements(canvas);
        // Drawing matrix
        // outer lines
        paint.setStrokeWidth(THCK_GLASS);
        paint.setColor(COLOUR_BORDERS);
        canvas.drawLine(ZERO_POINT, ZERO_POINT, ZERO_POINT, height, paint);
        canvas.drawLine(ZERO_POINT, height-BORDER_SHIFT, width, height-BORDER_SHIFT, paint);
        canvas.drawLine(width-BORDER_SHIFT, ZERO_POINT, width-BORDER_SHIFT, height, paint);
        // upper lines
        canvas.drawLine(ZERO_POINT, ZERO_POINT, ZERO_POINT + BORDER_SHIFT*CELL_SIZE, ZERO_POINT, paint);
        canvas.drawLine(width, BORDER_SHIFT, width - BORDER_SHIFT*CELL_SIZE, BORDER_SHIFT, paint);
        // inner lines
        canvas.drawLine(ZERO_POINT+CELL_SIZE, ZERO_POINT, ZERO_POINT+CELL_SIZE, height-CELL_SIZE, paint);
        canvas.drawLine(ZERO_POINT+CELL_SIZE, height-CELL_SIZE, width-CELL_SIZE, height-CELL_SIZE, paint);
        canvas.drawLine(width-CELL_SIZE, ZERO_POINT, width-CELL_SIZE, height-CELL_SIZE, paint);
        // Show game state
        if((isPaused) && (!isGameOver))
            drawPause(canvas);
        if(isGameOver)
            drawGameOver(canvas);
    }

    /**
     * Draw all bricks and current figure.
     * @param canvas - canvas for drawing.
     */
    private void refreshElements(Canvas canvas){
        // Bricks drawing (matrix)
        paint.setStrokeWidth(THCK_BRICK);
        for (int i=0; i<GLASS_X; i++){
            for(int j=0; j<GLASS_Y; j++){
                if (glass[i][j]!=0){
                    paint.setColor(glass[i][j]);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect((i+BORDER_SHIFT)*CELL_SIZE,
                                    j*CELL_SIZE,
                                    (i+BORDER_SHIFT+1)*CELL_SIZE,
                                (j+1)*CELL_SIZE, paint);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect((i+BORDER_SHIFT)*CELL_SIZE + (int)(CELL_SIZE*BRICK_MARGIN),
                            j*CELL_SIZE + (int)(CELL_SIZE*BRICK_MARGIN),
                            (i+BORDER_SHIFT+1)*CELL_SIZE - (int)(CELL_SIZE*BRICK_MARGIN),
                            (j+1)*CELL_SIZE - (int)(CELL_SIZE*BRICK_MARGIN), paint);
                }
            }
        }
        // Current figure drawing
        if(currentFigure!=null){
            point = currentFigure.getCoordinates();
            paint.setColor(currentFigure.getFigureColour());
            for (int i=0; i<point.length; i++){
                if(point[i].y>=0) {
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect((point[i].x+BORDER_SHIFT)*CELL_SIZE,
                            point[i].y*CELL_SIZE,
                            (point[i].x+BORDER_SHIFT+1)*CELL_SIZE,
                            (point[i].y+1)*CELL_SIZE, paint);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect((point[i].x+BORDER_SHIFT)*CELL_SIZE + (int)(CELL_SIZE*BRICK_MARGIN),
                            point[i].y*CELL_SIZE + (int)(CELL_SIZE*BRICK_MARGIN),
                            (point[i].x+BORDER_SHIFT+1)*CELL_SIZE - (int)(CELL_SIZE*BRICK_MARGIN),
                            (point[i].y+1)*CELL_SIZE - (int)(CELL_SIZE*BRICK_MARGIN), paint);
                }
            }
        }
    }

    private void drawPause(Canvas canvas){
        canvas.drawText("PAUSE", (width-5*FONT_SIZE)/2, height/2+FONT_SIZE/2, fontPaint);
    }

    private void drawGameOver(Canvas canvas){
        canvas.drawText("GAME", (width-4*FONT_SIZE)/2, height/2-FONT_SIZE/4, fontPaint);
        canvas.drawText("OVER", (width-4*FONT_SIZE)/2, height/2+FONT_SIZE, fontPaint);
    }

    public int getCellSize(){
        return CELL_SIZE;
    }

    public void setListener(OnClickListener i){
        setOnClickListener(i);
    }

    /**
     * Game state setters:
     */
    public void setIsPaused(boolean pause){
        isPaused = pause;
    }

    public void setIsGameOver(boolean gameover){
        isGameOver = gameover;
    }
}