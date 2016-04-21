package sun.bob.leela.ui.fragments;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import sun.bob.leela.R;
import sun.bob.leela.listeners.SlideListener;

/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureStepTypeSelect extends FragmentSlide.FragmentSlideFragment {

    private SlideListener slideListener;
    private AppCompatCheckBox easy, numbers, secure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);
        easy = (AppCompatCheckBox) ret.findViewById(R.id.checkbox_easy);
        easy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    slideListener.addWordsSlide();
                } else {
                    slideListener.removeWordsSlide();
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
}
