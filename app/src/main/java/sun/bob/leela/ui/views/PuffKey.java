package sun.bob.leela.ui.views;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;

/**
 * Created by bob.sun on 16/7/22.
 */
class PuffKey extends Keyboard.Key {

    public PuffKey(Resources res, Keyboard.Row parent, int x, int y, XmlResourceParser parser) {
        super(res, parent, x, y, parser);
        if(this.label == null && this.icon == null) {
            this.icon = new ColorDrawable(Color.argb(0xff, 0, 0, 0));
            this.iconPreview = this.icon;
        }
    }

    private final static int[] KEY_STATE_NORMAL_ON = {
            android.R.attr.state_checkable,
            android.R.attr.state_checked
    };

    private final static int[] KEY_STATE_PRESSED_ON = {
            android.R.attr.state_pressed,
            android.R.attr.state_checkable,
            android.R.attr.state_checked
    };

    private final static int[] KEY_STATE_NORMAL_OFF = {
            android.R.attr.state_checkable
    };

    private final static int[] KEY_STATE_PRESSED_OFF = {
            android.R.attr.state_pressed,
            android.R.attr.state_checkable
    };

    private final static int[] KEY_STATE_FUNCTION = {
            android.R.attr.state_single
    };

    private final static int[] KEY_STATE_FUNCTION_PRESSED = {
            android.R.attr.state_pressed,
            android.R.attr.state_single
    };

    private final static int[] KEY_STATE_NORMAL = {
    };

    private final static int[] KEY_STATE_PRESSED = {
            android.R.attr.state_pressed
    };

    @Override
    public int[] getCurrentDrawableState() {
        int[] states = KEY_STATE_NORMAL;

        if (on) {
            if (pressed) {
                states = KEY_STATE_PRESSED_ON;
            } else {
                states = KEY_STATE_NORMAL_ON;
            }
        } else {
            if (sticky) {
                if (pressed) {
                    states = KEY_STATE_PRESSED_OFF;
                } else {
                    states = KEY_STATE_NORMAL_OFF;
                }
            } else if(modifier){
                if (pressed) {
                    states = KEY_STATE_FUNCTION_PRESSED;
                } else {
                    states = KEY_STATE_FUNCTION;
                }
            } else {
                if (pressed) {
                    states = KEY_STATE_PRESSED;
                }
            }
        }
        return states;
    }
}
