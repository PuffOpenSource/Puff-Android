package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;


import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import java.security.SecureRandom;
import java.util.ArrayList;

import sun.bob.leela.R;
import com.heinrichreimersoftware.materialintro.app.SlideListener;
import sun.bob.leela.ui.fragments.SecureSlide;
import sun.bob.leela.ui.fragments.SecureStepDone;
import sun.bob.leela.ui.fragments.SecureStepIntro;
import sun.bob.leela.ui.fragments.SecureStepTypeSelect;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.PasswordGenerator;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;

public class PasswordGenActivity extends IntroActivity implements SlideListener {

    public enum PasswordType {
        Number,
        Secure
    }

    private SecureSlide introSlide, typeSlide, wordsSlide, doneSlide;
    SecureStepTypeSelect typeSlideFragment;
//    SecureStepWords wordsSlideFragment;
    SecureStepIntro introStepFragment;
    SecureStepDone doneSlideFragment;
    private ArrayList<String> words;
    private int length;
    private PasswordType type;

    private String password = "";

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
                Intent result = new Intent();
                result.putExtra("password", password);
                setResult(RESULT_OK, result);
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

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    ((SecureStepDone) getSlide(position).getFragment()).setPassword(password);
                    ((SecureStepDone) getSlide(position).getFragment()).setWordsAndType(words, type);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        setCanSwipe(false);

        setSlideListener(this);

        addSlide(introSlide);
        addSlide(typeSlide);
        addSlide(doneSlide);
    }


    @Override
    public void willLeaveSlide(int position) {
        ResUtil.hideSoftKeyboard(getCurrentFocus());
        switch (position) {
            case 0:
                length = ((SecureStepIntro) getSlide(0).getFragment()).getLength();
                length = length < 3 ? 3 : length;
                break;
            case 1:
                words = ((SecureStepTypeSelect) getSlide(1).getFragment()).getWords();
                type = ((SecureStepTypeSelect) getSlide(1).getFragment()).getType();
                generatePassword();
                break;
            default:
                break;
        }
    }

    private void generatePassword() {
        if (words == null || words.size() == 0) {
            //No words provided.
            if (type == PasswordType.Number) {
                password = PasswordGenerator.getNumPassword(length, length);
            } else {
                password = PasswordGenerator.getPassword(length, length);
            }
            return;
        } else {
            for (String s : words) {
                password += StringUtil.getMaskedWord(s);
                if (password.length() > length) {
                    password = password.substring(0, length);
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
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
