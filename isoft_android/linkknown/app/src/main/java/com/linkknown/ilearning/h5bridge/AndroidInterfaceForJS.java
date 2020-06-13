package com.linkknown.ilearning.h5bridge;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.just.agentweb.AgentWeb;
import com.linkknown.ilearning.util.LoginUtil;

public class AndroidInterfaceForJS {

    private AgentWeb agent;
    private Context context;

    public AndroidInterfaceForJS(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }

    @JavascriptInterface
    public String getLoginNickName() {
        return LoginUtil.getLoginNickName(context);
    }

    @JavascriptInterface
    public boolean checkHasLogin() {
        return LoginUtil.checkHasLogin(context);
    }

}
