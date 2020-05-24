package org.dropview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yuzzha
 * Date: 2019-08-08 14:59
 * Description: ${DESCRIPTION}
 * Remark:
 * 备注：其他参考 https://github.com/jaredrummler/MaterialSpinner.git
 */
public class DropDownListEditTextView extends LinearLayout {
    private EditText editText;
    private TextView tViewContent;

    private LinearLayout btn;
    private PopupWindow popupWindow = null;
    private OnItemClickListener onItemClickListener;

    private List<DropDownInfo> lists;

    public DropDownListEditTextView(Context context) {
        this(context, null);
    }

    public DropDownListEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DropDownListEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void initView() {
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);
        View view = layoutInflater.inflate(R.layout.comm_view_dropdownlist_edittext, this, true);
        editText = (EditText) view.findViewById(R.id.text);
        btn = (LinearLayout) view.findViewById(R.id.btn);
        tViewContent = (TextView) view.findViewById(R.id.tViewContent);
        this.setOnClickListener(v -> {
            if (popupWindow == null) {
                showPopWindow();
            } else
                closePopWindow();
        });
        lists = new ArrayList<>();
    }

    /**
     * 打开下拉列表弹窗
     */
    private void showPopWindow() {
        // 加载popupWindow的布局文件
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);
        View contentView = layoutInflater.inflate(R.layout.comm_view_dropdownlist_popupwindow, null, false);
        ListView listView = (ListView) contentView.findViewById(R.id.listView);

        listView.setAdapter(new DropDownListAdapter(getContext(), lists));
        popupWindow = new PopupWindow(contentView, this.getWidth(), LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(this);
    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
        popupWindow = null;
    }

    public void setInputType(int inputType) {
        editText.setInputType(inputType);
    }

    /**
     * 设置默认值
     */
    public void setDefaultValue(String text) {
        editText.setText(text);
    }

    /**
     * 设置默认值
     */
    public void setInputState(boolean state) {
        editText.setEnabled(state);
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setItemsDataKV(List<DropDownInfo> list) {
        lists.clear();
        lists.addAll(list);
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setItemsData(List<String> list) {
        lists.clear();
        DropDownInfo dropDownInfo;
        for (String s : list) {
            dropDownInfo = new DropDownInfo();
            dropDownInfo.setDisplayText(s);
            dropDownInfo.setValue(s);
            lists.add(dropDownInfo);
        }
    }

    public void setItemData(String[] data) {
        lists.clear();
        for (String s : data) {
            DropDownInfo dropDownInfo = new DropDownInfo();
            dropDownInfo.setDisplayText(s);
            dropDownInfo.setValue(s);
            lists.add(dropDownInfo);
        }
    }

    public String getContent() {
        return tViewContent.getText().toString();
    }

    public void setContent(String value) {
        tViewContent.setText(value);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 数据适配器
     *
     * @author caizhiming
     */
    class DropDownListAdapter extends BaseAdapter {

        Context mContext;
        List<DropDownInfo> mData;
        LayoutInflater inflater;

        public DropDownListAdapter(Context ctx, List<DropDownInfo> data) {
            mContext = ctx;
            mData = data;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // 自定义视图
            ListItemView listItemView;
            if (convertView == null) {
                // 获取list_item布局文件的视图
                convertView = inflater.inflate(R.layout.comm_view_dropdown_list_item, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv = (TextView) convertView.findViewById(R.id.tv);
                listItemView.layout = (LinearLayout) convertView.findViewById(R.id.layout_container);
                // 设置控件集到convertView
                convertView.setTag(listItemView);
            } else {
                listItemView = (ListItemView) convertView.getTag();
            }

            // 设置数据
            listItemView.tv.setText(mData.get(position).getDisplayText());
            final String text = mData.get(position).getDisplayText();
            final String value = mData.get(position).getValue();
            listItemView.layout.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                editText.setText(text);
                tViewContent.setText(value);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, text, value);
                }
                closePopWindow();
            });
            return convertView;
        }

    }

    private static class ListItemView {
        TextView tv;
        LinearLayout layout;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String text, String valuse);
    }
}
