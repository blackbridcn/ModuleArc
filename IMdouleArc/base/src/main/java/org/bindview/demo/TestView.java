package org.bindview.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

public class TestView extends AppCompatButton {
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private ProxyTestListener linseners;

    public void setProxyTestLinsener(ProxyTestListener linsener) {
        this.linseners = linsener;
        setOnClickListener((view) -> {
            if (linseners != null) {
                String proxyTest = linseners.onProxyTest(this);
                Log.e("TAG", "setProxyTestLinsener: --------------->  proxyTest :"+proxyTest);
            }
        });
    }

    public interface ProxyTestListener {

        String onProxyTest(View view);
    }
}
