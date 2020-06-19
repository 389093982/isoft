package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceViewHolder;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.AvailableCouponForPayActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.SetSmallIconActivity;
import com.linkknown.ilearning.activity.SetUserInfoActivity;
import com.linkknown.ilearning.activity.SetUserSignatureActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.FileUploadResponse;
import com.linkknown.ilearning.model.PayShoppinpCartResponse;
import com.linkknown.ilearning.model.SearchCouponForPayResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.CacheDataUtil;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingFragment extends PreferenceFragmentCompat {

    //头像
    private static final String smallIcon = "smallIcon";
    //用户信息
    private static final String userInfo = "userInfo";
    //个性签名
    private static final String userSignature = "userSignature";
    //清除缓存
    private static final String clearCache = "clearCache";
    // 清理缓存前
    private static final int clearCache_before = 0;
    // 清理缓存后
    private static final int clearCache_after = 1;
    //用户协议
    private static final String userAgreement = "userAgreement";
    //版本更新
    private static final String versionUpdate = "versionUpdate";
    //退出登录
    private static final String logout = "logout";

    private Context mContext;
    private Handler handler = new Handler();


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //设置基础页面
        setPreferencesFromResource(R.xml.preference_base, rootKey);
        mContext = getContext();

        //设置头像
        findPreference(smallIcon).setOnPreferenceClickListener(preference -> {
            setupSmallIcon();
            return true;
        });

        //设置用户信息
        findPreference(userInfo).setOnPreferenceClickListener(preference -> {
            setupUserDetail();
            return true;
        });

        //设置个性签名
        findPreference(userSignature).setOnPreferenceClickListener(preference -> {
            setupSignature();
            return true;
        });

        //设置缓存之前大小
        initCacheDataSize(clearCache_before);

        //开始清理缓存
        findPreference(clearCache).setOnPreferenceClickListener(preference -> {
            // 先清理重新检测存储空间
            CacheDataUtil.clearAllCache(mContext);
            initCacheDataSize(clearCache_after);
            return true;
        });

        //用户协议
        findPreference(userAgreement).setOnPreferenceClickListener(preference -> {
            ToastUtil.showText(mContext,"用户协议");
            return true;
        });

        //版本更新
        findPreference(versionUpdate).setOnPreferenceClickListener(preference -> {
            ToastUtil.showText(mContext,"版本更新");
            return true;
        });

        //退出登录
        findPreference(logout).setOnPreferenceClickListener(preference -> {
            logOut();
            return true;
        });



    }


    //设置头像
    public void setupSmallIcon(){
        LinkKnownApiFactory.getLinkKnownApi().getUserDetail(LoginUtil.getLoginUserName(mContext))
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {
                        if (userDetailResponse.isSuccess()){
                            UserDetailResponse.User user = userDetailResponse.getUser();
                            Intent intent = new Intent(mContext, SetSmallIconActivity.class);
                            //无需传参，头像可以通过工具类直接获取
                            startActivityForResult(intent,197);
                        }
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getUserDetail error", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                    }
                });

    };


    //设置用户基本信息，先做查询，把信息带到新页面
    public void setupUserDetail(){
        LinkKnownApiFactory.getLinkKnownApi().getUserDetail(LoginUtil.getLoginUserName(mContext))
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {
                        if (userDetailResponse.isSuccess()){
                            UserDetailResponse.User user = userDetailResponse.getUser();
                            Intent intent = new Intent(mContext, SetUserInfoActivity.class);
                            intent.putExtra("nick_name",user.getNick_name());
                            intent.putExtra("gender",user.getGender());
                            intent.putExtra("birthday", user.getBirthday());
                            intent.putExtra("current_residence",user.getCurrent_residence());
                            intent.putExtra("hometown",user.getHometown());
                            intent.putExtra("hat",user.getHat());
                            intent.putExtra("hat_in_use",user.getHat_in_use());
                            startActivityForResult(intent,198);
                        }
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getUserDetail error", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                    }
                });
    };


    //设置个性签名
    public void setupSignature(){
        LinkKnownApiFactory.getLinkKnownApi().getUserDetail(LoginUtil.getLoginUserName(mContext))
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {
                        if (userDetailResponse.isSuccess()){
                            String signature_text_old = userDetailResponse.getUser().getUser_signature();
                            Intent intent = new Intent(mContext, SetUserSignatureActivity.class);
                            intent.putExtra("signature_text_old",signature_text_old);
                            startActivityForResult(intent,199);
                        }
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getUserDetail error", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                    }
                });
    };



    //设置缓存大小
    private void initCacheDataSize (int pattern) {
        if (pattern == clearCache_before) {
            findPreference(clearCache).setSummary(String.format("已使用空间 %s", CacheDataUtil.getTotalCacheSize(mContext)));
        } else if (pattern == clearCache_after) {
            findPreference(clearCache).setSummary(String.format("清理完成，使用空间 %s", CacheDataUtil.getTotalCacheSize(mContext)));
        }
    }


    //退出登录
    private void logOut(){
        if (LoginUtil.checkHasLogin(mContext)) {
            LoginUtil.logout(mContext);
            UIUtils.showMessageForSnackBar(mContext, getView(), "账号退出成功！");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UIUtils.gotoActivity(mContext, LoginActivity.class);
                }
            }, 1000);
        }
    }


    /**
     * 为了从 “编辑个性签名” 回来 “本页面” 获取结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //更新头像
        if (requestCode == 197 && resultCode == 200) {
            Bundle bundle = data.getBundleExtra("bundle");
            String user_name = LoginUtil.getLoginUserName(mContext);
            String headFileUri =  (String) bundle.getSerializable("headFileUri");
            //根据路径生成Part
            File file = new File(headFileUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            //调用接口，传入part参数
            LinkKnownApiFactory.getLinkKnownApi().fileUpload("user","small_icon",filePart)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<FileUploadResponse>() {
                        @Override
                        public void onNext(FileUploadResponse o) {
                            if (o.isSuccess()){
                                String fileServerPath = o.getFileServerPath();

                                //获取了文件上传的地址， 这里开始真正更新user表， 更换头像
                                LinkKnownApiFactory.getLinkKnownApi().UpdateUserIcon(LoginUtil.getLoginUserName(mContext),fileServerPath)
                                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                                            @Override
                                            public void onNext(BaseResponse o) {
                                                if (o.isSuccess()){
                                                    ToastUtil.showText(mContext,"头像更新成功！");
                                                }
                                            };
                                            @Override
                                            public void onError(Throwable e) {
                                                Log.e("UpdateUserIcon error", e.getMessage());
                                                ToastUtil.showText(mContext,"头像更新失败！");
                                            }
                                        });
                            }else{
                                ToastUtil.showText(mContext,"上传失败");
                            }
                        };
                        @Override
                        public void onError(Throwable e) {
                            Log.e("fileUpload error", e.getMessage());
                            ToastUtil.showText(mContext,"上传文件失败！");
                        }
                    });

        }

        //更新用户信息
        if (requestCode == 198 && resultCode == 200) {
            Bundle bundle = data.getBundleExtra("bundle");
            String user_name = LoginUtil.getLoginUserName(mContext);
            String nick_name = (String) bundle.getSerializable("nick_name");
            String gender = (String) bundle.getSerializable("gender");
            String birthday = (Long) bundle.getSerializable("birthday") + "";
            String current_residence = (String) bundle.getSerializable("current_residence");
            String hometown = (String) bundle.getSerializable("hometown");
            String hat = (String) bundle.getSerializable("hat");
            String hat_in_use = (String) bundle.getSerializable("hat_in_use");
            LinkKnownApiFactory.getLinkKnownApi().UpdateUserDetail(user_name,nick_name,gender,birthday,current_residence,hometown,hat,hat_in_use)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse o) {
                            if (o.isSuccess()){
                                ToastUtil.showText(mContext,"更新成功");
                            }
                        };
                        @Override
                        public void onError(Throwable e) {
                            Log.e("EditUserSignature error", e.getMessage());
                            ToastUtil.showText(mContext,"更新个性签名失败！");
                        }
                    });

        }

        //设置个性签名
        if (requestCode == 199 && resultCode == 200) {
            Bundle bundle = data.getBundleExtra("bundle");
            String signature_text_new = (String) bundle.getSerializable("signature_text");
            LinkKnownApiFactory.getLinkKnownApi().EditUserSignature(signature_text_new)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse o) {
                            if (o.isSuccess()){
                                ToastUtil.showText(mContext,"更新成功");
                            }
                        };
                        @Override
                        public void onError(Throwable e) {
                            Log.e("EditUserSignature error", e.getMessage());
                            ToastUtil.showText(mContext,"更新个性签名失败！");
                        }
                    });

        }

    }


}
