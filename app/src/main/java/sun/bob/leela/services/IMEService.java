package sun.bob.leela.services;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import sun.bob.leela.R;
import sun.bob.leela.ui.activities.DialogAcctList;
import sun.bob.leela.ui.views.PuffKeyboard;
import sun.bob.leela.ui.views.PuffKeyboardView;
import sun.bob.leela.utils.VibrateUtil;

public class IMEService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private PuffKeyboardView kv;
    private PuffKeyboard normalKeyboard, symbolKeyboard, symsftKeyboard, currentKeyboard, shiftKeyboard;

    private String account, password, additional;

    private boolean caps = false;

    private IBinder mToken = null;

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
    public void onInitializeInterface() {
        normalKeyboard = new PuffKeyboard(this, R.xml.keyboard_layout_qwerty);
        shiftKeyboard = new PuffKeyboard(this, R.xml.keyboard_layout_qwerty_shift);
        symbolKeyboard = new PuffKeyboard(this, R.xml.keyboard_layout_symbol);
        symsftKeyboard = new PuffKeyboard(this, R.xml.keyboard_layout_symsft);
        currentKeyboard = normalKeyboard;
    }
    @Override
    public View onCreateInputView() {
        kv = (PuffKeyboardView) View.inflate(this, R.layout.layout_ime, null);
//        kv = (PuffKeyboardView)getLayoutInflater().inflate(R.layout.layout_ime, null);
        kv.setKeyboard(currentKeyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public AbstractInputMethodImpl onCreateInputMethodInterface() {
        return new InputMethodImpl() {
            @Override
            public void attachToken(IBinder token) {
                super.attachToken(token);
                mToken = token;
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideWindow();
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
            default:
                break;
        }
        currentKeyboard.setIMEOptions(getResources(), attribute.imeOptions);
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        hideWindow();
        stopSelf();
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
            case PuffKeyboardView.KEYCODE_EDIT :
                Intent intent = new Intent(this, DialogAcctList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent);
                stopSelf();
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
                if(kv.getKeyboard() == symbolKeyboard || kv.getKeyboard() == symsftKeyboard) {
                    currentKeyboard = normalKeyboard;
                } else {
                    currentKeyboard = symbolKeyboard;
                }
                kv.setKeyboard(currentKeyboard);
                break;
            case Keyboard.KEYCODE_SHIFT:
//                if (kv.getKeyboard() == normalKeyboard) {
//                    caps = !caps;
//                    normalKeyboard.setShifted(caps);
//                    kv.invalidateAllKeys();
//                }
                if(currentKeyboard == shiftKeyboard) {
                    currentKeyboard = normalKeyboard;
                } else {
                    currentKeyboard = shiftKeyboard;
                }
                kv.setKeyboard(currentKeyboard);
                break;
            case Keyboard.KEYCODE_ALT:
                if (kv.getKeyboard() == symbolKeyboard) {
                    currentKeyboard = symsftKeyboard;
                } else {
                    currentKeyboard = symbolKeyboard;
                }
                kv.setKeyboard(currentKeyboard);
                break;
            case -886 :
                Toast.makeText(IMEService.this, "close", Toast.LENGTH_SHORT).show();
                break ;
            default:
                char code = (char)primaryCode;
                if (caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
                break;
        }

        VibrateUtil.getStaticInstance(this).tick();
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
