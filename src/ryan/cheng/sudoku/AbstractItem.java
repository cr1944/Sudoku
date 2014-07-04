
package ryan.cheng.sudoku;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class AbstractItem {
    public static final int SUDOKU_NUMBER = 9;
    public static final int SUDOKU_ROW_NUMBER = 3;
    protected static final int[] NUMBER_FLAG = new int[] {
            0x1,
            0x2,
            0x4,
            0x8,
            0x10,
            0x20,
            0x40,
            0x80,
            0x100
    };
    protected static final int[][] ORDERS = {
            {
                    0, 1, 2
            },
            {
                    0, 2, 1
            },
            {
                    1, 0, 2
            },
            {
                    1, 2, 0
            },
            {
                    2, 0, 1
            },
            {
                    2, 1, 0
            },
    };
    protected static final int ORDERS_SIZE = 6;

    protected float mSize;
    protected float mTranslationX = 0.0f;
    protected float mTranslationY = 0.0f;

    public abstract void draw(Canvas canvas, Paint paint);

    public void setSize(float size) {
        mSize = size;
    }

    public void setTranslationX(float translationX) {
        mTranslationX = translationX;
    }

    public void setTranslationY(float translationY) {
        mTranslationY = translationY;
    }
}
