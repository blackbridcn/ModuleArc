package org.dialog.fragment.permiss;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.commview.R;

import org.recyclerview.wrapper.adapter.CommonAdapter;
import org.recyclerview.wrapper.base.ViewHolder;
import org.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yuzzha
 * Date: 2019-08-16 17:14
 * Description: 权限List DialogFragment
 * Remark:
 */
public class PermissionDialogFragment extends RewardDialogFragment {
    String postiveText, negativeText;

    public PermissionDialogFragment(String postive, String negative) {
        this.postiveText = postive;
        this.negativeText = negative;
    }

    public static PermissionDialogFragment newInstance(String postive, String negative) {
        PermissionDialogFragment permissionDialog = new PermissionDialogFragment(postive, negative);
        permissionDialog.setCancelable(false);
        return permissionDialog;
    }

    private List<PermissData> dataList = new ArrayList<>();
    private PermissionAdapter adapter;

    public void setPermissData(List<PermissData> data) {
        dataList.clear();
        dataList.addAll(data);
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View rootView, AlertDialog.Builder builder) {
        super.displayDialog(savedInstanceState, rootView, builder);
        positive.setText(postiveText);
        negative.setText(negativeText);
        adapter = new PermissionAdapter(rootView.getContext());
        if (dataList.size() > 5) {
            ViewGroup.LayoutParams layoutParams = rvLayout.getLayoutParams();
            int width = ViewUtils.getScreenWidth(getContext());
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;//width * 4/ 5;
            layoutParams.height = width;//ViewGroup.LayoutParams.MATCH_PARENT;
            rvLayout.setLayoutParams(layoutParams);
        }
        adapter.addDataAll(dataList);
        rvLayout.setAdapter(adapter);
    }

    class PermissionAdapter extends CommonAdapter<PermissData> {

        public PermissionAdapter(Context context) {
            super(context, R.layout.comm_view_permissin_item_layout);
        }

        @Override
        protected void convert(ViewHolder holder, PermissData permissData, int position) {
            holder.setImageResource(R.id.iv_icon, permissData.getResId());
            holder.setText(R.id.tv_title, permissData.getPermTitle());
            holder.setText(R.id.tv_sub, permissData.getPermSub());
        }
    }

    public static class PermissData {
        int resId;
        String permTitle;
        String permSub;

        public PermissData() {
        }

        public PermissData(int resId, String permTitle, String permSub) {
            this.resId = resId;
            this.permTitle = permTitle;
            this.permSub = permSub;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getPermTitle() {
            return permTitle;
        }

        public void setPermTitle(String permTitle) {
            this.permTitle = permTitle;
        }

        public String getPermSub() {
            return permSub;
        }

        public void setPermSub(String permSub) {
            this.permSub = permSub;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dataList.clear();
        dataList = null;
        adapter = null;
    }
}
