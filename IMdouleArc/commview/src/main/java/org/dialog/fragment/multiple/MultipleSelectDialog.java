package org.dialog.fragment.multiple;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;
import org.utils.ViewUtils;


/**
 * Author: yuzzha
 * Date: 2019-11-13 10:43
 * Description: 多选框
 * Remark:
 */
public abstract class MultipleSelectDialog extends BaseDialogFragment {

    protected TextView positive, dialogTitle, negative;
    protected RecyclerView mRecyclerView;
    protected int titleId, postiveId, negativeId;
    protected int width;

    @Override
    protected void initWindowParams(Window window) {
        super.initWindowParams(window);
        window.setLayout(dm.widthPixels, window.getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View rootView, AlertDialog.Builder builder) {
        ViewGroup rootLayout = rootView.findViewById(R.id.root_layout);
        ViewGroup.LayoutParams layoutParams = rootLayout.getLayoutParams();
        width = ViewUtils.getScreenWidth(getContext());
        layoutParams.width = width * 4 / 5;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        positive = rootView.findViewById(R.id.tv_positive);
        dialogTitle = rootView.findViewById(R.id.tv_title);
        dialogTitle.setText(titleId);
        positive.setOnClickListener((view) -> {
            if (this.positiveListener != null)
                this.positiveListener.OnPositive();
            dismissDialog();
        });
        negative = rootView.findViewById(R.id.tv_negative);
        negative.setOnClickListener((view) -> {
            if (this.negativeListener != null)
                this.negativeListener.OnNegative();
            dismissDialog();
        });
        mRecyclerView = rootView.findViewById(R.id.list_item);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_mulitip_select_layout;
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

}
