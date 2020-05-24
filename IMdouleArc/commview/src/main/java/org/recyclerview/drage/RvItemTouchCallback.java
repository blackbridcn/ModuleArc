package org.recyclerview.drage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: yuzzha
 * Date: 2019-07-19 10:18
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class RvItemTouchCallback extends ItemTouchHelper.Callback {

    private ItemTouchAdapter itemTouchAdapter;

    public RvItemTouchCallback(ItemTouchAdapter itemTouchAdapter) {
        this.itemTouchAdapter = itemTouchAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        itemTouchAdapter.onMove(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        itemTouchAdapter.onSwiped(position);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动时改变Item的透明度
            float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (background == null && bkcolor == -1) {
                Drawable drawable = viewHolder.itemView.getBackground();
                if (drawable == null) {
                    bkcolor = 0;
                } else background = drawable;
            }
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(1.0f);
        if (background != null) viewHolder.itemView.setBackgroundDrawable(background);
        if (bkcolor != -1) viewHolder.itemView.setBackgroundColor(bkcolor);
        if (onDragListener != null) {
            onDragListener.onFinishDrag();
        }
    }

    private Drawable background = null;
    private int bkcolor = -1;

    private OnDragListener onDragListener;

    public RvItemTouchCallback setOnDragListener() {
        this.onDragListener = onDragListener;
        return this;
    }

    public interface OnDragListener {
        void onFinishDrag();
    }

    public interface ItemTouchAdapter {
        void onMove(int fromPosition, int toPosition);

        void onSwiped(int position);
    }
}
