package sun.bob.leela.ui.activities;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;


import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import java.util.ArrayList;

import sun.bob.leela.R;
import com.heinrichreimersoftware.materialintro.app.SlideListener;
import sun.bob.leela.ui.fragments.SecureSlide;
import sun.bob.leela.ui.fragments.SecureStepIntro;
import sun.bob.leela.ui.fragments.SecureStepTypeSelect;
import sun.bob.leela.ui.fragments.SecureStepWords;

public class PasswordGenActivity extends IntroActivity implements SlideListener {

//    private ViewPager viewPager;

    private enum LayoutType {
        NUMBER,
        ALPHA,
        ALL
    }

    private LayoutType layoutType;

    private SecureSlide introSlide, typeSlide, wordsSlide;
    SecureStepTypeSelect typeSlideFragment;
    SecureStepWords wordsSlideFragment;
    SecureStepIntro introStepFragment;
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
                words = wordsSlideFragment.getWords();
            }
        });
        getSupportActionBar().setTitle("Secure Password Generator");

        wireRefs();

        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }

            @Override
            public boolean canGoBackward(int position) {
                return true;
            }
        });

        setSlideListener(this);

//        FragmentSlide slide0 = new FragmentSlide.Builder()
//                .background(R.color.colorPrimary)
//                .backgroundDark(R.color.colorPrimaryDark)
//                .fragment(R.layout.activity_password_gen, R.style.AppTheme)
//                .build();
//
//        SecureStepWords two = SecureStepWords.newInstance(R.layout.fragment_step_words);
//        SecureSlide slide1 = new SecureSlide.Builder()
//                .background(R.color.colorPrimary)
//                .backgroundDark(R.color.colorPrimaryDark)
//                .fragment(two)
//                .build();
//
//        addSlide(slide0);
//        addSlide(slide1);

        addSlide(introSlide);
        addSlide(typeSlide);
    }

    @Override
    public void layoutNumber() {
        this.layoutType = LayoutType.NUMBER;
    }

    @Override
    public void layoutAlphaOnly() {
        this.layoutType = LayoutType.ALPHA;
    }

    @Override
    public void layoutAll() {
        this.layoutType = LayoutType.ALL;

    }

    @Override
    public void addWordsSlide() {
        addSlide(wordsSlide);
    }

    @Override
    public void removeWordsSlide() {
        removeSlide(wordsSlide);
    }

    @Override
    public void willLeaveSlide(int position) {
        if (position == 0) {
            length = ((SecureStepIntro) getSlide(0).getFragment()).getLength();
            return;
        }
        Slide slide = getSlide(position);
        if (slide.getFragment() instanceof SecureStepWords) {
            words = ((SecureStepWords) slide.getFragment()).getWords();
        }
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

        wordsSlideFragment = SecureStepWords.newInstance(R.layout.fragment_step_words);
        wordsSlideFragment.setSlideListener(this);
        wordsSlide = new SecureSlide.Builder()
                .fragment(wordsSlideFragment)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build();
    }
}
