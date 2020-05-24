package org.dialog.alert;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.commview.R;


/**
 * File: AsynTaskDialog.java
 * Author: yuzhuzhang
 * Create: 2019-12-29 23:14
 * Description: TODO
 * -----------------------------------------------------------------
 * 2019-12-29 : Create AsynTaskDialog.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class AsynTaskDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private int mResid;

    public AsynTaskDialog(Context context) {
        this(context, 0);
    }

    public AsynTaskDialog(Context context, int id) {
        super(context);
        this.mResid = id;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mImageView.setBackgroundResource(mResid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(() -> mAnimation.start());
    }

    private void initView() {
        this.mResid = R.drawable.comm_view_net_dialog_frame;
        setContentView(R.layout.comm_view_ansy_dialog_progress_layout);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.0f;// 设置边框黑暗度为0
        window.setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawableResource(R.color.base_transport);
    }
}
