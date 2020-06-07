package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.common.LinkKnownOnNextObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.dialog.WaitingDialog;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseCustomTagFragment extends Fragment {

    private Context mContext;
    private Handler handler = new Handler();
    private WaitingDialog waitingDialog;

    @BindView(R.id.customTagLayout)
    public LinearLayout customTagLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    // 更多
    @BindView(R.id.moreView)
    public TextView moreView;
    // 头部标题
    @BindView(R.id.customTagName)
    public TextView customTagName;

    @BindView(R.id.refreshNextLayout)
    public LinearLayout refreshNextLayout;
    // 换一拨推荐
    @BindView(R.id.refreshNextBtn)
    public ImageView refreshNextBtn;
    // 分页信息
    private Paginator paginator;

    private BaseQuickAdapter baseQuickAdapter;
    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();

    private String custom_tag;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_custom_tag_course, container, false);
        mContext = getContext();
        ButterKnife.bind(this, rootView);

        custom_tag = getArguments().getString("custom_tag", "");

        waitingDialog = new WaitingDialog();
        initView();
        initData();

        return rootView;
    }

    private void registerListener () {
        moreView.setOnClickListener(v -> ToastUtil.showText(mContext, "点击了更多"));
        refreshNextLayout.setOnClickListener(v -> {
            waitingDialog.showDialog(this);
            // 动画效果
            // 属性动画属性值不变,动画不会再次执行,所以此处不能写 360,得写变化值
            refreshNextBtn.animate().rotation(refreshNextBtn.getRotation() + 360).setInterpolator(new LinearInterpolator()).setDuration(1000).start();

            // 转圈转完再加载下一页
            handler.postDelayed(() -> {
                // 加载下一页,最后一页的话加载第一页
                loadPageData(CommonUtil.isLastPage(paginator) ? 1 : paginator.getCurrpage() + 1);
            }, 1000);

        });
    }

    private void initView() {
        registerListener();

        baseQuickAdapter = new BaseQuickAdapter<CourseMetaResponse.CourseMeta, BaseViewHolder>(R.layout.item_custom_tag_course, courseMetaList) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, CourseMetaResponse.CourseMeta courseMeta) {
                UIUtils.setImage(mContext, viewHolder.findView(R.id.courseImageView), courseMeta.getSmall_image(), 20);
                viewHolder.setText(R.id.courseNameView, courseMeta.getCourse_name());
                viewHolder.setText(R.id.userNameText, courseMeta.getCourse_author());
                // 设置顶部标题
                customTagName.setText(courseMeta.getCustom_tag_name());
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(baseQuickAdapter);
    }


    protected void initData() {
        loadPageData(1);
    }

    private void loadPageData(int current_page) {
        // 每次最多显示 6 条
        LinkKnownApiFactory.getLinkKnownApi().queryCustomTagCourse(custom_tag, current_page, 6)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess() && CollectionUtils.isNotEmpty(courseMetaResponse.getCourses())) {
                            customTagLayout.setVisibility(View.VISIBLE);
                            courseMetaList.clear();
                            courseMetaList.addAll(courseMetaResponse.getCourses());
                            baseQuickAdapter.setList(courseMetaList);
                            paginator = courseMetaResponse.getPaginator();
                        } else {
                            customTagLayout.setVisibility(View.GONE);
                        }
                        waitingDialog.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        customTagLayout.setVisibility(View.GONE);
                        waitingDialog.dismissDialog();
                    }
                });
    }
}
