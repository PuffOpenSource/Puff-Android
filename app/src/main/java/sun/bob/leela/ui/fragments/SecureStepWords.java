package sun.bob.leela.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.listeners.SlideListener;
import sun.bob.leela.utils.StringUtil;

/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureStepWords extends FragmentSlide.FragmentSlideFragment {

    private SlideListener slideListener;
    private AppCompatTextView word1, word2, word3, word4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);
        word1 = (AppCompatTextView) ret.findViewById(R.id.word_one);
        return ret;
    }

    public static SecureStepWords newInstance(@LayoutRes int layoutRes) {
        SecureStepWords fragment = new SecureStepWords();
        Bundle arguments = new Bundle();
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES", layoutRes);
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES", -1);
        fragment.setArguments(arguments);

        return fragment;
    }

    public ArrayList<String> getWords() {
        ArrayList<AppCompatTextView> textViews = new ArrayList();
        textViews.add(word1);
        textViews.add(word2);
        textViews.add(word3);
        textViews.add(word4);
        ArrayList<String> ret = new ArrayList<>();
        for (AppCompatTextView e : textViews) {
            if (StringUtil.isNullOrEmpty(e.getText().toString())) {
                ret.add(e.getText().toString());
            }
        }

        return ret;
    }

    public void setSlideListener(SlideListener slideListener) {
        this.slideListener = slideListener;
    }
}
