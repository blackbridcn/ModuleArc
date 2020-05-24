package org.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Author: yuzzha
 * Date: 2019-07-02 15:31
 * Description: ${DESCRIPTION}
 * Remark:
 **/
public abstract class BaseDialogFragment extends DialogFragment {

    private boolean isShow = false;
    private Unbinder unbinder;
    protected DisplayMetrics dm;

    protected float mDimAmount = 0.5f;//背景昏暗度
    protected boolean mShowBottomEnable;//是否底部显示
    protected int mMargin = 0;//左右边距
    protected int mAnimStyle = 0;//进入退出动画

    protected int mWidth;
    protected int mHeight;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void showDialog(FragmentManager fragmentManager) {
        if (this.isAdded()) return;
        show(fragmentManager, this.getClass().getCanonicalName());
        setShow(true);
    }

    //onCreateDialog  -> onCreateView -> onStart
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(getLayoutId(), null);
        dm = new DisplayMetrics();
        unbinder = ButterKnife.bind(this, view);
        builder.setView(view);
        displayDialog(savedInstanceState, view, builder);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initWindowParams(getDialog().getWindow());
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 这里设置Dialog的显示位置
     *
     * @param window
     */
    protected void initWindowParams(Window window) {
    }

    /**
     * 显示弹窗信息
     *
     * @param savedInstanceState
     * @param builder
     */
    public abstract void displayDialog(Bundle savedInstanceState, View view, AlertDialog.Builder builder);

    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract boolean isCanceledOnTouchOutside();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void dismissDialog() {
        if (isShow)
            this.dismiss();
    }

    /*--------------------------------------------------------------------------------------------*/

    protected OnItemClickListener listener;
    protected OnNegativeListener negativeListener;
    protected OnPositiveListener positiveListener;

    public interface OnNegativeListener {
        void OnNegative();
    }

    public interface OnPositiveListener {
        void OnPositive();
    }

    public interface OnItemClickListener {
        void OnItemClick(int positon);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setNegativeListener(OnNegativeListener negativeListener) {
        this.negativeListener = negativeListener;
    }

    public void setPositiveListener(OnPositiveListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /*--------------------------------------------------------------------------------------------*/

}
