package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import sun.bob.leela.R;
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
        // TODO: 16/3/19 Remove below debug code
        newInstall = false;
        noMainPassword = false;

        if (newInstall) {

        } else if (noMainPassword) {

        } else {
            setContentView(R.layout.activity_splash);
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
        return;
    }

}
