package game.petruccio.bricksgame;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;

/**
 * View for output next figure.
 */
public class DrawNextFigure extends View {
    // Colours
    private final int COLOUR_NETWORK = 0xFF151515;
    private final int COLOUR_BORDERS = Color.GREEN;
    // Measures
    private final int CELLS_NUM = 4;            // number of bricks in figure
    private final int NET_SIZE = 4;             // cells number for the view
    private final int BORDER_MARGIN = 2;        // margin from the right and bottom borders (for excluding hidden lines)
    private final int THCK_BRICK = 2;           // brick outer line thickness
    private final float BRICK_MARGIN = 0.2f;    // margin (rate) from outer brick to inner brick (free space)
    private int cellSize;   // brick size
    private int width;      // Widget -
    private int height;     // dimensions
    private Paint paint;
    private Tetromino nextFigure;
    private Point[] point;  // bricks coordinates of the next tetromino

    public DrawNextFigure(Context context, int unitSize){
        super(context);
        paint = new Paint();
        cellSize = unitSize;
        width = NET_SIZE*cellSize;
        height = NET_SIZE*cellSize;
    }

    /**
     * Set next figure to show.
     * @param next - next tetromino.
     */
    public void setNextFigure(Tetromino next){
        nextFigure = next;
    }

    @Override
    public void onDraw(Canvas canvas){
        paint.setColor(COLOUR_NETWORK);
        // Matrix network
//        for(int i=0; i<=CELLS_NUM+2; i++){
//            canvas.drawLine(0 + i*cellSize,0, 0 + i*cellSize, height - BORDER_MARGIN, paint);
//            canvas.drawLine(0,0 + i*cellSize, width - BORDER_MARGIN, 0 + i*cellSize, paint);
//        }
        paint.setColor(COLOUR_BORDERS);
        // Matrix borders
//        canvas.drawLine(0 ,0, width, 0, paint);
//        canvas.drawLine(0 ,height, width, height, paint);
//        canvas.drawLine(0 ,0, 0, height, paint);
//        canvas.drawLine(width ,0, width, height, paint);
        // Next figure output
        if(nextFigure==null)
            return;
        paint.setColor(nextFigure.getNextFigureColor());
        point = nextFigure.getNextFigureCoordinates();
        // Move figure from center to zero-point
        int shiftToLeft = point[0].x;
        for (int i=1; i<CELLS_NUM; i++){
            if (shiftToLeft>point[i].x)
                shiftToLeft = point[i].x;
        }
        // Figure align in view
        int figureWidth = 0;
        int figureHeight = 0;
        for (int i=0; i<CELLS_NUM; i++){
            if(point[i].x-shiftToLeft>figureWidth)
                figureWidth = point[i].x-shiftToLeft;
            if(point[i].y>figureHeight)
                figureHeight = point[i].y;
        }
        int shiftX = (NET_SIZE - figureWidth)/2;
        int shiftY = (NET_SIZE - figureHeight)/2;
        // Draw bricks
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(THCK_BRICK);
        // Outer shapes of bricks
        for(int i=0; i<CELLS_NUM; i++){
            canvas.drawRect((point[i].x-shiftToLeft+shiftX)*cellSize, (point[i].y+shiftY)*cellSize,
                    ((point[i].x+1+shiftX)-shiftToLeft)*cellSize, (point[i].y+1+shiftY)*cellSize, paint);
        }
        // Inner filled bricks
        paint.setStyle(Paint.Style.FILL);
        for(int i=0; i<CELLS_NUM; i++){
            canvas.drawRect((point[i].x-shiftToLeft+shiftX)*cellSize + (int)(cellSize*BRICK_MARGIN),
                            (point[i].y+shiftY)*cellSize + (int)(cellSize*BRICK_MARGIN),
                        ((point[i].x+1+shiftX)-shiftToLeft)*cellSize - (int)(cellSize*BRICK_MARGIN),
                        (point[i].y+1+shiftY)*cellSize - (int)(cellSize*BRICK_MARGIN), paint);
        }
    }
}