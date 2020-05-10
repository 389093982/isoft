package com.linkknown.ilearning.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.api.LinkKnownApi;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.interceptor.TokenHeaderInterceptor;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.model.RegistResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Query;

// LiveData 在实体类里可以通知指定某个字段的数据更新
// MutableLiveData 则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
public class UserService {

    public static void regist (Context mContext, String username, String passwd, String nickname, String verifyCode, String third_user_type) {
        LinkKnownApiFactory.getLinkKnownApi().regist(username, passwd, nickname, verifyCode, third_user_type)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<RegistResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RegistResponse registResponse) {
                        if (registResponse.isSuccess()) {
                            LiveEventBus.get("registResponse", RegistResponse.class).post(registResponse);
                        } else {
                            Log.e("onNext =>", "regist failed！" + registResponse.getErrorMsg());
                            RegistResponse result = new RegistResponse();
                            result.setErrorMsg(registResponse.getErrorMsg());
                            LiveEventBus.get("registResponse", RegistResponse.class).post(result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        RegistResponse result = new RegistResponse();
                        result.setErrorMsg(mContext.getResources().getString(R.string.regist_failed));
                        LiveEventBus.get("registResponse", RegistResponse.class).post(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void logout (Context mContext) {
        TokenHeaderInterceptor.TOKEN_STRING.set("");
        LoginUserResponse result = new LoginUserResponse();
        result.setErrorMsg("用户未登录！");
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).post(result);
        UIUtils.gotoActivity(mContext, LoginActivity.class);
    }

    public static void login(Context mContext, String username, String passwd) {
        LinkKnownApiFactory.getLinkKnownApi().postLogin(username, passwd, "http://www.linkknown.com?index=helloworld")
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<LoginUserResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginUserResponse loginUserResponse) {
                        if (loginUserResponse.isSuccess()) {
                            // 存储登录后的 tokenString
                            TokenHeaderInterceptor.TOKEN_STRING.set(loginUserResponse.getTokenString());

                            LiveEventBus.get("loginUserResponse", LoginUserResponse.class).post(loginUserResponse);
                        } else {
                            Log.e("onNext =>", "login failed！" + loginUserResponse.getErrorMsg());
                            LoginUserResponse result = new LoginUserResponse();
                            result.setErrorMsg(loginUserResponse.getErrorMsg());
                            LiveEventBus.get("loginUserResponse", LoginUserResponse.class).post(result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        LoginUserResponse result = new LoginUserResponse();
                        result.setErrorMsg(mContext.getResources().getString(R.string.login_failed));
                        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).post(result);
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


}
