package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebChromeClient;
import com.linkknown.ilearning.BuildConfig;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.h5bridge.AndroidInterfaceForJS;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HuodongActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.huodongLayout)
    LinearLayout huodongLayout;

    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong);

        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView () {
        initAgentWebView();
    }

    private void initAgentWebView() {
        //传入Activity or Fragment
        mAgentWeb = AgentWeb.with(this)
                //传入AgentWeb 的父控件,如果父控件为 RelativeLayout,那么第二参数需要传入 RelativeLayout.LayoutParams,第一个参数和第二个参数应该对应
                .setAgentWebParent((LinearLayout) huodongLayout, new LinearLayout.LayoutParams(-1, -1))
                // 关闭进度条
                .closeIndicator()
                // 使用默认进度条颜色
                .createAgentWeb()
                .ready()
                .go(BuildConfig.FRONT_URL + "/isoft_webapp_ui/huodong/luckywheel");

        //注入对象
        // 会将该对象 application_android 绑定到 window 上面
        mAgentWeb.getJsInterfaceHolder().addJavaObject("application_android",new AndroidInterfaceForJS(mAgentWeb,this.getApplicationContext()));
        IAgentWebSettings agentWebSettings = mAgentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);

        //获取网页的标题
        mAgentWeb.getWebCreator().getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)) {
                    // 设置 toolbar
                    initToolBar(toolbar, true, title);
                }
                super.onReceivedTitle(view, title);
            }
        });

        WebView webView = mAgentWeb.getWebCreator().getWebView();
    }

    // 处理 webView 的键盘事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

