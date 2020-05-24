package org.dialog.fragment.permiss;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;
import org.utils.ViewUtils;


/**
 * Author: yuzzha
 * Date: 2019/5/16 9:36
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class RewardDialogFragment extends BaseDialogFragment {

    public static RewardDialogFragment newInstance() {
        RewardDialogFragment permissionDialog = new RewardDialogFragment();
        permissionDialog.setCancelable(false);
        return permissionDialog;
    }

    protected TextView positive, negative, dialogTitle;
    protected RecyclerView rvLayout;

    private String dialog;

    public void setDialogTitle(String dialogTitle) {
        dialog = dialogTitle;
    }

    @Override
    protected void initWindowParams(Window window) {
        super.initWindowParams(window);
        window.setLayout(dm.widthPixels, window.getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_list_permission_layout;
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View rootView, AlertDialog.Builder builder) {
        LinearLayout rootLayout = rootView.findViewById(R.id.ll_rootlayout);
        ViewGroup.LayoutParams layoutParams = rootLayout.getLayoutParams();
        int width = ViewUtils.getScreenWidth(getContext());
        layoutParams.width = width * 4/ 5;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rootLayout.setLayoutParams(layoutParams);

        positive = rootView.findViewById(R.id.tv_positive);
        dialogTitle = rootView.findViewById(R.id.tv_dialogtitle);
        dialogTitle.setText(dialog);
        positive.setOnClickListener((view) -> {
            if (this.mOnPositiveClickListener != null)
                this.mOnPositiveClickListener.onPositiveClick(view);
        });
        negative = rootView.findViewById(R.id.tv_negative);
        negative.setOnClickListener((view) -> {
            if (this.mOnNegativeClickListener != null)
                this.mOnNegativeClickListener.onNegativeClick(view);
        });
        rvLayout = rootView.findViewById(R.id.recyclerview);
        rvLayout.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOnNegativeClickListener = null;
        mOnPositiveClickListener = null;
    }

    public interface OnNegativeClickListener {
        void onNegativeClick(View view);
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(View view);
    }

    private OnNegativeClickListener mOnNegativeClickListener;
    private OnPositiveClickListener mOnPositiveClickListener;

    public void setPositiveClickListener(OnPositiveClickListener mOnPositiveClickListener) {
        this.mOnPositiveClickListener = mOnPositiveClickListener;
    }

    public void setOnNegativeClickListener(OnNegativeClickListener mOnNegativeClickListener) {
        this.mOnNegativeClickListener = mOnNegativeClickListener;
    }

}

