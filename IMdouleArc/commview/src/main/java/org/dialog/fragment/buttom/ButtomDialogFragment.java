package org.dialog.fragment.buttom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.commview.R;

import org.dialog.fragment.BaseDialogFragment;

/**
 * Author: yuzzha
 * Date: 2019-08-27 17:56
 * Description:  屏幕底部弹框Dialog
 * Remark:
 */
public abstract class ButtomDialogFragment extends BaseDialogFragment {

    @Override
    protected void initWindowParams(Window window) {
        super.initWindowParams(window);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.windowAnimations= R.style.EnterExitAnimation;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
