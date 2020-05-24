package org.dialog.fragment.buttom;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commview.R;
import com.commview.R2;

import org.recyclerview.wrapper.adapter.CommonAdapter;
import org.recyclerview.wrapper.base.ViewHolder;
import org.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: yuzzha
 * Date: 2019-08-28 12:08
 * Description :  取消栏的 底部List DialogFragment
 * Remark:
 */
public class CancelListButtomDialog extends ButtomDialogFragment {

    @BindView(R2.id.recyclerview)
    RecyclerView rvLayout;

    private CancelListButtomDialog() {
    }

    public static CancelListButtomDialog newInstance() {
        CancelListButtomDialog permissionDialog = new CancelListButtomDialog();
        permissionDialog.setCancelable(true);
        return permissionDialog;
    }

    private List<String> listData;

    public void setRecyclerViewData(@NonNull List<String> listData) {
        this.listData = listData;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_cancel_buttom_list_layout;
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View view, AlertDialog.Builder builder) {
        if (listData == null) listData = new ArrayList<>();
        if (listData.size() > 10)
            ViewUtils.setViewHeight(rvLayout, 0.6);
        ButtomListAdapter adapter = new ButtomListAdapter(view.getContext(), listData);
        adapter.setOnItemClickListener((view1, holder, str, position) -> {
            if (listener != null)
                listener.OnItemClick(position);
            dismissDialog();
        });
        rvLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLayout.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvLayout.setAdapter(adapter);
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    @OnClick(R2.id.tv_cancel)
    public void onViewClicked() {
        dismissDialog();
    }

    class ButtomListAdapter extends CommonAdapter<String> {

        public ButtomListAdapter(Context context, List<String> itemData) {
            super(context, R.layout.comm_view_dialog_buttom_list_item_layout, itemData);
        }

        @Override
        protected void convert(ViewHolder holder, String str, int position) {
            holder.setText(R.id.tv_item, str);
        }
    }

}
