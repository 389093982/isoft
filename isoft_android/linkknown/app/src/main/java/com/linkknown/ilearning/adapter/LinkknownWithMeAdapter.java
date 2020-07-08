package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.activity.PayOrderCommitActivity;
import com.linkknown.ilearning.activity.PayOrderDetailActivity;
import com.linkknown.ilearning.activity.PersonalCenterActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.UserLinkAgentResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LinkknownWithMeAdapter extends BaseQuickAdapter<UserLinkAgentResponse.UserLinkAgent, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public LinkknownWithMeAdapter(Context mContext, List<UserLinkAgentResponse.UserLinkAgent> userLinkAgent) {
        super(R.layout.item_user_link_agent, userLinkAgent);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, UserLinkAgentResponse.UserLinkAgent userLinkAgent) {
        //头像
        UIUtils.setImage(mContext, viewHolder.findView(R.id.headerIcon), userLinkAgent.getSmall_icon());
        viewHolder.findView(R.id.headerIcon).setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME,userLinkAgent.getUser_name());
            return intent;
        }));
        //昵称
        viewHolder.setText(R.id.userNameText,userLinkAgent.getNick_name());
        viewHolder.findView(R.id.userNameText).setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME,userLinkAgent.getUser_name());
            return intent;
        }));
        //性别
        String gender = userLinkAgent.getGender();
        if ("male".equals(userLinkAgent.getGender())){
            viewHolder.setVisible(R.id.genderIcon_male,true);
            viewHolder.setGone(R.id.genderIcon_female,true);
        }else{
            viewHolder.setVisible(R.id.genderIcon_female,true);
            viewHolder.setGone(R.id.genderIcon_male,true);
        }

        //绑定时间
        viewHolder.setText(R.id.bind_time,"关联时间:"+DateUtil.formatDate_StandardForm(userLinkAgent.getBind_time()));

        //回报率
        BigDecimal res = new BigDecimal(userLinkAgent.getReturn_rate()).multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_HALF_UP);
        viewHolder.setText(R.id.return_rate,"回报率:"+res+"%");
        //个性签名
        viewHolder.setText(R.id.userSignature,userLinkAgent.getUser_signature());

    }

}
