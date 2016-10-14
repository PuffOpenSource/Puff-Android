package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CryptoUtil;
import sun.bob.leela.utils.UserDefault;


public class SplashActivity extends AppCompatActivity {

    private boolean newInstall;
    private boolean noMainPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDefault userDefault = UserDefault.getInstance(this);
        newInstall = userDefault.getBoolean("newInstall");
        noMainPassword = userDefault.getBoolean("noMainPassword");
        EventBus.getDefault().register(this);
        if (newInstall) {
            startActivity(new Intent(this, PuffIntroActivity.class));
            userDefault.save("newInstall", false);
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (UserDefault.getInstance(null).needPasswordWhenLaunch()) {
                        startActivity(new Intent(SplashActivity.this, AuthorizeActivity.class));
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
        return;
    }

    @Override
    public void finish() {
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Object event) {
        if (!(event instanceof CryptoEvent)) {
            return;
        }
        switch (((CryptoEvent) event).getType()) {
            case AppConstants.TYPE_MASTERPWD:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
                break;
            default:
                break;
        }
    }

}
