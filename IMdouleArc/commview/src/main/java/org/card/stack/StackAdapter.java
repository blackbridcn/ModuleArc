package org.card.stack;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * File: StackAdapter.java
 * Author: yuzhuzhang
 * Create: 2020-02-10 13:17
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-10 : Create StackAdapter.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public abstract class StackAdapter<T> extends CardStackView.Adapter<CardStackView.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<T> mData;

    public StackAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList();
    }

    public void updateData(List<T> data) {
        this.setData(data);
        this.notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override
    public void onBindViewHolder(CardStackView.ViewHolder holder, int position) {
        T data = this.getItem(position);
        this.bindView(data, position, holder);
    }

    public abstract void bindView(T data, int position, CardStackView.ViewHolder holder);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(int position) {
        return this.mData.get(position);
    }

}
