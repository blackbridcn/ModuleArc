package org.recyclerview.wrapper.empty;

/**
 * File: PullToRefreshListener.java
 * Author: yuzhuzhang
 * Create: 2020-02-25 21:08
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-25 : Create PullToRefreshListener.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public  interface PullToRefreshListener {
    void onRefresh();
    void onLoadMore();
}