package sun.bob.leela.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import sun.bob.leela.R;
import sun.bob.leela.utils.ResUtil;

/**
 * Created by bob.sun on 16/4/26.
 */
public class PuffKeyboardView extends KeyboardView {

    public static final int TYPE_ABC = 0;
    public static final int TYPE_ABC_SHIFT = 1;
    public static final int TYPE_SYMBOL = 2;
    public static final int TYPE_SYMBOL_MORE = 3;
    public static final int KEYCODE_EDIT = -10;
    private int currentType = TYPE_ABC;

    private final int VERTICAL_PADDING = 20;
    private final int HORIZONTAL_PADDING = 30;
    private final int VERTICAL_PADDING_SPACE = 40;
    private final int HORIZONTAL_PADDING_SPACE = 100;

    private Paint linePaint, keyTextPaint, smallTextPaint, keyPaint;
    int normalSize, smallSize;
    private Drawable keyDrawable, backgroundDrawable;

    public PuffKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        linePaint = new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);

        keyTextPaint = new Paint();
        keyTextPaint.setTextAlign(Paint.Align.CENTER);
        normalSize = ResUtil.getInstance(getContext()).pointToDp(20);
        keyTextPaint.setTextSize(normalSize);
        keyTextPaint.setColor(Color.rgb(42, 55, 62));
//        keyTextPaint.setShadowLayer(5, 0, 0, Color.BLACK);
        keyTextPaint.setFakeBoldText(true);

        smallTextPaint = new Paint();
        smallTextPaint.setTextAlign(Paint.Align.CENTER);
        smallSize = ResUtil.getInstance(getContext()).pointToDp(12);
        smallTextPaint.setTextSize(smallSize);
        smallTextPaint.setColor(Color.rgb(42, 55, 62));

        keyPaint = new Paint();
        keyPaint.setColor(Color.LTGRAY);
        keyDrawable = context.getResources().getDrawable(R.drawable.key_rect);
        backgroundDrawable = context.getResources().getDrawable(R.drawable.backgroun_ime);

    }

    // TODO 这里进行整理
    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            switch (currentType) {
                case TYPE_ABC:
                    drawABC(key, canvas);
                    break;
                case TYPE_ABC_SHIFT:

                    break;
                case TYPE_SYMBOL:

                    break;
                case TYPE_SYMBOL_MORE:

                    break;
                default:

                    break;
            }
        }
    }

    private void drawABC(Keyboard.Key key, Canvas canvas) {
        if (key.codes[0] == -1) {
            drawKeyBackground(R.drawable.sym_keyboard_not_shift, canvas, key, getNormalPadding());
        } else if (key.codes[0] == -5) {
            drawKeyBackground(R.drawable.sym_keyboard_delete, canvas, key, getNormalPadding());
        } else if (key.codes[0] == 32) {
            drawKeyBackground(R.drawable.btn_keyboard_key_space, canvas, key, getNullPadding());
        } else if (key.codes[0] == 10) {
            drawKeyBackground(R.drawable.sym_keyboard_return2, canvas, key, getNormalPadding());
        }
        if (key.codes[0] == -10) {
            drawKeyBackground(R.drawable.btn_keyboard_key_puff, canvas, key, getNullPadding());
        } else if (key.codes[0] == -11) {
            drawKeyBackground(R.drawable.btn_keyboard_key_puff, canvas, key, getNullPadding());
        } else if (key.codes[0] == -12) {
            drawKeyBackground(R.drawable.btn_keyboard_key_puff, canvas, key, getNullPadding());
        } else if (key.codes[0] == -13) {
            drawKeyBackground(R.drawable.btn_keyboard_key_puff, canvas, key, getNullPadding());
        }
    }


    private Rect getNormalPadding() {
        return new Rect(HORIZONTAL_PADDING, VERTICAL_PADDING, -HORIZONTAL_PADDING, -VERTICAL_PADDING);
    }

    private Rect getNullPadding() {
        return new Rect(0, 0, 0, 0);
    }

    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key, Rect padBounds) {
        Drawable npd = getContext().getResources().getDrawable(drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x + padBounds.left,
                key.y + padBounds.top,
                key.x + key.width + padBounds.right,
                key.y + key.height + padBounds.bottom);
        npd.draw(canvas);
    }
}
