package org.recyclerview.drage;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: yuzzha
 * Date: 2019-07-19 10:22
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public RecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (touchLinsener != null) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null)
                    touchLinsener.OnItemTouch(false, recyclerView.getChildViewHolder(child));
                //onItemClick(recyclerView.getChildViewHolder(child));
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (touchLinsener != null) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null)
                    touchLinsener.OnItemTouch(true, recyclerView.getChildViewHolder(child));
                //onLongClick(recyclerView.getChildViewHolder(child));
            }
        }
    }

    private OnItemTouchLinsener touchLinsener;

    public interface OnItemTouchLinsener {
        void OnItemTouch(boolean isLongClick, RecyclerView.ViewHolder mViewHolder);
    }

    public void setTouchLinsener(OnItemTouchLinsener touchLinsener) {
        this.touchLinsener = touchLinsener;
    }
}
