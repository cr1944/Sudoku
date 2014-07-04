package ryan.cheng.sudoku;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class Atom extends AbstractItem {
    private int mNumber;
    private float mNumberSize;
    private int mNumberPadding;
    private int mNumberColor;
    private int mBgColor;
    private boolean mIsFocused;
    private static int DEFAULT_COLOR = Color.WHITE;
    private static Typeface DEFAULT_TYPEFACE = Typeface.SANS_SERIF;
    private final static int[] COLORS = {
        0xff33b5e5, //1
        0xff99cc00, //2
        0xffffbb33, //3
        0xffff4444, //4
        0xffaa66cc, //5
        0xff5c7a29, //6
        0xff472d56, //7
        0xffaa2116, //8
        0xff5e7c85  //9
    };

    private void setNumberColor() {
        mBgColor = isNumberInvalid() ? DEFAULT_COLOR : COLORS[mNumber - 1];
    }

    public Atom(Resources res) {
        mNumberPadding = res.getDimensionPixelSize(R.dimen.sudoku_number_padding);
        mNumberColor = res.getColor(R.color.sudoku_number_color);
    }

    public void setFocused(boolean focused) {
        mIsFocused = focused;
    }

    public void setNumber(int number) {
        mNumber = number;
        setNumberColor();
    }

    public int getNumber() {
        return mNumber;
    }

    public boolean isNumberInvalid() {
        return mNumber < 1 || mNumber > 9;
    }

    @Override
    public void setSize(float size) {
        super.setSize(size);
        mNumberSize = size - mNumberPadding * 2;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(false);
        paint.setColor(mBgColor);
        canvas.translate(mTranslationX, mTranslationY);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        float zoom = 0.1f * mSize;
        float lt = mIsFocused ? -zoom : 0;
        float rb = mIsFocused ? mSize + zoom : mSize;
        canvas.clipRect(lt, lt, rb, rb);
        canvas.drawColor(mBgColor);
        canvas.restore();
        if (!isNumberInvalid()) {
            paint.setAntiAlias(true);
            paint.setTextAlign(Align.CENTER);
            paint.setColor(mNumberColor);
            paint.setTextSize(mIsFocused ? mNumberSize * 1.2f : mNumberSize);
            paint.setTypeface(DEFAULT_TYPEFACE);
            float ascent = paint.ascent();
            float descent = paint.descent();
            float padding = (mSize + ascent + descent) / 2f;
            canvas.drawText(String.valueOf(mNumber), mSize / 2f, mSize - padding, paint);
        }
        canvas.restore();
    }

}
