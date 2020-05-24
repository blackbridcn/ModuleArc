package org.card.stack;

/**
 * File: ScrollDelegate.java
 * Author: yuzhuzhang
 * Create: 2020-02-10 13:13
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-10 : Create ScrollDelegate.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public interface ScrollDelegate {

    void scrollViewTo(int x, int y);
    void setViewScrollY(int y);
    void setViewScrollX(int x);
    int getViewScrollY();
    int getViewScrollX();


}
