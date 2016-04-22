package sun.bob.leela.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sun.bob.leela.R;

/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureStepDone extends SecureSlide.Fragment {

    AppCompatTextView password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);
        password = (AppCompatTextView) ret.findViewById(R.id.password);
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

    public void setPassword(String password) {
        this.password.setText(password);
    }
}
