package org.skin.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.skin.action.IDynamicNewView;
import org.skin.action.SkinInflaterFactory;
import org.skin.attr.DynamicAttr;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 3/24/2020 5:18 PM
 * Description:
 * Remark:
 */
public class BaseSkinFragment extends Fragment implements IDynamicNewView {
    private IDynamicNewView mIDynamicNewView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mIDynamicNewView = (IDynamicNewView) context;
        } catch (ClassCastException e) {
            mIDynamicNewView = null;
        }
    }

    @Override
    public final void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        if (mIDynamicNewView == null) {
            throw new RuntimeException("IDynamicNewView should be implements !");
        } else {
            mIDynamicNewView.dynamicAddView(view, pDAttrs);
        }
    }

    @Override
    public final void dynamicAddView(View view, String attrName, int attrValueResId) {
        mIDynamicNewView.dynamicAddView(view, attrName, attrValueResId);
    }

    @Override
    public final void dynamicAddFontView(TextView textView) {
        mIDynamicNewView.dynamicAddFontView(textView);
    }

    public final SkinInflaterFactory getSkinInflaterFactory() {
        if (getActivity() instanceof BaseSkinActivity) {
            return ((BaseSkinActivity) getActivity()).getInflaterFactory();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        removeAllView(getView());
        super.onDestroyView();
    }

    protected void removeAllView(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                removeAllView(viewGroup.getChildAt(i));
            }
            removeViewInSkinInflaterFactory(v);
        } else {
            removeViewInSkinInflaterFactory(v);
        }
    }

    private void removeViewInSkinInflaterFactory(View v) {
        if (getSkinInflaterFactory() != null) {
            //此方法用于Activity中Fragment销毁的时候，移除Fragment中的View
            getSkinInflaterFactory().removeSkinView(v);
        }
    }
}
