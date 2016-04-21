package sun.bob.leela.listeners;

import sun.bob.leela.ui.fragments.SecureSlide;

/**
 * Created by bob.sun on 16/4/21.
 */
public interface SlideListener {
    void layoutNumber();
    void layoutAlphaOnly();
    void layoutAll();

    void addWordsSlide();
    void removeWordsSlide();
}
