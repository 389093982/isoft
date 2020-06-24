package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.PayOrderCommitActivity;
import com.linkknown.ilearning.activity.PersonalCenterActivity;
import com.linkknown.ilearning.activity.ShoppingCartActivity;
import com.linkknown.ilearning.activity.VideoPlayActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.common.LinkKnownOnNextObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.FavoriteCountResponse;
import com.linkknown.ilearning.model.IsFavoriteResponse;
import com.linkknown.ilearning.model.QueryIsAttentionResponse;
import com.linkknown.ilearning.model.ui.CourseOperate;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.DrawableUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.widget.CommonTagView;
import com.linkknown.ilearning.widget.CourseVideoView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;

public class CourseIntroduceFragment extends BaseLazyLoadFragment {

    private Context mContext;
    private CourseDetailResponse courseDetailResponse;

    public CourseIntroduceFragment(CourseDetailResponse courseDetailResponse) {
        this.courseDetailResponse = courseDetailResponse;
    }

    @BindView(R.id.courseOperateRecyclerView)
    public RecyclerView courseOperateRecyclerView;

    //课程名称
    @BindView(R.id.courseNameText)
    public TextView courseNameText;

    //播放次数
    @BindView(R.id.playNumberText)
    public TextView playNumberText;

    //课程集数
    @BindView(R.id.courseNumberText)
    public TextView courseNumberText;

    //价格
    @BindView(R.id.price)
    public TextView price;

    //老价格
    @BindView(R.id.old_price)
    public TextView old_price;

    //课程描述
    @BindView(R.id.courseShortDescText)
    public TextView courseShortDescText;

    //购物车
    @BindView(R.id.shoppingCart)
    public ImageView shoppingCart;

    //加入购物车按钮
    @BindView(R.id.addShoppingCart)
    public TextView addShoppingCart;

    // 购买按钮
    @BindView(R.id.buyView)
    public TextView buyView;

    // 课程操作菜单和适配器
    private List<CourseOperate> courseOperates;
    private BaseQuickAdapter courseOperateAdapter;

    //用户头像
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;

    //用户名称
    @BindView(R.id.userNameText)
    public TextView userNameText;

    //性别icon
    @BindView(R.id.genderIcon_male)
    public ImageView genderIcon_male;
    @BindView(R.id.genderIcon_female)
    public ImageView genderIcon_female;

    //关注 & 粉丝数量
    @BindView(R.id.attention_counts)
    public TextView attention_counts;
    @BindView(R.id.fensi_counts)
    public TextView fensi_counts;

    //用户个性签名
    @BindView(R.id.userSignature)
    public TextView userSignature;

    //关注按钮
    @BindView(R.id.attention_off)
    public TextView attention_off;
    @BindView(R.id.attention_on)
    public TextView attention_on;

    // 课程自定义标签语
    @BindView(R.id.courseTagView)
    public CommonTagView courseTagView;

    //分集视频
    @BindView(R.id.courseVideoView)
    public CourseVideoView courseVideoView;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void initData() {
        init();
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_course_introduce;
    }

    private void init () {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        List<CourseDetailResponse.CVideo> cVideos = courseDetailResponse.getCVideos();
        String course_label = courseDetailResponse.getCourse().getCourse_label();

        // 操作区域初始化并刷新刷新
        initCourseOperateArea();
        refreshCourseOperateArea(course);

        // 设置课程名称
        courseNameText.setText(course.getCourse_name());
        // 设置播放次数
        playNumberText.setText(course.getWatch_number() + "");
        // 课程集数
        courseNumberText.setText(course.getCourse_number() + "");
        //课程价格
        if ("charge".equals(course.getIsCharge())){
            price.setText(Constants.RMB+course.getPrice());
            price.setVisibility(View.VISIBLE);
        }else{
            price.setVisibility(View.GONE);
        }
        //设置划线价
        if ("Y".equals(course.getIs_show_old_price())){
            old_price.setVisibility(View.VISIBLE);
            old_price.setText(Constants.RMB+course.getOld_price());
            old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
        }else{
            old_price.setVisibility(View.GONE);
        }

        // 课程描述
        courseShortDescText.setText(course.getCourse_short_desc());

        //设置用户名称
        userNameText.setText(courseDetailResponse.getUser().getNick_name());
        UIUtils.setImage(mContext, headerIcon, courseDetailResponse.getUser().getSmall_icon());
        //设置性别
        if ("male".equals(courseDetailResponse.getUser().getGender())){
            genderIcon_male.setVisibility(View.VISIBLE);
            genderIcon_female.setVisibility(View.GONE);
        }else{
            genderIcon_male.setVisibility(View.GONE);
            genderIcon_female.setVisibility(View.VISIBLE);
        }
        //设置关注和粉丝数量
        attention_counts.setText(courseDetailResponse.getUser().getAttention_counts()==0?"关注:0":"关注:"+courseDetailResponse.getUser().getAttention_counts());
        fensi_counts.setText(courseDetailResponse.getUser().getFensi_counts()==0?"粉丝:0":"粉丝:"+courseDetailResponse.getUser().getFensi_counts());
        //个性签名
        userSignature.setText(courseDetailResponse.getUser().getUser_signature());

        // 初始化自定义标签语
        courseTagView.setList(CommonUtil.splitCommonTag(course_label));

        initCVideoView(courseDetailResponse, course, cVideos);

        // 初始化 加入购物车 按钮 && 初始化购买按钮
        initAddShoppingCart_And_BuyView(courseDetailResponse);

        //关注按钮的展示与否
        attentionBtnShow();

        //关注按钮的事件绑定
        initAttentionBtn();
    }

    //关注按钮的展示与否
    public void attentionBtnShow(){
        //关注按钮的显示与隐藏
        if (LoginUtil.isLoginUserName(mContext, courseDetailResponse.getCourse().getCourse_author())){
            attention_off.setVisibility(View.GONE);
            attention_on.setVisibility(View.GONE);
        }else{
            LinkKnownApiFactory.getLinkKnownApi().QueryIsAttention("user",courseDetailResponse.getCourse().getCourse_author())
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<QueryIsAttentionResponse>() {
                        @Override
                        public void onNext(QueryIsAttentionResponse o) {
                            if (o.isSuccess() && o.getAttention_records() > 0){
                                //大于0 则表示已关注
                                attention_off.setVisibility(View.GONE);
                                attention_on.setVisibility(View.VISIBLE);
                            }else{
                                attention_off.setVisibility(View.VISIBLE);
                                attention_on.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {}
                    });
        }
    };

    //关注点击事件
    public void initAttentionBtn(){
        attention_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginUtil.checkHasLogin(mContext)){
                    LoginUtil.showLoginOrAutoLoginDialog(mContext);
                    return;
                }
                LinkKnownApiFactory.getLinkKnownApi().doAttention("user",courseDetailResponse.getCourse().getCourse_author(),"on")
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                            @Override
                            public void onNext(BaseResponse o) {
                                if (o.isSuccess()){
                                    ToastUtil.showText(mContext,"关注成功");
                                    attention_off.setVisibility(View.GONE);
                                    attention_on.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showText(mContext,"系统异常");
                            }
                        });
            }
        });
        attention_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginUtil.checkHasLogin(mContext)){
                    LoginUtil.showLoginOrAutoLoginDialog(mContext);
                    return;
                }
                LinkKnownApiFactory.getLinkKnownApi().doAttention("user",courseDetailResponse.getCourse().getCourse_author(),"off")
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                            @Override
                            public void onNext(BaseResponse o) {
                                if (o.isSuccess()){
                                    ToastUtil.showText(mContext,"取消成功");
                                    attention_off.setVisibility(View.VISIBLE);
                                    attention_on.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showText(mContext,"系统异常");
                            }
                        });
            }
        });
    };


    //初始化加入购物车 && 立即购买按钮
    private void initAddShoppingCart_And_BuyView(CourseDetailResponse courseDetailResponse) {
        //展示按钮的各种情况
        if (StringUtils.equalsIgnoreCase(courseDetailResponse.getCourse().getIsCharge(), "charge")) {
            if (LoginUtil.checkHasLogin(mContext)) {
                //已登录还要判断，是不是自己的课程，如果是则不展示按钮
                if (!LoginUtil.getLoginUserName(mContext).equals(courseDetailResponse.getCourse().getCourse_author())){
                    shoppingCart.setVisibility(View.VISIBLE);
                    addShoppingCart.setVisibility(View.VISIBLE);
                    buyView.setVisibility(View.VISIBLE);
                }else{
                    //是自己的课程
                    shoppingCart.setVisibility(View.GONE);
                    addShoppingCart.setVisibility(View.GONE);
                    buyView.setVisibility(View.GONE);
                }
            } else {
                shoppingCart.setVisibility(View.VISIBLE);
                addShoppingCart.setVisibility(View.VISIBLE);
                buyView.setVisibility(View.VISIBLE);
            }
        } else {
            shoppingCart.setVisibility(View.GONE);
            addShoppingCart.setVisibility(View.GONE);
            buyView.setVisibility(View.GONE);
        }

        //点击购物车，查看购物车
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(mContext, ShoppingCartActivity.class);
            }
        });

        //添加购物车
        addShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否已登录，否则提示去登录
                if (LoginUtil.checkHasLogin(mContext)){
                    //加入购物车
                    LinkKnownApiFactory.getLinkKnownApi().addToShoppingCart("course_theme_type",String.valueOf(courseDetailResponse.getCourse().getId()),courseDetailResponse.getCourse().getPrice())
                            .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                            .subscribe(new LinkKnownObserver<BaseResponse>() {
                                @Override
                                public void onNext(BaseResponse o) {
                                    if (o.isSuccess()){
                                        ToastUtil.showText(mContext,"添加成功！");
                                    }else{
                                        ToastUtil.showText(mContext,o.getErrorMsg());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtil.showText(mContext,e.getMessage());
                                }
                            });
                }else{
                    //提示去登录
                    LoginUtil.showLoginOrAutoLoginDialog(mContext);
                }

            }
        });

        //付款界面
        buyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否已登录，否则提示去登录
                if (LoginUtil.checkHasLogin(mContext)){
                    if (courseDetailResponse.getPayOrder() != null && "SUCCESS".equals(courseDetailResponse.getPayOrder().getPay_result())){
                        ToastUtil.showText(mContext,"该课程您已购买过，无需再次购买^_^");
                    }else{
                        //1.去结算页面
                        UIUtils.gotoActivity(mContext, PayOrderCommitActivity.class, intent -> {
                            intent.putExtra("goodsType","course_theme_type");
                            intent.putExtra("goodsId",String.valueOf(courseDetailResponse.getCourse().getId()));
                            intent.putExtra("goodsImg",courseDetailResponse.getCourse().getSmall_image());
                            intent.putExtra("goodsDesc",courseDetailResponse.getCourse().getCourse_name());
                            intent.putExtra("price",""+courseDetailResponse.getCourse().getPrice());
                            return intent;
                        });
                    }
                }else{
                    //提示去登录
                    LoginUtil.showLoginOrAutoLoginDialog(mContext);
                }

            }
        });

    }

    private void initCVideoView(CourseDetailResponse courseDetailResponse, CourseDetailResponse.Course course, List<CourseDetailResponse.CVideo> cVideos) {
        courseVideoView.setCallBackListener(new CourseVideoView.CallBackListener() {
            @Override
            public void onConfirm(int position) {
                CourseDetailResponse.CVideo cVideo = courseDetailResponse.getCVideos().get(0);
                UIUtils.gotoActivity(mContext, VideoPlayActivity.class, intent -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("courseDetailResponse", courseDetailResponse);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    return intent;
                });
            }
        });
        // 传递视频信息，并刷新页面
        courseVideoView.setList(courseDetailResponse, CourseDetailResponse.MultiItemTypeCVideo.setItemType(cVideos, CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST));
    }

    private void handleCourseOperateClick (String operateName) {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        switch (operateName) {
            case CourseOperate.OPERATE_SHOU_CANG:
                toggleFavorite(course, Constants.FAVORITE_TYPE_COURSE_COLLECT, "课程收藏成功！");
                break;
            case CourseOperate.OPERATE_PRIASE:
                toggleFavorite(course, Constants.FAVORITE_TYPE_COURSE_PRIASE, "课程点赞成功！");
                break;
            case CourseOperate.OPERATE_PLAY:
                List<CourseDetailResponse.CVideo> cVideos = courseDetailResponse.getCVideos();
                if (CollectionUtils.isNotEmpty(cVideos)) {
                    CourseDetailResponse.CVideo cVideo = cVideos.get(0);
                    UIUtils.gotoActivity(mContext, VideoPlayActivity.class, intent -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("courseDetailResponse", courseDetailResponse);
                        intent.putExtras(bundle);
                        return intent;
                    });
                } else {
                    ToastUtil.showText(mContext, Constants.COURSE_PLAY_NO_COURSE_NUM_TIP);
                }
                break;
            default:
                break;
        }
    }

    private void toggleFavorite(CourseDetailResponse.Course course, String favorite_type, String successTip) {
        LinkKnownApiFactory.getLinkKnownApi().toggleFavorite(course.getId(), favorite_type)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {

                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            ToastUtil.showText(mContext, successTip);
                            // 收藏成功后刷新收藏区域
                            refreshCourseOperateArea(course);
                        } else {
                            ToastUtil.showText(mContext, Constants.TIP_SYSTEM_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showText(mContext, Constants.TIP_SYSTEM_ERROR);
                    }
                });
    }

    private void refreshCourseOperateArea(CourseDetailResponse.Course course) {
        Observable.zip(
                LinkKnownApiFactory.getLinkKnownApi().queryFavoriteCount(course.getId(), Constants.FAVORITE_TYPE_COURSE_COLLECT),
                LinkKnownApiFactory.getLinkKnownApi().queryFavoriteCount(course.getId(), Constants.FAVORITE_TYPE_COURSE_PRIASE),
                // 是否收藏应该从请求头中获取用户名，不强制登录，没登录不发送请求
                LinkKnownApiFactory.getLinkKnownApi().isFavorite(LoginUtil.getLoginUserName(mContext), course.getId(), Constants.FAVORITE_TYPE_COURSE_COLLECT),
                LinkKnownApiFactory.getLinkKnownApi().isFavorite(LoginUtil.getLoginUserName(mContext), course.getId(), Constants.FAVORITE_TYPE_COURSE_PRIASE),
                new Function4<FavoriteCountResponse, FavoriteCountResponse, IsFavoriteResponse, IsFavoriteResponse, CourseOperate.CourseOperateResponseWrapper>() {
                    @Override
                    public CourseOperate.CourseOperateResponseWrapper
                    apply(FavoriteCountResponse collectFavoriteCountResponse, FavoriteCountResponse priaseFavoriteCountResponse,
                          IsFavoriteResponse collectIsFavoriteResponse, IsFavoriteResponse priaseIsFavoriteResponse) throws Exception {
                        // 将多个对象组装成一个对象
                        CourseOperate.CourseOperateResponseWrapper wrapper = new CourseOperate.CourseOperateResponseWrapper();
                        wrapper.setCollectFavoriteCountResponse(collectFavoriteCountResponse);
                        wrapper.setCollectIsFavoriteResponse(collectIsFavoriteResponse);
                        wrapper.setPriaseFavoriteCountResponse(priaseFavoriteCountResponse);
                        wrapper.setPriaseIsFavoriteResponse(priaseIsFavoriteResponse);
                        return wrapper;
                    }
                }
        ).subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownOnNextObserver<CourseOperate.CourseOperateResponseWrapper>() {
                    @Override
                    public void onNext(CourseOperate.CourseOperateResponseWrapper wrapper) {
                        // 课程播放次数
                        CourseOperate operatePlay = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_PLAY);
                        operatePlay.setOperateNum(courseDetailResponse.getCourse().getWatch_number());
                        // 课程是否收藏
                        if (wrapper.getCollectIsFavoriteResponse().isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_SHOU_CANG);
                            operate.setOperateStatus(wrapper.getCollectIsFavoriteResponse().isFavorite ? 1 : 0);
                        }
                        // 课程收藏数量
                        if (wrapper.getCollectFavoriteCountResponse().isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_SHOU_CANG);
                            operate.setOperateNum(wrapper.getCollectFavoriteCountResponse().getCounts());
                        }
                        // 课程是否点赞
                        if (wrapper.getPriaseIsFavoriteResponse().isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_PRIASE);
                            operate.setOperateStatus(wrapper.getPriaseIsFavoriteResponse().isFavorite ? 1 : 0);
                        }
                        // 课程收藏点赞
                        if (wrapper.getPriaseFavoriteCountResponse().isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_PRIASE);
                            operate.setOperateNum(wrapper.getPriaseFavoriteCountResponse().getCounts());
                        }
                        courseOperateAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initCourseOperateArea() {
        // 获取初始操作信息
        courseOperates = CourseOperate.getInitialCourseOperates();
        courseOperateAdapter = new BaseQuickAdapter<CourseOperate, BaseViewHolder> (R.layout.item_course_operate, courseOperates) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, CourseOperate operate) {
                // 设置操作名称
                viewHolder.setText(R.id.operateNameText, operate.getOperateName());
                // 主要是 tint 属性
                VectorDrawableCompat vectorDrawableCompat =
                        VectorDrawableCompat.create(getResources(), operate.getOperateIcon(), mContext.getTheme());
                // 颜色
                int color = operate.getOperateStatus() == 1 ?
                        ContextCompat.getColor(mContext, R.color.colorPrimary) : ContextCompat.getColor(mContext, R.color.gray);
                // 设置图标和图标颜色
                // 使用工具类解决着色共享状态的 bug
                viewHolder.setImageDrawable(R.id.operateIcon, DrawableUtil.tintDrawable(vectorDrawableCompat, color));
                viewHolder.setText(R.id.operateNum, operate.getOperateNum() + "");
                viewHolder.findView(R.id.operateIcon).setOnClickListener(v -> handleCourseOperateClick(operate.getOperateName()));
            }
        };


        courseOperateRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        courseOperateRecyclerView.setAdapter(courseOperateAdapter);
    }

    @OnClick(R.id.headerIcon)
    public void showUserDetail () {
        UIUtils.gotoActivity(mContext, PersonalCenterActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME, courseDetailResponse.getCourse().getCourse_author());
            return intent;
        });
    }

}

