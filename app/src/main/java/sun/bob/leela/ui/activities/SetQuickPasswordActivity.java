package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.eftimoff.patternview.PatternView;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.runnable.QuickPassRunnable;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CryptoUtil;

public class SetQuickPasswordActivity extends AppCompatActivity {

    public static final int ShowTypeSet         = 0x6001;
    public static final int ShowTypeVerify      = 0x6002;

    private PatternView patternView;
    private int type;
    private String masterPassword, quickCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_quick_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        patternView = (PatternView) findViewById(R.id.lock_view);
        type = getIntent().getIntExtra("type", ShowTypeSet);

        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                Log.e("Pattern", patternView.getPatternString());
                quickCode = patternView.getPatternString();
                if (type == ShowTypeVerify) {
                    checkQuickPass();
                }
            }
        });



        if (type == ShowTypeVerify) {
            masterPassword = getIntent().getStringExtra("masterPassword");
            getSupportActionBar().setTitle("Verify Quick Password");
            fab.setVisibility(View.INVISIBLE);
        } else {
            getSupportActionBar().setTitle("Set Quick Password");
            fab.setVisibility(View.VISIBLE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput())
                    return;
                startActivity(new Intent(SetQuickPasswordActivity.this, AuthorizeActivity.class));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean validateInput() {
        // TODO: 16/6/28 Validate
        return true;
    }

    private void saveQuickPass() {
        new Thread(new QuickPassRunnable(quickCode, masterPassword))
                .run();
    }

    private void checkQuickPass() {
        new Thread(new QuickPassRunnable(quickCode)).run();
    }

    public void onEventMainThread(Object event) {
        if (!(event instanceof CryptoEvent)) {
            return;
        }
        switch (((CryptoEvent) event).getType()) {
            case AppConstants.TYPE_DECRYPT :
                finish();
                break;
            case AppConstants.TYPE_ENCRYPT :
                Account save = new Account();
                save.setType(AppConstants.TYPE_QUICK);
                save.setHash(((CryptoEvent) event).getResult());

                save.setSalt("");
                save.setName("");
                save.setTag("");

                AccountHelper.getInstance(null).saveAccount(save);
                setResult(RESULT_OK);
                finish();
                break;
            case AppConstants.TYPE_MASTERPWD:
                masterPassword = ((CryptoEvent) event).getResult();
                saveQuickPass();
                break;
            case AppConstants.TYPE_SHTHPPN:
                patternView.setPattern(PatternView.DisplayMode.Wrong, patternView.getPattern());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
