package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private Context mContext;
    private boolean mIsRefreshing = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type_detail, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        init();

        return rootView;
    }

    private void init() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView.post(() -> {
            mRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            loadData();
        });
        mRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            loadData();
        });

        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();

        // 注册多块内容
        multiTypeAdapter.register(BannerEntityWrapper.class, new MultiItemView<BannerEntityWrapper>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.layout_banner;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull BannerEntityWrapper bannerEntityWrapper, int i) {
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
        });
        BannerEntityWrapper wrapper = new BannerEntityWrapper();
        List<BannerEntity> bannerEntities = new ArrayList<>();
        BannerEntity bannerEntitie = new BannerEntity();
        bannerEntitie.setImg("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setTitle("测试11111");
        bannerEntitie.setLink("1111111");
        bannerEntities.add(bannerEntitie);
        bannerEntitie = new BannerEntity();
        bannerEntitie.setImg("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setTitle("测试11111");
        bannerEntitie.setLink("1111111");
        bannerEntities.add(bannerEntitie);
        bannerEntitie = new BannerEntity();
        bannerEntitie.setImg("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setTitle("测试11111");
        bannerEntitie.setLink("1111111");
        bannerEntities.add(bannerEntitie);
        wrapper.setBannerEntities(bannerEntities);
        multiTypeAdapter.setItems(Arrays.asList(wrapper));
        // 网格布局, 每行最多容量 2 个子 View
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (multiTypeAdapter.getItemViewType(position)) {
                    case 0:
                        // 将子视图的SpanSize都设置为2，那么这个子视图将占整个RecyclerView可用宽度
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(multiTypeAdapter);
    }


    @Override
    public void onClick(View v) {

    }

    private void loadData() {

    }

    private void clearData() {

    }

    @Data
    public class BannerEntityWrapper {
        public List<BannerEntity> bannerEntities;
    }

    @Data
    public class BannerEntity {
        public String title;
        public String img;
        public String link;
    }

}
