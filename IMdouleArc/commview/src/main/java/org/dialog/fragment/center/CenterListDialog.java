package org.dialog.fragment.center;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;
import org.jetbrains.annotations.NotNull;
import org.recyclerview.wrapper.adapter.CommonAdapter;
import org.recyclerview.wrapper.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * File: CenterListDialog.java
 * Author: yuzhuzhang
 * Create: 2020-02-09 13:23
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-09 : Create CenterListDialog.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class CenterListDialog extends BaseDialogFragment {

    private String mTitle;
    private int resTitle = -1;
    TextView mTitleView;
    RecyclerView mListView;

    private List<String> data = new ArrayList<>();

    private CenterAdapter adapter;

    @Override
    public void displayDialog(Bundle savedInstanceState, View rootView, AlertDialog.Builder builder) {
        mTitleView = rootView.findViewById(R.id.title_text);
        mListView = rootView.findViewById(R.id.recyclerview);
        mListView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        adapter = new CenterAdapter(rootView.getContext(), data);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, holder, o, position) -> {
            if (listener != null) listener.OnItemClick(position);
        });
        if (this.mTitle != null && mTitle.length() > 0) {
            mTitleView.setText(this.mTitle);
        } else if (this.resTitle != -1) mTitleView.setText(this.resTitle);
    }


    public void setItemData(List<String> data) {
        if (data != null && data.size() > 0) {
            this.data.clear();
            this.data.addAll(data);
            this.adapter.notifyDataSetChanged();
        }
    }

    private void setTitleText(@NotNull String mTitle) {
        this.mTitle = mTitle;
    }

    private void setTitleText(@StringRes int resTitle) {
        this.resTitle = resTitle;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_title_list_layout;
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected void initWindowParams(Window window) {
        super.initWindowParams(window);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog);

        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            //params.dimAmount = mDimAmount;

            //设置dialog显示位置
         /*   if (mShowBottomEnable) {
                params.gravity = Gravity.BOTTOM;
            }*/

            //设置dialog宽度
            if (mWidth == 0) {
                params.width = getScreenWidth(getContext()) - 2 * dp2px(getContext(), mMargin);
            } else {
                params.width = dp2px(getContext(), mWidth);
            }

            //设置dialog高度
            if (mHeight == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = dp2px(getContext(), mHeight);
            }

            //设置dialog动画
            if (mAnimStyle != 0) {
                window.setWindowAnimations(mAnimStyle);
            }

            window.setAttributes(params);
        }
        //window.setLayout(dm.widthPixels, window.getAttributes().height);
        // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    class CenterAdapter extends CommonAdapter<String> {

        TextView itemText;

        public CenterAdapter(Context context, List<String> data) {
            super(context, R.layout.comm_view_dialog_item_center_textview_layout);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            itemText = (TextView) holder.getConvertView();
            itemText.setText(s);
        }
    }
}
