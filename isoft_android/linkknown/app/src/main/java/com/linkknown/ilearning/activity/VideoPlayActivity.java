package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.popup.HotSearchPopView;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.widget.CourseVideoView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseActivity {

    private Context mContext;
    @BindView(R.id.video_player)
    public JzvdStd player;

    @BindView(R.id.courseVideoView)
    public CourseVideoView courseVideoView;

    private List<CourseDetailResponse.CVideo> cVideos;
    private CourseDetailResponse courseDetailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mContext = this;
        ButterKnife.bind(this);

        Intent intent = getIntent();
        courseDetailResponse = (CourseDetailResponse)intent.getExtras().getSerializable("courseDetailResponse");
        int position = intent.getExtras().getInt("position");
        cVideos = courseDetailResponse.getCVideos();

        initVideoInfo();
        initVideoPlayer(position);
    }

    private void initVideoInfo() {
        courseVideoView.setCallBackListener(position -> {
            if (checkCanPlay(courseDetailResponse, position)) {
                initVideoPlayer(position);
            }
        });
        courseVideoView.setList(courseDetailResponse, CourseDetailResponse.MultiItemTypeCVideo.setItemType(cVideos, CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST));
    }

    // 判断视频是否可以播放
    private boolean checkCanPlay(CourseDetailResponse courseDetailResponse, int position) {
        // 收费课程需要进一步判断
        if (StringUtils.equalsIgnoreCase(courseDetailResponse.getCourse().getIsCharge(), "charge")) {
            // 收费课程前 N 集免费可看
            if (position + 1 <= courseDetailResponse.getCourse().getPreListFree()) {
                return true;
            } else {
                // 收费课程后续集需要购买
                // 登录判断
                if (LoginUtil.checkHasLogin(this)) {
                    // 已登录
                    if (courseDetailResponse.getPayOrder() != null) {
                        // 已购买
                        return true;
                    } else {
                        // 未购买，提示购买
                        new XPopup.Builder(this)
                                .hasShadowBg(true)
                                .asConfirm("温馨提示", "该视频为付费视频,请确认是否需要购买？", () -> {
                                    //1.去结算页面
                                    UIUtils.gotoActivity(mContext, PayOrderCommitActivity.class, intent -> {
                                        intent.putExtra("goodsType","course_theme_type");
                                        intent.putExtra("goodsId",courseDetailResponse.getCourse().getId());
                                        intent.putExtra("goodsImg",courseDetailResponse.getCourse().getSmall_image());
                                        intent.putExtra("goodsDesc",courseDetailResponse.getCourse().getCourse_name());
                                        intent.putExtra("price",""+courseDetailResponse.getCourse().getPrice());
                                        return intent;
                                    });
                                }).show();
                        return false;
                    }
                } else {
                    // 没登录,提示登录
                    new XPopup.Builder(this)
                            .hasShadowBg(true)
                            .asConfirm("温馨提示", "该视频为付费视频,请先进行登录？", () -> {
                                UIUtils.gotoActivity(this, LoginActivity.class);
                            }).show();
                    return false;
                }
            }
        }
        // 免费课程可以直接播放
        return true;
    }


    private void initVideoPlayer(int position) {

        UIUtils.setImage(this, player.posterImageView, courseDetailResponse.getCourse().getSmall_image());

        if (checkCanPlay(courseDetailResponse, position)) {
            CourseDetailResponse.CVideo cVideo = cVideos.get(position);
            player.setUp(UIUtils.replaceMediaUrl(cVideo.getFirst_play()), StringUtilEx.getFileNameNoEx(cVideo.getVideo_name()));

            player.titleTextView.setTextSize(14);
            player.titleTextView.setTextColor(UIUtils.getResourceColor(this, R.color.white));
            // 设置容器内播放器高,解决黑边（视频全屏）
            player.setVideoImageDisplayType(JzvdStd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);

            // 自动播放
            player.startVideo();
        }
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
