package org.recyclerview.wrapper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commview.R;

import org.recyclerview.wrapper.base.ItemViewDelegateManager;
import org.recyclerview.wrapper.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * File: EmptyAdapter.java
 * Author: yuzhuzhang
 * Create: 2020/3/1 8:23 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/1 : Create EmptyAdapter.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public abstract class EmptyAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private int mEmptyLayoutId;
    //private View mEmptyView;
    protected Context mContext;
    protected List<T> mDatas;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public int offset = 0;

    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;

    public EmptyAdapter(final Context context, final int layoutId) {
        this(context, layoutId, new ArrayList<T>());
    }

    public EmptyAdapter(final Context context, final int layoutId, List<T> datas) {
        this.mEmptyLayoutId = R.layout.comm_view_item_empty_layout;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    protected ItemViewDelegateManager mItemViewDelegateManager;

    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() > 0) {
            return VIEW_TYPE_ITEM;
        }
        return VIEW_TYPE_EMPTY;
    }


    @Override
    public int getItemCount() {
       int size= mDatas.size();
        if (size > 0) {
            return size;
        }
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, mLayoutId);
            setListener(parent, holder);
            return holder;
        } else { /*if (viewType == VIEW_TYPE_EMPTY)*/
            ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, mEmptyLayoutId);
            holder.getConvertView().setOnClickListener(v -> {
                if (this.emptyClickListener != null)
                    this.emptyClickListener.onEmptyClick();
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mDatas.size()>0)
            convertItemView(holder, mDatas.get(position), position);
    }


    protected abstract void convertItemView(ViewHolder holder, T t, int position);


    public interface OnEmptyClickListener<T> {
        void onEmptyClick();
    }

    private OnEmptyClickListener emptyClickListener;

    public void setEmptyClickListener(OnEmptyClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
    }

    protected MultiItemTypeAdapter.OnItemClickListener<T> mOnItemClickListener;
    protected MultiItemTypeAdapter.OnItemLongClickListener<T> onItemLongClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    public void setOnItemClickListener(MultiItemTypeAdapter.OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(MultiItemTypeAdapter.OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder) {
        //if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                mOnItemClickListener.onItemClick(v, viewHolder, mDatas.get(position - offset), position);
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                return onItemLongClickListener.onItemLongClick(v, viewHolder, mDatas.get(position - offset), position);
            }
            return false;
        });
    }

    public void addDataAll(List data) {
        mDatas.addAll(data);
    }

    public void clearData() {
        mDatas.clear();
    }
}
