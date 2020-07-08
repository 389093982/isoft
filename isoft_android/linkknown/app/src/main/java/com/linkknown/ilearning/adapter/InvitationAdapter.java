package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.PersonalCenterActivity;
import com.linkknown.ilearning.model.UserLinkAgentResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class InvitationAdapter extends BaseQuickAdapter<UserLinkAgentResponse.UserLinkAgent, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    private ClickAcceptInvite clickAcceptInvite;

    public InvitationAdapter(Context mContext, List<UserLinkAgentResponse.UserLinkAgent> userLinkAgent) {
        super(R.layout.item_invitation, userLinkAgent);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, UserLinkAgentResponse.UserLinkAgent userLinkAgent) {
        //头像
        UIUtils.setImage(mContext, viewHolder.findView(R.id.headerIcon), userLinkAgent.getSmall_icon());
        viewHolder.findView(R.id.headerIcon).setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME,userLinkAgent.getAgent_user_name());
            return intent;
        }));
        //昵称
        viewHolder.setText(R.id.userNameText,userLinkAgent.getNick_name());
        viewHolder.findView(R.id.userNameText).setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME,userLinkAgent.getAgent_user_name());
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

        //账号
        viewHolder.setText(R.id.userName,userLinkAgent.getUser_name());
        //绑定时间
        viewHolder.setText(R.id.bind_time,"邀请时间:"+DateUtil.formatDate_StandardForm(userLinkAgent.getBind_time()));

        //接收邀请
        viewHolder.findView(R.id.accept_invite).setOnClickListener(v -> clickAcceptInvite.acceptInvite(userLinkAgent.getAgent_user_name()));
    }



    public interface ClickAcceptInvite{
        public void acceptInvite(String agent_user_name);
    };

    public void setClickAcceptInvite(ClickAcceptInvite clickAcceptInvite){
        this.clickAcceptInvite = clickAcceptInvite;
    }

}
