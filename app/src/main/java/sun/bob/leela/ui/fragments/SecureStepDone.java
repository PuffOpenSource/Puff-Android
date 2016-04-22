package sun.bob.leela.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureStepDone extends SecureSlide.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);

        return ret;
    }

    public static SecureStepDone newInstance(@LayoutRes int layoutRes) {
        SecureStepDone fragment = new SecureStepDone();
        Bundle arguments = new Bundle();
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES", layoutRes);
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES", -1);
        fragment.setArguments(arguments);
        return fragment;
    }
}
