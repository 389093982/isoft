package com.linkknown.ilearning.activity.ui.login;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.LoginResponse;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginViewModel extends ViewModel {

    // LiveData 在实体类里可以通知指定某个字段的数据更新
    // MutableLiveData 则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String passwd) {
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

                            LoginResult lrt = new LoginResult();
                            lrt.setLoggedInUser(loggedInUser);
                            loginResult.setValue(lrt);
                        } else {
                            Log.e("onNext =>", "登录失败！");
                            LoginResult lrt = new LoginResult();
                            lrt.setErrorMsg(R.string.login_failed);
                            loginResult.setValue(lrt);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        LoginResult lrt = new LoginResult();
                        lrt.setErrorMsg(R.string.login_failed);
                        loginResult.setValue(lrt);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void validateLoginDataChanged(String username, String password) {
        LoginFormState state = new LoginFormState();
        if (!isUserNameValid(username)) {
            state.setUsernameError(R.string.invalid_username);
        } else if (!isPasswordValid(password)) {
            state.setPasswordError(R.string.invalid_password);
        } else {
            state.setDataValid(true);
            loginFormState.setValue(state);
        }
    }

    private boolean isUserNameValid(String username) {
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

    private boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;
        return StringUtils.isNotBlank(password);
    }




    // LiveData 管理的模型对象,用于双向绑定
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class LoginResult {
        @Nullable
        private LoggedInUserView loggedInUser;
        @Nullable
        private Integer errorMsg;           // 登录失败的错误提示,使用 R.string.xxx int 类型参数
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class LoginFormState {
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
    public class LoggedInUserView {
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
    }
}
