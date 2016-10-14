package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import sun.bob.leela.R;

/**
 * Created by bob.sun on 16/8/15.
 */
public class PuffIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_one)
//                .description(R.string.description_1)
                .image(R.drawable.onboarding_slide_one)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                    .title(R.string.title_intro_two)
                    .image(R.drawable.onboarding_slide_two)
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_three)
                .image(R.drawable.ic_puff)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        setSkipEnabled(false);
        setFullscreen(true);
        setAllowFinish(false);
        setRunWhenFinish(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PuffIntroActivity.this, MainActivity.class));
                PuffIntroActivity.this.finish();
            }
        });
        /**
         * Custom fragment slide
         */
//        addSlide(new FragmentSlide.Builder()
//                .background(R.color.background_2)
//                .backgroundDark(R.color.background_dark_2)
//                .fragment(R.layout.fragment_2, R.style.FragmentTheme)
//                .build());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
