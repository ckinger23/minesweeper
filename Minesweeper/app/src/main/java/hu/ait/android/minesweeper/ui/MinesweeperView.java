package hu.ait.android.minesweeper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import hu.ait.android.minesweeper.MainActivity;
import hu.ait.android.minesweeper.R;
import hu.ait.android.minesweeper.model.MinesweeperModel;

import static hu.ait.android.minesweeper.model.MinesweeperModel.getNumFlags;
import static hu.ait.android.minesweeper.model.MinesweeperModel.numBombs;
import static hu.ait.android.minesweeper.model.MinesweeperModel.numRowCols;

public class MinesweeperView extends View {

    private Paint paintBackGround;
    private Paint paintLine;
    private Paint paintFont;
    private Bitmap bmBomb;
    private Bitmap bmFlag;
    private Bitmap bmTile;
    private Bitmap bgLightning;


    public MinesweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paintBackGround = new Paint();
        paintBackGround.setColor(Color.BLACK);
        paintBackGround.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintFont = new Paint();
        paintFont.setTextSize(50);
        paintFont.setColor(Color.RED);

        bmBomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        bmFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        bmTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile);
        bgLightning = BitmapFactory.decodeResource(getResources(), R.drawable.lightning);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bmBomb = Bitmap.createScaledBitmap(bmBomb, w / numRowCols, h / numRowCols, false); //rescale bitmap to block size
        bmFlag = Bitmap.createScaledBitmap(bmFlag, w / numRowCols, h / numRowCols, false);
        bmTile = Bitmap.createScaledBitmap(bmTile, w / numRowCols, h / numRowCols, false);
        bgLightning = Bitmap.createScaledBitmap(bgLightning, w, h, false); //possible background
        paintFont.setTextSize(getHeight() / numRowCols); //rescale number that is printed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGameArea(canvas);
        drawPlayers(canvas);
    }


    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < numRowCols; i++) {
            for (int j = 0; j < numRowCols; j++) {
                if (MinesweeperModel.getInstance().getFieldsContent(i, j).isWasTouched()) { //if field has been touched
                    if (MinesweeperModel.getInstance().getFieldsContent(i, j).isFlagged()) { //if flag has been placed
                        canvas.drawBitmap(bmFlag, i * getWidth() / numRowCols, j * getHeight() / numRowCols, null);
                    } else if (MinesweeperModel.getInstance().getFieldsContent(i, j).isMine()) { //if field is mine
                        canvas.drawBitmap(bmBomb, i * getWidth() / numRowCols, j * getHeight() / numRowCols, null);
                    } else { //field is not mine
                        canvas.drawText(MinesweeperModel.getInstance().getFieldsContent(i, j).getSurroundingTotal(),
                                (getWidth() / numRowCols) * i + 30, (getHeight() / numRowCols) * (j + 1) - 20,
                                setNumberColor(MinesweeperModel.getInstance().getFieldsContent(i, j).getSurroundingTotal()));
                    }
                }
            }
        }
    }

    public Paint setNumberColor(String number) { //different colors for each number
        int theNumber = Integer.parseInt(number);
        if (theNumber == 0) {
            paintFont.setColor(Color.WHITE);
            return paintFont;
        } else if (theNumber == 1) {
            paintFont.setColor(Color.BLUE);
            return paintFont;
        } else if (theNumber == 2) {
            paintFont.setColor(Color.GREEN);
            return paintFont;
        } else if (theNumber == 3) {
            paintFont.setColor(Color.RED);
            return paintFont;
        } else if (theNumber == 4) {
            paintFont.setColor(Color.CYAN);
            return paintFont;
        } else {
            paintFont.setColor(Color.MAGENTA);
            return paintFont;
        }

    }

    private void drawGameArea(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        for (int i = 0; i < numRowCols; i++) {
            for (int j = 0; j < numRowCols; j++) {
                canvas.drawBitmap(bmTile, i * getWidth() / numRowCols, j * getHeight() / numRowCols, null);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ((MainActivity) getContext()).changeTvStatus(numBombs + getContext().getString(R.string.tv_bombs_total));
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX() / (getWidth() / numRowCols);
            int y = (int) event.getY() / (getHeight() / numRowCols);

            if (MinesweeperModel.getInstance().getFieldsContent(x, y).isWasTouched() == false) { //untouched thus far
                if (MinesweeperModel.getInstance().isFlagMode() == false) { //try mode
                    if (MinesweeperModel.getInstance().getFieldsContent(x, y).isMine() == true) { //is a mine
                        ((MainActivity) getContext()).showFinalMessage(getContext().getString(R.string.you_lose));
                        ((MainActivity) getContext()).changeTvStatus(getContext().getString(R.string.kaboom_msg));
                        for (int i = 0; i < numRowCols; i++) {
                            for (int j = 0; j < numRowCols; j++) {
                                MinesweeperModel.getInstance().getFieldsContent(i, j).setWasTouched(true);//display board contents
                            }
                        }
                    } else { //not a mine
                        MinesweeperModel.getInstance().getFieldsContent(x, y).setWasTouched(true);
                    }
                } else { //flag mode
                    if (MinesweeperModel.getInstance().getFieldsContent(x, y).isMine()) { //if flag a mine
                        MinesweeperModel.getInstance().getFieldsContent(x, y).setWasTouched(true);
                        MinesweeperModel.getInstance().getFieldsContent(x, y).setFlagged(true);
                        MinesweeperModel.getInstance().setNumFlags(getNumFlags() - 1); //decrease amount of flags available
                        if (MinesweeperModel.getInstance().hasPlayerWon()) { //test if final flag
                            ((MainActivity) getContext()).showFinalMessage(getContext().getString(R.string.win_message));
                            ((MainActivity) getContext()).changeTvStatus(getContext().getString(R.string.congrats_msg));

                        } else { //if game not over
                            ((MainActivity) getContext()).
                                    showMessage(getContext().
                                            getString(R.string.you_have) + getNumFlags() + getContext().getString(R.string.flags_left));
                        }
                    } else { //if flag a field that is not a mine
                        ((MainActivity) getContext()).showFinalMessage(getContext().getString(R.string.you_lose));
                        for (int i = 0; i < numRowCols; i++) {
                            for (int j = 0; j < numRowCols; j++) {
                                MinesweeperModel.getInstance().getFieldsContent(i, j).setWasTouched(true);//display board contents
                            }
                        }
                    }
                }
            }
            invalidate();
        }
        return true;
    }


    public void resetGame() {
        MinesweeperModel.getInstance().resetFields();
        ((MainActivity) getContext()).showMessage(getContext().getString(R.string.new_game_announce));
        MinesweeperModel.getInstance().setNumFlags(numBombs);
        ((MainActivity) getContext()).resetToggle();
        MinesweeperModel.getInstance().setFlagMode(false);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //force being square
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h; //will use smaller value to always keep square
        setMeasuredDimension(d, d); //must be called by onMeasure
    }
}













