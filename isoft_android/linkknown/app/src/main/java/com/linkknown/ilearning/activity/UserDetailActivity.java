package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends BaseActivity {

    private String userName;
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;
    @BindView(R.id.nickNameText)
    public TextView nickNameText;
    @BindView(R.id.vipLevel)
    public ImageView vipLevel;
    @BindView(R.id.userSignature)
    public TextView userSignature;
    @BindView(R.id.genderView)
    public ImageView genderView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        userName = getIntent().getStringExtra(Constants.USER_NAME);

        ButterKnife.bind(this);

        initToolBar(toolbar, true, "");
        initView();
    }

    private void initView () {
        LinkKnownApiFactory.getLinkKnownApi().getUserDetail(userName)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {
                        if (userDetailResponse.isSuccess()){
                            UserDetailResponse.User user = userDetailResponse.getUser();
                            // 设置用户头像、用户昵称、用户会员等级、用户标签语
                            UIUtils.setImage(getApplication(), headerIcon, user.getSmall_icon());
                            nickNameText.setText(user.getNick_name());
                            vipLevel.setImageResource(UIUtils.getVipLevelImageResource(user.getVip_level()));
                            userSignature.setText(StringUtils.isNotEmpty(user.getUser_signature()) ? user.getUser_signature() : "该用户太懒，什么也没有留下~");
                            genderView.setImageResource(UIUtils.getGenderImageResource(user.getGender()));


                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onNext =>", "系统异常,请联系管理员~");
                        ToastUtil.showText(getApplication(), "系统异常,请联系管理员~");
                    }
                });
    }

}
