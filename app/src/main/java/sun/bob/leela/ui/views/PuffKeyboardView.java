package sun.bob.leela.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * Created by bob.sun on 16/4/26.
 */
public class PuffKeyboardView extends KeyboardView {

    public enum KeyBoardType {
        Paste,
        QWERTY
    }
    public PuffKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint = new Paint();
//        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setTextSize(25);
//        paint.setColor(Color.RED);
//
//        List<Keyboard.Key> keys = getKeyboard().getKeys();
//        for(Keyboard.Key key: keys) {
//            Log.e("Leela keyboard key:", (String) key.label);
//            if(key.label != null)
//                canvas.drawText(key.label.toString(), key.x + (key.width/2), key.y + 25, paint);
//        }
    }
}
