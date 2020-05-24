package org.dialog.fragment.confirm;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;

/**
 * File: ConfirmDialogFragment.java
 * Author: yuzhuzhang
 * Create: 2020-02-02 20:28
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-02 : Create ConfirmDialogFragment.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ConfirmDialogFragment extends BaseDialogFragment {

    protected TextView positive, negative, dialogTitle,dialogContent;

    protected int titleId,contentId;
    String titleText,contentText;

    private int mMargin = 0;//左右边距

    private ConfirmDialogFragment() {
    }

    public static ConfirmDialogFragment newInstance() {
        ConfirmDialogFragment dialog = new ConfirmDialogFragment();
        dialog.setCancelable(true);
        return dialog;
    }

    public void setTitle(@StringRes int title) {
        this.titleId = title;
    }

    public void setTitle(@NonNull String title) {
        this.titleText = title;
    }


    public void setContent(@StringRes int content) {
        this.contentId = content;
    }

    public void setContent(@NonNull String content) {
        this.contentText = content;
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View rootView, AlertDialog.Builder builder) {
        dialogTitle = rootView.findViewById(R.id.title);
        dialogContent= rootView.findViewById(R.id.content);
        if (titleText != null && titleText.length() > 0) {
            dialogTitle.setText(titleText);
        } else
            dialogTitle.setText(titleId);
        if (contentText != null && contentText.length() > 0) {
            dialogContent.setText(contentText);
        } else
            dialogContent.setText(contentId);

        positive = rootView.findViewById(R.id.confirm);
        positive.setOnClickListener((view) -> {
            if (this.positiveListener != null)
                this.positiveListener.OnPositive();
            dismissDialog();
        });

        negative = rootView.findViewById(R.id.cancel);
        negative.setOnClickListener((view) -> {
            if (this.negativeListener != null)
                this.negativeListener.OnNegative();
            dismissDialog();
        });
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


    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_confirm_layout;
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return false;
    }
}
