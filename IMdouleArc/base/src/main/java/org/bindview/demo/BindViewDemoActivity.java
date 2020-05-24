package org.bindview.demo;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;


@SuppressLint("ResourceType")
//@ContentView(R.layout.base_bindview_demo_activity)
public class BindViewDemoActivity extends AppCompatActivity {

/*    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.tv_text)
    TextView text;

    @BindColor(R.color.base_blue)
    int colorPrimary;

    @BindColor(R.color.base_red)
    ColorStateList greenSelector;

    @BindView(R.id.iv_one)
    ImageView one;

    @BindView(R.id.iv_two)
    ImageView two;

    @BindDrawable(R.drawable.base_ic_launcher)
    Drawable placeholder;

    @BindDrawable(value = R.drawable.base_ic_launcher, tint = R.attr.colorControlNormal)
    Drawable tintedPlaceholder;

    @BindString(R.string.app_name)
    String usernameErrorText;

    @BindViews({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5})
    TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindEvent.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        one.setImageDrawable(placeholder);

        two.setImageDrawable(tintedPlaceholder);
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText("< - BindViews " + i + " - >");
        }
      btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.btn, R.id.iv_two})
    public void onViewClickeds(View view) {
        text.setTextColor(colorPrimary);
        btn.setBackgroundTintList(greenSelector);
        Log.e("TAG", "onViewClickeds:------------------> OnClick " + view.getId());
        Toast.makeText(this, "onViewClicked  " + usernameErrorText + view.getClass().getName(), Toast.LENGTH_LONG).show();
    }

    @OnLongClickListener(R.id.btn)
    public boolean OnLongClick(View view) {
        Log.e("TAG", "OnLongClick:------------------> OnLongClick " + view.getId());
        Toast.makeText(this, "OnLongClick  " + usernameErrorText + view.getClass().getName(), Toast.LENGTH_LONG).show();
        return  false;
    }

    @ProxyTest(R.id.testpro)
    public String  onProxyTests(View view ){
        Log.e("TAG", "onProxyTests:------------------> onProxyTests -----> " + view.getId());
        Toast.makeText(this, "onProxyTests  " + usernameErrorText + view.getClass().getName(), Toast.LENGTH_LONG).show();
        return  "onProxyTest : testpro";
    }*/


}
