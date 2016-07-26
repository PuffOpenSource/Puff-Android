package sun.bob.leela.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

import sun.bob.leela.R;
import sun.bob.leela.utils.ResUtil;

/**
 * Created by bob.sun on 16/4/26.
 */
public class PuffKeyboardView extends KeyboardView {

    public enum KeyBoardType {
        Paste,
        QWERTY
    }

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
        keyTextPaint.setColor(Color.LTGRAY);
//        keyTextPaint.setShadowLayer(5, 0, 0, Color.BLACK);
        keyTextPaint.setFakeBoldText(true);


        smallTextPaint = new Paint();
        smallTextPaint.setTextAlign(Paint.Align.CENTER);
        smallSize = ResUtil.getInstance(getContext()).pointToDp(12);
        smallTextPaint.setTextSize(smallSize);
        smallTextPaint.setColor(Color.WHITE);

        keyPaint = new Paint();
        keyDrawable = context.getResources().getDrawable(R.drawable.key_rect);
        backgroundDrawable = context.getResources().getDrawable(R.drawable.backgroun_ime);
    }

    @Override
    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        backgroundDrawable.draw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.codes[0] > -10) {
                Drawable d = getContext().getResources().getDrawable(R.drawable.key_rect);
                d.setBounds(key.x, key.y + 4, key.x + key.width, key.y + key.height - 2);
                d.draw(canvas);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + normalSize / 2, keyTextPaint);
            } else {
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + smallSize / 2, smallTextPaint);
            }
        }
    }
}
