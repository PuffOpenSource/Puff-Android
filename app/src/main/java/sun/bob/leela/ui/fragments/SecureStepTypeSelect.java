package sun.bob.leela.ui.fragments;


import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import sun.bob.leela.R;
import sun.bob.leela.ui.activities.PasswordGenActivity;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;

import com.heinrichreimersoftware.materialintro.app.SlideListener;

import java.util.ArrayList;

/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureStepTypeSelect extends FragmentSlide.FragmentSlideFragment {

    private SlideListener slideListener;
    private AppCompatCheckBox easy, numbers, secure;
    private AppCompatEditText word1, word2, word3, word4;
    private LinearLayout checkboxes;
    private ScrollView words;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);

        checkboxes = (LinearLayout) ret.findViewById(R.id.checkboxes);
        words = (ScrollView) ret.findViewById(R.id.words);
        words.setVisibility(View.GONE);

        word1 = (AppCompatEditText) ret.findViewById(R.id.word_one);
        word2 = (AppCompatEditText) ret.findViewById(R.id.word_two);
        word3 = (AppCompatEditText) ret.findViewById(R.id.word_three);
        word4 = (AppCompatEditText) ret.findViewById(R.id.word_four);

        numbers = (AppCompatCheckBox) ret.findViewById(R.id.checkbox_numbers);
        numbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    easy.setChecked(false);
                    easy.setEnabled(false);
                    secure.setChecked(false);
                } else {
                    easy.setEnabled(true);
                    secure.setChecked(true);
                }
            }
        });

        easy = (AppCompatCheckBox) ret.findViewById(R.id.checkbox_easy);
        easy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    words.setVisibility(View.VISIBLE);
                } else {
                    words.setVisibility(View.GONE);
                }
            }
        });

        secure = (AppCompatCheckBox) ret.findViewById(R.id.checkbox_secure);
        secure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    numbers.setChecked(false);
                } else {
                    numbers.setChecked(true);
                }
            }
        });
        secure.setChecked(true);

        return ret;
    }

    public static SecureStepTypeSelect newInstance(@LayoutRes int layoutRes) {
        SecureStepTypeSelect fragment = new SecureStepTypeSelect();
        Bundle arguments = new Bundle();
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES", layoutRes);
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES", -1);
        fragment.setArguments(arguments);
        return fragment;
    }

    public void setSlideListener(SlideListener slideListener) {
        this.slideListener = slideListener;
    }

    public PasswordGenActivity.PasswordType getType() {
        if (secure.isChecked()) {
            return PasswordGenActivity.PasswordType.Secure;
        } else {
            return PasswordGenActivity.PasswordType.Number;
        }
    }

    public ArrayList<String> getWords() {
        ArrayList<AppCompatEditText> textViews = new ArrayList();
        textViews.add(word1);
        textViews.add(word2);
        textViews.add(word3);
        textViews.add(word4);
        ArrayList<String> ret = new ArrayList<>();
        for (AppCompatEditText e : textViews) {
            if (!StringUtil.isNullOrEmpty(e.getText().toString())) {
                ret.add(e.getText().toString().replaceAll("\\s+",""));
            }
        }

        return ret;
    }
}
