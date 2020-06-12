package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.linkknown.ilearning.R;

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

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) huodongLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("http://www.jd.com");

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

    }
}

