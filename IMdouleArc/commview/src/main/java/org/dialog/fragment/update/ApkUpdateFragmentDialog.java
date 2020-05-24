package org.dialog.fragment.update;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;

/**
 * File: ApkUpdateFragmentDialog.java
 * Author: yuzhuzhang
 * Create: 2020-02-08 02:49
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-08 : Create ApkUpdateFragmentDialog.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ApkUpdateFragmentDialog extends BaseDialogFragment {

    private ApkUpdateFragmentDialog() {
    }

    public static ApkUpdateFragmentDialog newInstance() {
        ApkUpdateFragmentDialog dialog = new ApkUpdateFragmentDialog();
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View view, AlertDialog.Builder builder) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_apk_update_layout;
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
    }
}
