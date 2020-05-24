package org.recyclerview.wrapper.empty;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * File: ViewHolder.java
 * Author: yuzhuzhang
 * Create: 2020-02-25 21:08
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-25 : Create ViewHolder.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private View convertView;
    private Context context;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        convertView = itemView;
        views = new SparseArray<>();
    }

    public static ViewHolder get(Context context, ViewGroup viewGroup, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        return new ViewHolder(context, itemView);
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public String getText(int viewId) {
        TextView tv = getView(viewId);
        return tv.getText().toString();
    }

    public ViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }


    public ViewHolder setViewVisiable(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public ViewHolder setViewBackgroundResource(int viewId, int resId) {
        getView(viewId).setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setOnclickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
