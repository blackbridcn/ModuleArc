package org.dialog.fragment.buttom;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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


/**
 * Author: yuzzha
 * Date: 2019-08-28 15:57
 * Description: 顶部Title 的 List DialogFragment
 * Remark:
 */
public class TitleListButtomDialog extends ButtomDialogFragment {

    @BindView(R2.id.title_text)
    TextView mTitleValue;

    //@BindView(R2.id.recyclerview)
   RecyclerView mLsitView;

    public static TitleListButtomDialog newInstance() {
        TitleListButtomDialog instance = new TitleListButtomDialog();
        return instance;
    }

    private List<String> listData;
    private String title;

    public void setRecyclerViewData(@NonNull List<String> listData, @NonNull String title) {
        this.listData = listData;
        this.title = title;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_view_dialog_title_list_layout;
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    @Override
    public void displayDialog(Bundle savedInstanceState, View view, AlertDialog.Builder builder) {
        if (listData == null) listData = new ArrayList<>();
        mLsitView=view.findViewById(R.id.recyclerview);
        if (listData.size() > 10)
            ViewUtils.setViewHeight(mLsitView, 0.6);
        mTitleValue.setText(this.title);
        TitleListAdapter adapter = new TitleListAdapter(view.getContext(), listData);
        adapter.setOnItemClickListener((view1, holder, str, position) -> {
            if (listener != null)
                listener.OnItemClick(position);
            dismissDialog();
        });
        mLsitView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLsitView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mLsitView.setAdapter(adapter);
    }

    class TitleListAdapter extends CommonAdapter<String> {

        public TitleListAdapter(Context context, List<String> datas) {
            super(context, R.layout.comm_view_dialog_buttom_list_item_layout, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            holder.setText(R.id.tv_item, s);
        }
    }

}
