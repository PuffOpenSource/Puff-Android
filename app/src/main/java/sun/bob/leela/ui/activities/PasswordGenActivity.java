package sun.bob.leela.ui.activities;

import android.os.Bundle;


import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import java.util.ArrayList;

import sun.bob.leela.R;
import com.heinrichreimersoftware.materialintro.app.SlideListener;
import sun.bob.leela.ui.fragments.SecureSlide;
import sun.bob.leela.ui.fragments.SecureStepDone;
import sun.bob.leela.ui.fragments.SecureStepIntro;
import sun.bob.leela.ui.fragments.SecureStepTypeSelect;

public class PasswordGenActivity extends IntroActivity implements SlideListener {

//    private ViewPager viewPager;

    private SecureSlide introSlide, typeSlide, wordsSlide, doneSlide;
    SecureStepTypeSelect typeSlideFragment;
//    SecureStepWords wordsSlideFragment;
    SecureStepIntro introStepFragment;
    SecureStepDone doneSlideFragment;
    private ArrayList<String> words;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSkipEnabled(false);
        setFinishEnabled(true);
        setAllowFinish(false);
        setRunWhenFinish(new Runnable() {
            @Override
            public void run() {
//                words = wordsSlideFragment.getWords();
                finish();
            }
        });
//        getSupportActionBar().setTitle("Secure Password Generator");

        wireRefs();

        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }

            @Override
            public boolean canGoBackward(int position) {
                return position <= 1;
            }
        });

        setSlideListener(this);

        addSlide(introSlide);
        addSlide(typeSlide);
        addSlide(doneSlide);
    }


    @Override
    public void willLeaveSlide(int position) {
        if (position == 0) {
            length = ((SecureStepIntro) getSlide(0).getFragment()).getLength();
            return;
        }
        Slide slide = getSlide(position);
//        if (slide.getFragment() instanceof SecureStepWords) {
//            words = ((SecureStepWords) slide.getFragment()).getWords();
//        }
    }


    private void wireRefs() {

        introStepFragment = SecureStepIntro.newInstance(R.layout.fragment_step_intro);
        introSlide = new SecureSlide.Builder()
                .fragment(introStepFragment)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build();

        typeSlideFragment = SecureStepTypeSelect.newInstance(R.layout.fragment_step_type_select);
        typeSlideFragment.setSlideListener(this);
        typeSlide = new SecureSlide.Builder()
                .fragment(typeSlideFragment)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build();

        doneSlideFragment = SecureStepDone.newInstance(R.layout.fragment_step_done);
        doneSlide = new SecureSlide.Builder()
                .fragment(doneSlideFragment)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build();
    }
}
