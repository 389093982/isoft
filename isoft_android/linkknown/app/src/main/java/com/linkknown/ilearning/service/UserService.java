package com.linkknown.ilearning.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.interceptor.HeaderInterceptor;
import com.linkknown.ilearning.model.CreateVerifyCodeResponse;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.model.RegistResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.CheckParamUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// LiveData 在实体类里可以通知指定某个字段的数据更新
// MutableLiveData 则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
public class UserService {

    public static void createVerifyCode(String userName) {
        LinkKnownApiFactory.getLinkKnownApi().createVerifyCode(userName)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CreateVerifyCodeResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CreateVerifyCodeResponse createVerifyCodeResponse) {
                        if (StringUtils.isEmpty(createVerifyCodeResponse.getErrorMsg())) {
                            LiveEventBus.get("createVerifyCodeResponse",  CreateVerifyCodeResponse.class).post(createVerifyCodeResponse);
                        } else {
                            Log.e("onError =>", createVerifyCodeResponse.getErrorMsg());
                            LiveEventBus.get("createVerifyCodeResponse",  CreateVerifyCodeResponse.class).post(createVerifyCodeResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        CreateVerifyCodeResponse result = new CreateVerifyCodeResponse();
                        result.setErrorMsg(e.getMessage());
                        LiveEventBus.get("createVerifyCodeResponse",  CreateVerifyCodeResponse.class).post(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void logout (Context mContext) {
        LoginUserResponse result = new LoginUserResponse();
        result.setErrorMsg("用户未登录！");
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).post(result);

        LoginUtil.logout(mContext);
       UIUtils.gotoActivity(mContext, LoginActivity.class);
    }

    public static void login(Context mContext, String username, String passwd) {
        LinkKnownApiFactory.getLinkKnownApi().postLogin(username, passwd, "http://www.linkknown.com?index=helloworld")
                .flatMap((Function<LoginUserResponse, ObservableSource<LoginUserResponse>>) loginUserResponse -> {
                    return Observable.zip(Observable.just(loginUserResponse),
                            LinkKnownApiFactory.getLinkKnownApi().getUserDetail(loginUserResponse.getUserName()),
                            (loginUserResponse1, userDetailResponse) -> {
                                loginUserResponse1.setUserDetailResponse(userDetailResponse);
                                return loginUserResponse1;
                            });
                })
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<LoginUserResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginUserResponse loginUserResponse) {
                        if (loginUserResponse.isSuccess()) {
                            LoginUtil.memoryAccount(mContext, username, passwd, loginUserResponse);
                            // 记录 token 信息
                            LoginUtil.memoryRefreshToken(mContext, loginUserResponse.getTokenString(), loginUserResponse.getExpireSecond());
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
        } else if (!CheckParamUtil.checkRegex(password, CheckParamUtil.REGEX_PASSWD)) {
            state.setPasswordError(R.string.invalid_password);
        } else {
            state.setDataValid(true);
            LiveEventBus.get("loginFormState", LoginFormState.class).post(state);
        }
    }

    public static boolean isUserNameValid(String username) {
        return StringUtils.isNotBlank(username) && (CheckParamUtil.checkRegex(username, CheckParamUtil.REGEX_EMAIL) || CheckParamUtil.checkRegex(username, CheckParamUtil.REGEX_PHONE));
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
