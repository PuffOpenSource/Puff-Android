package sun.bob.leela.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Created by bob.sun on 16/3/20.
 */
public class ColorSquare extends Drawable {

    private String color;

    public ColorSquare(String color) {
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor(color));
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public int getIntrinsicWidth() {
        return 20;
    }

    @Override
    public int getIntrinsicHeight() {
        return 20;
    }
}
