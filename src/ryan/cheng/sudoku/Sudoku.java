package ryan.cheng.sudoku;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Handler.Callback;
import android.util.Log;

public class Sudoku extends AbstractItem {
    private static final String TAG = "Sudoku";
    private Atom[][] mAtomsCell = new Atom[SUDOKU_NUMBER][SUDOKU_NUMBER];
    private Atom[][] mAtomsRow = new Atom[SUDOKU_NUMBER][SUDOKU_NUMBER];
    private Atom[][] mAtomsCol = new Atom[SUDOKU_NUMBER][SUDOKU_NUMBER];
    private int mFocusRow = -1;
    private int mFocusCol = -1;
    private int mLineWidth;
    private int mLineColor;
    private int mSecondLineWidth;
    private int mSecondLineColor;
    private float[] mLines;
    private Handler mHandler;

    public Sudoku(Resources res, Callback callback) {
        mLineWidth = res.getDimensionPixelOffset(R.dimen.sudoku_line_width);
        mLineColor = res.getColor(R.color.sudoku_line_color);
        mSecondLineWidth = res.getDimensionPixelOffset(R.dimen.sudoku_cell_line_width);
        mSecondLineColor = res.getColor(R.color.sudoku_cell_line_color);
        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            int a = i / SUDOKU_ROW_NUMBER;
            int b = i % SUDOKU_ROW_NUMBER;
            for (int j = 0; j < SUDOKU_NUMBER; j++) {
                int x = j / SUDOKU_ROW_NUMBER;
                int y = j % SUDOKU_ROW_NUMBER;
                mAtomsRow[i][j] = new Atom(res);
                mAtomsCol[j][i] = mAtomsRow[i][j];
                mAtomsCell[x + a * SUDOKU_ROW_NUMBER][y + b * SUDOKU_ROW_NUMBER] = mAtomsRow[i][j];
            }
        }

        mHandler = new Handler(callback);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (mLines == null) {
            return;
        }
        paint.setAntiAlias(false);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(mSecondLineWidth);
        paint.setColor(mSecondLineColor);
        canvas.drawLines(mLines, 32, 48, paint);
        paint.setStrokeWidth(mLineWidth);
        paint.setColor(mLineColor);
        canvas.drawLines(mLines, 0, 32, paint);
        paint.setAntiAlias(true);
        Atom focusOne = null;
        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            for (int j = 0; j < SUDOKU_NUMBER; j++) {
                boolean focused = (i == mFocusRow && j == mFocusCol);
                mAtomsRow[i][j].setFocused(focused);
                if (focused) {
                    focusOne = mAtomsRow[i][j];
                } else {
                    mAtomsRow[i][j].draw(canvas, paint);
                }
            }
        }
        if (focusOne != null) {
            focusOne.draw(canvas, paint);
        }
    }

    public void setFocus(int row, int col) {
        if (mFocusRow == row && mFocusCol == col) {
            mFocusRow = -1;
            mFocusCol = -1;
        } else {
            mFocusRow = row;
            mFocusCol = col;
        }
    }

    public void layout(float size) {
        setSize(size);
        mLines = new float[40 * 2];
        float cellSize = (size - mLineWidth) / SUDOKU_ROW_NUMBER;
        float atomSize = (cellSize - mLineWidth - 2 * mSecondLineWidth) / SUDOKU_ROW_NUMBER;
        int linesIndex = 0;
        float lineGap = mLineWidth / 2f;
        float secondLineGap = mSecondLineWidth / 2f;
        // 4 vertical lines
        mLines[linesIndex++] = lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = cellSize + lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = cellSize + lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = size - cellSize - lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = size - cellSize - lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = size - lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = size - lineGap;
        mLines[linesIndex++] = size;

        // 4 horizental lines
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = cellSize + lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = cellSize + lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = size - cellSize - lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = size - cellSize - lineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = size - lineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = size - lineGap;

        // 6 second vertical lines
        mLines[linesIndex++] = atomSize + mLineWidth + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = atomSize + mLineWidth + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = 2 * atomSize + mLineWidth + mSecondLineWidth + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = 2 * atomSize + mLineWidth + mSecondLineWidth + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = cellSize + mLineWidth + 2 * atomSize + secondLineGap + mSecondLineWidth;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = cellSize + mLineWidth + 2 * atomSize + secondLineGap + mSecondLineWidth;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = 2 * cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = 2 * cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = size - atomSize - mLineWidth - secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = size - atomSize - mLineWidth - secondLineGap;
        mLines[linesIndex++] = size;

        // 6 second horizental lines
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = atomSize + mLineWidth + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = atomSize + mLineWidth + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = 2 * atomSize + mLineWidth + mSecondLineWidth + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = 2 * atomSize + mLineWidth + mSecondLineWidth + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = cellSize + mLineWidth + 2 * atomSize + secondLineGap + mSecondLineWidth;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = cellSize + mLineWidth + 2 * atomSize + secondLineGap + mSecondLineWidth;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = 2 * cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = 2 * cellSize + mLineWidth + atomSize + secondLineGap;
        mLines[linesIndex++] = 0;
        mLines[linesIndex++] = size - atomSize - mLineWidth - secondLineGap;
        mLines[linesIndex++] = size;
        mLines[linesIndex++] = size - atomSize - mLineWidth - secondLineGap;

        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            //int x = i % SUDOKU_ROW_NUMBER;
            int y = i / SUDOKU_ROW_NUMBER;
            for (int j = 0; j < SUDOKU_NUMBER; j++) {
                //int a = j % SUDOKU_ROW_NUMBER;
                int b = j / SUDOKU_ROW_NUMBER;
                mAtomsRow[i][j].setSize(atomSize);
                mAtomsRow[i][j].setTranslationX(j * atomSize +
                        (j - b) * mSecondLineWidth + (b + 1) * mLineWidth);
                mAtomsRow[i][j].setTranslationY(i * atomSize +
                        (i - y) * mSecondLineWidth + (y + 1) * mLineWidth);
            }
        }
    }

    public void setNumbers() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                boolean done;
                long start = System.currentTimeMillis();
                do {
                    done = caculate();
                } while (!done);
                Log.d(TAG, "setNumbers time:" + (System.currentTimeMillis() - start));
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private boolean caculate() {
        reset();
        int[][] row = new int[3][];
        int[][] col = new int[3][];
        setOrders(row, col);
        setNumber(row, col, 1);

        boolean hasSame = false;
        int[][] row2 = new int[3][];
        int[][] col2 = new int[3][];
        do {
            setOrders(row2, col2);
            hasSame = hasSameValue(row, col, row2, col2);
        } while (hasSame);
        setNumber(row2, col2, 2);

        int[][] row3 = new int[3][];
        int[][] col3 = new int[3][];
        do {
            setOrders(row3, col3);
            hasSame = hasSameValue(row, col, row3, col3) || hasSameValue(row2, col2, row3, col3);
        } while (hasSame);
        setNumber(row3, col3, 3);

        int[][] row4 = new int[3][];
        int[][] col4 = new int[3][];
        do {
            setOrders(row4, col4);
            hasSame = hasSameValue(row, col, row4, col4) || hasSameValue(row2, col2, row4, col4)
                    || hasSameValue(row3, col3, row4, col4);
        } while (hasSame);
        setNumber(row4, col4, 4);

        int[][] row5 = new int[3][];
        int[][] col5 = new int[3][];
        do {
            setOrders(row5, col5);
            hasSame = hasSameValue(row, col, row5, col5) || hasSameValue(row2, col2, row5, col5)
                    || hasSameValue(row3, col3, row5, col5) || hasSameValue(row4, col4, row5, col5);
        } while (hasSame);
        setNumber(row5, col5, 5);

        int[][] row6 = new int[3][];
        int[][] col6 = new int[3][];
        do {
            setOrders(row6, col6);
            hasSame = hasSameValue(row, col, row6, col6) || hasSameValue(row2, col2, row6, col6)
                    || hasSameValue(row3, col3, row6, col6) || hasSameValue(row4, col4, row6, col6)
                    || hasSameValue(row5, col5, row6, col6);
        } while (hasSame);
        setNumber(row6, col6, 6);

        int[][] row7 = new int[3][];
        int[][] col7 = new int[3][];
        do {
            setOrders(row7, col7);
            hasSame = hasSameValue(row, col, row7, col7) || hasSameValue(row2, col2, row7, col7)
                    || hasSameValue(row3, col3, row7, col7) || hasSameValue(row4, col4, row7, col7)
                    || hasSameValue(row5, col5, row7, col7) || hasSameValue(row6, col6, row7, col7);
        } while (hasSame);
        setNumber(row7, col7, 7);

        generateLast2Number(mAtomsCell[0]);
        int leftAtom = 2 * (SUDOKU_NUMBER - 1);
        do {
            boolean valid = false;
            for (int i = 0; i < SUDOKU_NUMBER; i++) {
                if (finish(mAtomsRow[i]) || finish(mAtomsCol[i]) || finish(mAtomsCell[i])) {
                    leftAtom--;
                    valid = true;
                    mHandler.sendEmptyMessage(0);
                }
            }
            if (!valid) break;
        } while (leftAtom > 0);

        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            if (check(mAtomsRow[i]) != 0 || check(mAtomsCol[i]) != 0 || check(mAtomsCell[i]) != 0) {
                Log.w(TAG, "generate numbers fail!");
                return false;
            }
        }
        return true;
    }

    private void setNumber(int[][] row, int[][] col, int number) {
        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            int x = i / SUDOKU_ROW_NUMBER;
            int y = i % SUDOKU_ROW_NUMBER;
            mAtomsCell[i][row[x][y] * SUDOKU_ROW_NUMBER + col[y][x]].setNumber(number);
        }
        mHandler.sendEmptyMessage(0);
    }

    private void reset() {
        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            for (int j = 0; j < SUDOKU_NUMBER; j++) {
                mAtomsRow[i][j].setNumber(0);
            }
        }
    }

    private static void generateLast2Number(Atom[] cell) {
        int[] indexs = new int[] {8, 8};
        int t = (int) (Math.random() * 2);
        indexs[t] = 9;
        int i = 0;
        for (Atom a : cell) {
            if (a.isNumberInvalid()) {
                a.setNumber(indexs[i++]);
            }
        }
    }

    private static boolean finish(Atom[] atoms) {
        Atom invalidAtom = null;
        int number = 0;
        for (Atom a : atoms) {
            if (a.isNumberInvalid()) {
                if (invalidAtom == null) {
                    invalidAtom = a;
                } else {
                    invalidAtom = null;
                }
            } else {
                if (a.getNumber() == 9) {
                    number = 8;
                } else if (a.getNumber() == 8) {
                    number = 9;
                }
            }
        }
        if (invalidAtom != null && number != 0) {
            invalidAtom.setNumber(number);
            return true;
        }
        return false;
    }

    private static int check(Atom[] atoms) {
        int result = 0x1ff;
        for (Atom a : atoms) {
            if (a.isNumberInvalid()) continue;
            result &= ~NUMBER_FLAG[a.getNumber() - 1];
        }
        return result;
    }

    private static boolean hasSameValue(int[][] row, int[][] col, int[][] row2, int[][] col2) {
        for (int i = 0; i < SUDOKU_NUMBER; i++) {
            int x = i / SUDOKU_ROW_NUMBER;
            int y = i % SUDOKU_ROW_NUMBER;
            if(row[x][y] == row2[x][y] && col[y][x] == col2[y][x])
                return true;
        }
        return false;
    }

    private static void setOrders(int[][] row, int[][] col) {
        row[0] = generateOrder();
        row[1] = generateOrder();
        row[2] = generateOrder();
        col[0] = generateOrder();
        col[1] = generateOrder();
        col[2] = generateOrder();

    }

    private static int[] generateOrder() {
        int t = (int) (Math.random() * ORDERS_SIZE);
        return ORDERS[t];
    }

}
