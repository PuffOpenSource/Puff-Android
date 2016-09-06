package sun.bob.leela.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import sun.bob.leela.R;

/**
 * Created by bob.sun on 16/4/26.
 */
public class PuffKeyboardView extends KeyboardView {

    public static final int TYPE_ABC = 0;
    public static final int TYPE_ABC_SHIFT = 1;
    public static final int TYPE_SYMBOL = 2;
    public static final int TYPE_SYMBOL_MORE = 3;
    public static final int KEYCODE_EDIT = -10;
    private int currentType = 911;


    public PuffKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        initType(keys);
        for (Keyboard.Key key : keys) {
            drawSpecialKey(key, canvas);
        }
    }

    /**
     * 判断为何种键盘, 通过某一个按键的code值
     * TODO 注意，这种方法有弊端：如果xml中的最上面一排的按键数目发生变化，这里应该改变
     *
     * @param keys
     */
    private void initType(List<Keyboard.Key> keys) {
        switch (keys.get(5).codes[0]) {
            case 113:
                currentType = TYPE_ABC;
                break;
            case 81:
                currentType = TYPE_ABC_SHIFT;
                break;
            case 49:
                currentType = TYPE_SYMBOL;
                break;
            case 126:
                currentType = TYPE_SYMBOL_MORE;
                break;
        }
    }

    /**
     * 通过Key.code判断，应该画什么
     *
     * @param key
     * @param canvas
     */
    private void drawSpecialKey(Keyboard.Key key, Canvas canvas) {
        int drawableId = 0;
        switch (key.codes[0]) {
            case -1:
                if (currentType == TYPE_ABC)  // 大小写的图标不同
                    drawableId = R.drawable.btn_keyboard_key_shift;
                else
                    drawableId = R.drawable.btn_keyboard_key_shifted;
                break;
            case 32:
                drawableId = R.drawable.btn_keyboard_key_space;
                break;
            case 10:
                drawableId = R.drawable.btn_keyboard_key_return;
                break;
            case -5:
                drawableId = R.drawable.btn_keyboard_key_delete;
                break;
            case -10:
                drawableId = R.drawable.btn_keyboard_key_puff;
                break;
            case -11:
                drawableId = R.drawable.btn_keyboard_key_account;
                break;
            case -12:
                drawableId = R.drawable.btn_keyboard_key_password;
                break;
            case -13:
                drawableId = R.drawable.btn_keyboard_key_additional;
                break;
            default:
                break;
        }
        if (drawableId != 0)
            drawKeyBackground(drawableId, canvas, key);
    }

    /**
     * 画图
     * 特殊的键盘按键，或其图片背景
     *
     * @param drawableId
     * @param canvas
     * @param key
     */
    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable npd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            npd = getContext().getDrawable(drawableId);
        } else {
            npd = getContext().getResources().getDrawable(drawableId);
        }
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        npd.draw(canvas);
    }
}
