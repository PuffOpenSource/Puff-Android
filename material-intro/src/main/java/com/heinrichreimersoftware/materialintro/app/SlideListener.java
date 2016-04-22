package com.heinrichreimersoftware.materialintro.app;


/**
 * Created by bob.sun on 16/4/21.
 */
public interface SlideListener {
    void layoutNumber();
    void layoutAlphaOnly();
    void layoutAll();

    void addWordsSlide();
    void removeWordsSlide();

    void willLeaveSlide(int position);
}
