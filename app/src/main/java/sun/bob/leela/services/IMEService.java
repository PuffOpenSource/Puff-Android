package sun.bob.leela.services;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import sun.bob.leela.R;
import sun.bob.leela.ui.activities.DialogAcctList;
import sun.bob.leela.ui.views.PuffKeyboardView;

public class IMEService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private PuffKeyboardView kv;
    private Keyboard normalKeyboard, symbolKeyboard, symsftKeyboard;

    private String account, password, additional;

    private boolean caps = false;

    public IMEService() {
        account = "";
        password = "";
        additional = "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == "INIT") {
            account = intent.getStringExtra("account");
            password = intent.getStringExtra("password");
            additional = intent.getStringExtra("additional");
        }
        return START_STICKY;
    }

        @Override
    public View onCreateInputView() {
        Log.e("Leela IME", "onCreateInputView");
        kv = (PuffKeyboardView)getLayoutInflater().inflate(R.layout.layout_ime, null);
        normalKeyboard = new Keyboard(this, R.xml.keyboard_layout_qwerty);
        kv.setKeyboard(normalKeyboard);
        symbolKeyboard = new Keyboard(this, R.xml.keyboard_layout_symbol);
        symsftKeyboard = new Keyboard(this, R.xml.keyboard_layout_symsft);
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
            case -10 :
                Intent intent = new Intent(this, DialogAcctList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case -11 :
                ic.commitText(account, 1);
                account = "";
                break;
            case -12:
                ic.commitText(password, 1);
                password = "";
                break;
            case -13:
                ic.commitText(additional, 1);
                additional = "";
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                if(kv.getKeyboard() == normalKeyboard) {
                    kv.setKeyboard(symbolKeyboard);
                } else {
                    kv.setKeyboard(normalKeyboard);
                }
                break;
            case Keyboard.KEYCODE_SHIFT:
                if (kv.getKeyboard() == normalKeyboard) {
                    caps = !caps;
                    normalKeyboard.setShifted(caps);
                    kv.invalidateAllKeys();
                }
                break;
            case Keyboard.KEYCODE_ALT:
                if (kv.getKeyboard() == symbolKeyboard) {
                    kv.setKeyboard(symsftKeyboard);
                } else {
                    kv.setKeyboard(symbolKeyboard);
                }
                break;
            default:
                char code = (char)primaryCode;
                if (caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
                break;
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
