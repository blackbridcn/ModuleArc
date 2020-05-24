package org.dialog.fragment.input;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;
import org.utils.ViewUtils;

/**
 * File: InputDialogFragment.java
 * Author: yuzhuzhang
 * Create: 2020-02-02 10:49
 * Description: 输入框
 * -----------------------------------------------------------------
 * 2020-02-02 : Create InputDialogFragment.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class InputDialogFragment extends BaseDialogFragment {

    protected TextView positive, negative, dialogTitle,dialogContent;

    protected EditText inputValue;

    protected int titleId,contentId;
    protected int width;

    private InputDialogFragment() {
    }

    public static InputDialogFragment newInstance() {
        InputDialogFragment dialog = new InputDialogFragment();
        dialog.setCancelable(true);
        return dialog;
    }

    public void setTitle(@StringRes int title) {
        this.titleId = title;
    }

    public void setContent(@StringRes int contentId) {
        this.contentId = contentId;
    }


    @Override
    public void displayDialog(Bundle savedInstanceState, View rootView, AlertDialog.Builder builder) {
        ViewGroup rootLayout = rootView.findViewById(R.id.root_layout);
        ViewGroup.LayoutParams layoutParams = rootLayout.getLayoutParams();
        width = ViewUtils.getScreenWidth(getContext());
        layoutParams.width = width * 4 / 5;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        positive = rootView.findViewById(R.id.tv_positive);
        inputValue = rootView.findViewById(R.id.input_value);
        dialogTitle = rootView.findViewById(R.id.title);
        dialogTitle.setText(titleId);
        dialogContent = rootView.findViewById(R.id.content);
       // dialogContent.setText(contentId);

        positive.setOnClickListener((view) -> {
            if (this.positiveListener != null)
                this.positiveListener.OnPositive(inputValue.getText().toString());
            dismissDialog();
        });
        negative = rootView.findViewById(R.id.tv_negative);
        negative.setOnClickListener((view) -> {
            if (this.negativeListener != null)
                this.negativeListener.OnNegative();
            dismissDialog();
        });
    }

    @Override
    protected void initWindowParams(Window window) {
        super.initWindowParams(window);
        window.setLayout(dm.widthPixels, window.getAttributes().height);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_input_layout;
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    /*--------------------------------------------------------------------------------------------*/

    protected OnInputPositiveListener positiveListener;

    public interface OnInputPositiveListener {
        void OnPositive(String value);
    }

    public void setOnInputPositiveListener(OnInputPositiveListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    /*--------------------------------------------------------------------------------------------*/

}
