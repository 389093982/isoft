package com.linkknown.ilearning.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.service.ShowTypeDetailService;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

// MultiTypeAdapter
public class ShowTypeDetailFragment extends Fragment implements View.OnClickListener {

    // 下拉刷新布局
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private boolean mIsRefreshing = false;

    private MultiTypeAdapter multiTypeAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type_detail, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        // 初始化组件
        init();

        // 绑定 adapter
        bindAdapter();

        bindAdapterData();

        return rootView;
    }

    private void bindAdapterData () {
        LiveEventBus.get("showTypeDetails", List.class).observe(this, list -> {
            multiTypeAdapter.setItems(list);
            multiTypeAdapter.notifyDataSetChanged();
            finishRefreshing();
        });
    }

    private void bindAdapter () {
        multiTypeAdapter = new MultiTypeAdapter();
        // 注册多块内容
        multiTypeAdapter.register(ShowTypeDetailService.BannerEntityWrapper.class, getMultiItemViewForBanner());
        multiTypeAdapter.register(ShowTypeDetailService.HotClassify.class, getMultiItemViewForHotClassify());
        multiTypeAdapter.register(ShowTypeDetailService.HotClassify2.class, getMultiItemViewForHotRecommend());

        // 网格布局, 每行最多容量 2 个子 View
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(getSpanSizeLookup(multiTypeAdapter));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(multiTypeAdapter);
    }

    private GridLayoutManager.SpanSizeLookup getSpanSizeLookup(MultiTypeAdapter multiTypeAdapter) {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (multiTypeAdapter.getItemViewType(position)) {
                    case 0:
                        // 将子视图的SpanSize都设置为2，那么这个子视图将占整个RecyclerView可用宽度
                        return 2;
                    case 1:
                        // 将子视图的SpanSize都设置为2，那么这个子视图将占整个RecyclerView可用宽度
                        return 2;
                    default:
                        return 1;
                }
            }
        };
    }

    private void init() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        // 通过post(Runnable runnable)方法放到UI线程排队执行
        recyclerView.post(() -> {
            // 显示刷新进度条，通过直接设置setRefreshing(true)
            refreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            clearAndLoadData();
        });

        // refreshLayout 设置刷新监听
        refreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            clearAndLoadData();
        });
    }


    @Override
    public void onClick(View v) {

    }

    protected void finishRefreshing() {
        mIsRefreshing = false;
        refreshLayout.setRefreshing(false);
    }



    private void clearAndLoadData() {
        ShowTypeDetailService.loadData();
    }

    private MultiItemView getMultiItemViewForHotRecommend () {
        MultiItemView multiItemView = new MultiItemView<ShowTypeDetailService.HotClassify2>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.layout_region_recommend_card_item;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShowTypeDetailService.HotClassify2 hotClassify, int position) {
                ImageView imageView = viewHolder.getConvertView().findViewById(R.id.item_img);
                imageView.setImageResource(hotClassify.getClassifyImage());
                viewHolder.setText(R.id.item_title, hotClassify.getClassifyName());
                viewHolder.setText(R.id.item_review, "888");
                viewHolder.setText(R.id.item_title, "888");

                viewHolder.setText(R.id.item_title, hotClassify.getClassifyName());
                viewHolder.setText(R.id.item_title, hotClassify.getClassifyName());
            }
        };
        return multiItemView;
    }

    private MultiItemView getMultiItemViewForHotClassify () {
        MultiItemView multiItemView = new MultiItemView<ShowTypeDetailService.HotClassify>(){
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.layout_recycleview;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShowTypeDetailService.HotClassify hotClassify, int position) {
                RecyclerView recyclerView = viewHolder.getConvertView().findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 8));

                MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
                // 注册多块内容
                multiTypeAdapter.register(ShowTypeDetailService.HotClassify.class, getMultiItemViewForHotClassify2());
                multiTypeAdapter.setItems(Arrays.asList(hotClassify,hotClassify,hotClassify,hotClassify,hotClassify,hotClassify,hotClassify));
                recyclerView.setAdapter(multiTypeAdapter);
            }
        };
        return multiItemView;
    }

    private MultiItemView getMultiItemViewForHotClassify2 () {
        MultiItemView multiItemView = new MultiItemView<ShowTypeDetailService.HotClassify>(){
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_types_icon;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShowTypeDetailService.HotClassify hotClassify, int position) {
                viewHolder.setText(R.id.item_title, hotClassify.getClassifyName());
                ImageView imageView = viewHolder.getConvertView().findViewById(R.id.item_icon);
//                Glide.with(mContext)
//                        .load(UIUtils.replaceMediaUrl(hotClassify.getClassifyImage()))
//                        // placeholder 图片加载出来前,显示的图片
//                        // error 图片加载失败后,显示的图片
//                        .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
//                        .into(imageView);
                imageView.setImageResource(hotClassify.getClassifyImage());
            }
        };
        return multiItemView;
    }

    private MultiItemView getMultiItemViewForBanner () {
        MultiItemView multiItemView = new MultiItemView<ShowTypeDetailService.BannerEntityWrapper>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.layout_banner;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShowTypeDetailService.BannerEntityWrapper bannerEntityWrapper, int i) {
                Banner banner = viewHolder.getConvertView().findViewById(R.id.banner);
                banner.clearAnimation();
                //设置 banner 样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());

                // 存放轮播图所有图片
                ArrayList<String> bannerImageList = new ArrayList<>();
                //清空旧数据
                bannerImageList.clear();

                for (int index = 0; index < bannerEntityWrapper.bannerEntities.size(); index++) {
                    bannerImageList.add(bannerEntityWrapper.getBannerEntities().get(index).getImg());
                }
                banner.setImages(bannerImageList);
                banner.start();
            }
        };
        return multiItemView;
    }

}
