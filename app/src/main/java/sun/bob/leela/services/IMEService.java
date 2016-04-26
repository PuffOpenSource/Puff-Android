package sun.bob.leela.services;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import sun.bob.leela.R;

public class IMEService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;

    public IMEService() {
    }

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.layout_ime, null);
        keyboard = new Keyboard(this, R.xml.keyboard_layout);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        switch(primaryCode){
            case -11 :
                ic.commitText("Account", 1);
                break;
            case -12:
                ic.commitText("Password", 1);
                break;
            case -13:
                ic.commitText("Additional", 1);
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                break;
//                char code = (char)primaryCode;
//                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
