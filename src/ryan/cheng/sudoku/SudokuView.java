
package ryan.cheng.sudoku;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SudokuView extends View implements Callback {
    private static final String TAG = "SudokuView";
    private Sudoku mSudoku;
    private int mMinSize;
    private Paint mPaint;

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            int col = (int) (x / getWidth() * Sudoku.SUDOKU_NUMBER);
            int row = (int) (y / getHeight() * Sudoku.SUDOKU_NUMBER);
            Log.d(TAG, "clicked row:" + row + ", col:" + col);
            mSudoku.setFocus(row, col);
            invalidate();

            return super.onSingleTapUp(e);
        }
    }
    GestureDetector mDetector;

    public SudokuView(Context context) {
        this(context, null);
    }

    public SudokuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SudokuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = context.getResources();
        mSudoku = new Sudoku(res, this);
        mMinSize = res.getDimensionPixelSize(R.dimen.sudoku_min_size);
        mPaint = new Paint();
        mDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mMinSize;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return mMinSize;
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int computedWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int computedHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        int size = Math.min(computedWidth, computedHeight);
        mSudoku.layout(size);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSudoku.draw(canvas, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.d(TAG, "" + action + " ev.getPointerCount() = " + event.getPointerCount());
        boolean result = mDetector.onTouchEvent(event);

        return result;
    }

    public Sudoku getSudoku() {
        return mSudoku;
    }

    @Override
    public boolean handleMessage(Message msg) {
        invalidate();
        return true;
    }

}
