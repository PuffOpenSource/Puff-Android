package sun.bob.leela.ui.fragments;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;


/**
 * Created by bob.sun on 16/4/21.
 */
public class SecureSlide extends Slide {
    private final android.support.v4.app.Fragment fragment;
    @ColorRes
    private final int background;
    @ColorRes
    private final int backgroundDark;
    private final boolean canGoForward;
    private final boolean canGoBackward;

    private SecureSlide(SecureSlide.Builder builder) {
        this.fragment = builder.fragment;
        this.background = builder.background;
        this.backgroundDark = builder.backgroundDark;
        this.canGoForward = builder.canGoForward;
        this.canGoBackward = builder.canGoBackward;
    }

    public android.support.v4.app.Fragment getFragment() {
        return this.fragment;
    }

    public int getBackground() {
        return this.background;
    }

    public int getBackgroundDark() {
        return this.backgroundDark;
    }

    public boolean canGoForward() {
        return this.fragment instanceof SlideFragment ?((SlideFragment)this.fragment).canGoForward():this.canGoForward;
    }

    public boolean canGoBackward() {
        return this.fragment instanceof SlideFragment?((SlideFragment)this.fragment).canGoBackward():this.canGoBackward;
    }

    public static class Fragment extends android.support.v4.app.Fragment {
        private static final String ARGUMENT_LAYOUT_RES = "com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES";
        private static final String ARGUMENT_THEME_RES = "com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES";

        public Fragment() {
        }

        public static SecureSlide.Fragment newInstance(@LayoutRes int layoutRes, @StyleRes int themeRes) {
            SecureSlide.Fragment fragment = new SecureSlide.Fragment();
            Bundle arguments = new Bundle();
            arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES", layoutRes);
            arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES", themeRes);
            fragment.setArguments(arguments);
            return fragment;
        }

        public static SecureSlide.Fragment newInstance(@LayoutRes int layoutRes) {
            return newInstance(layoutRes, -1);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.getActivity(), this.getArguments().getInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES"));
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            return localInflater.inflate(this.getArguments().getInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES"), container, false);
        }
    }

    public static class Builder {
        private android.support.v4.app.Fragment fragment;
        @ColorRes
        private int background;
        @ColorRes
        private int backgroundDark = 0;
        private boolean canGoForward = true;
        private boolean canGoBackward = true;

        public Builder() {
        }

        public SecureSlide.Builder fragment(android.support.v4.app.Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public SecureSlide.Builder fragment(@LayoutRes int layoutRes, @StyleRes int themeRes) {
            this.fragment = SecureSlide.Fragment.newInstance(layoutRes, themeRes);
            return this;
        }

        public SecureSlide.Builder fragment(@LayoutRes int layoutRes) {
            this.fragment = SecureSlide.Fragment.newInstance(layoutRes);
            return this;
        }

        public SecureSlide.Builder fragment(FragmentSlide.FragmentSlideFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public SecureSlide.Builder background(@ColorRes int background) {
            this.background = background;
            return this;
        }

        public SecureSlide.Builder backgroundDark(@ColorRes int backgroundDark) {
            this.backgroundDark = backgroundDark;
            return this;
        }

        public SecureSlide.Builder canGoForward(boolean canGoForward) {
            this.canGoForward = canGoForward;
            return this;
        }

        public SecureSlide.Builder canGoBackward(boolean canGoBackward) {
            this.canGoBackward = canGoBackward;
            return this;
        }

        public SecureSlide build() {
            if(this.background != 0 && this.fragment != null) {
                return new SecureSlide(this);
            } else {
                throw new IllegalArgumentException("You must set at least a fragment and background.");
            }
        }
    }
}
