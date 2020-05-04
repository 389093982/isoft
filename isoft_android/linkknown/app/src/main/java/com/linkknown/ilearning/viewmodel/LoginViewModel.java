package com.linkknown.ilearning.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.interceptor.TokenHeaderInterceptor;
import com.linkknown.ilearning.model.LoginResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// LiveData 在实体类里可以通知指定某个字段的数据更新
// MutableLiveData 则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
public class LoginViewModel extends ViewModel {

    public static void logout (Context mContext) {
        TokenHeaderInterceptor.TOKEN_STRING.set("");
        LoginResult lrt = new LoginResult();
        lrt.setErrorMsg("用户未登录！");
        LiveEventBus.get("loginResult", LoginResult.class).post(lrt);
        UIUtils.gotoActivity(mContext, LoginActivity.class);
    }

    public static void login(Context mContext, String username, String passwd) {
        LinkKnownApiFactory.getLinkKnownService().postLogin(username, passwd, "http://www.linkknown.com?index=helloworld")
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<LoginResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        if (loginResponse.isSuccess()) {
                            LoggedInUserView loggedInUser = new LoggedInUserView();
                            // 对象转换
                            loggedInUser.setDomain(loginResponse.getDomain());
                            loggedInUser.setErrorMsg(loginResponse.getErrorMsg());
                            loggedInUser.setExpireSecond(loginResponse.getExpireSecond());
                            loggedInUser.setLoginStatus(loginResponse.getStatus());
                            loggedInUser.setNickName(loginResponse.getNickName());
                            loggedInUser.setLoginSuccess(loginResponse.getLoginSuccess());
                            loggedInUser.setRedirectUrl(loginResponse.getRedirectUrl());
                            loggedInUser.setRoleName(loginResponse.getRoleName());
                            loggedInUser.setTokenString(loginResponse.getTokenString());
                            loggedInUser.setUserName(loginResponse.getUserName());
                            loggedInUser.setHeaderIcon(loginResponse.getHeaderIcon());

                            // 存储登录后的 tokenString
                            TokenHeaderInterceptor.TOKEN_STRING.set(loginResponse.getTokenString());

                            LoginResult lrt = new LoginResult();
                            lrt.setLoggedInUser(loggedInUser);

                            LiveEventBus.get("loginResult", LoginResult.class).post(lrt);
                        } else {
                            Log.e("onNext =>", "login failed！" + loginResponse.getErrorMsg());
                            LoginResult lrt = new LoginResult();
                            lrt.setErrorMsg(loginResponse.getErrorMsg());
                            LiveEventBus.get("loginResult", LoginResult.class).post(lrt);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        LoginResult lrt = new LoginResult();
                        lrt.setErrorMsg(mContext.getResources().getString(R.string.login_failed));
                        LiveEventBus.get("loginResult", LoginResult.class).post(lrt);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void validateLoginDataChanged(String username, String password) {
        LoginFormState state = new LoginFormState();
        if (!isUserNameValid(username)) {
            state.setUsernameError(R.string.invalid_username);
        } else if (!isPasswordValid(password)) {
            state.setPasswordError(R.string.invalid_password);
        } else {
            state.setDataValid(true);
            LiveEventBus.get("loginFormState", LoginFormState.class).post(state);
        }
    }

    private static boolean isUserNameValid(String username) {
//        if (username == null) {
//            return false;
//        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
//            return !username.trim().isEmpty();
//        }
        return StringUtils.isNotBlank(username);
    }

    private static boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;
        return StringUtils.isNotBlank(password);
    }


    // LiveData 管理的模型对象,用于双向绑定
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResult {
        @Nullable
        private LoggedInUserView loggedInUser;
        @Nullable
        private String errorMsg;           // 登录失败的错误提示
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginFormState {
        @Nullable
        private Integer usernameError;
        @Nullable
        private Integer passwordError;
        private boolean isDataValid;
    }

    // 登录成功后的存储的用户信息
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoggedInUserView {
        private String domain;
        private String errorMsg;
        private String expireSecond;
        private String loginStatus;
        private String loginSuccess;
        private String nickName;
        private String redirectUrl;
        private String roleName;
        private String tokenString;
        private String userName;
        private String headerIcon;
    }
}
