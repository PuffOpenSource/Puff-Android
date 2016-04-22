package sun.bob.leela.ui.fragments;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import sun.bob.leela.R;
import sun.bob.leela.utils.StringUtil;

import com.heinrichreimersoftware.materialintro.app.SlideListener;

import java.util.ArrayList;

/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureStepTypeSelect extends FragmentSlide.FragmentSlideFragment {

    private SlideListener slideListener;
    private AppCompatCheckBox easy, numbers;
    private AppCompatEditText word1, word2, word3, word4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);

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
                } else {
                    easy.setEnabled(true);
                }
            }
        });

        easy = (AppCompatCheckBox) ret.findViewById(R.id.checkbox_easy);
        easy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    slideListener.addWordsSlide();
                } else {
//                    slideListener.removeWordsSlide();
                }
            }
        });
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

    public ArrayList<String> getWords() {
        ArrayList<AppCompatEditText> textViews = new ArrayList();
        textViews.add(word1);
        textViews.add(word2);
        textViews.add(word3);
        textViews.add(word4);
        ArrayList<String> ret = new ArrayList<>();
        for (AppCompatEditText e : textViews) {
            if (!StringUtil.isNullOrEmpty(e.getText().toString())) {
                ret.add(e.getText().toString());
            }
        }

        return ret;
    }
}
