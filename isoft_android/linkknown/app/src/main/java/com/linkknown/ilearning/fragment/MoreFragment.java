package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CouponCenterActivity;
import com.linkknown.ilearning.activity.CourseClassifyActivity;
import com.linkknown.ilearning.activity.CourseListActivity;
import com.linkknown.ilearning.activity.TeacherZhaoPingActivity;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.OnItemClickListener;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// MultiTypeAdapter
public class MoreFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_space, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        init();

        return rootView;
    }

    private void init() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        List<Item> mData = new ArrayList<>();

        mData.add(new Item("直播0", R.drawable.item_space_live, CourseClassifyActivity.class));
        mData.add(new Item("直播0", R.drawable.item_space_live, CourseClassifyActivity.class));
        mData.add(new Item("直播0", R.drawable.item_space_live, CourseClassifyActivity.class));
        mData.add(new Item("直播0", R.drawable.item_space_live, CourseClassifyActivity.class));
        mData.add(new Item("直播0", R.drawable.item_space_live, CourseClassifyActivity.class));
        mData.add(new Item("直播0", R.drawable.item_space_live, CourseClassifyActivity.class));
        mData.add(new Item("推荐视频", R.drawable.item_space_zhaomu, CourseListActivity.class));
        mData.add(new Item("名师招募令", R.drawable.item_space_zhaomu, TeacherZhaoPingActivity.class));
        mData.add(new Item("畅享图书", R.drawable.item_space_zhaomu, TeacherZhaoPingActivity.class));
        mData.add(new Item("锦鲤活动", R.drawable.item_space_zhaomu, TeacherZhaoPingActivity.class));
        mData.add(new Item("领券中心", R.drawable.item_space_zhaomu, CouponCenterActivity.class));
        mData.add(new Item("个人中心", R.drawable.item_space_zhaomu, UserDetailActivity.class));


        MultiTypeAdapter adapter = new MultiTypeAdapter();
        adapter.register(Item.class, new MultiItemView<Item>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_space;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull Item item, int i) {
                viewHolder.setImageResource(R.id.item_icon, item.getDrawableImage());
                viewHolder.setText(R.id.item_title, item.getItemName());
            }
        });
        adapter.setItems(mData);
        adapter.setOnItemClickListener(new OnItemClickListener<Item>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, Item item, int position) {
                // 跳转 Activity
                UIUtils.gotoActivity(getActivity(), item.getRedirectClazz());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, Item item, int position) {
                return false;
            }
        });
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Item {
        String itemName;
        int drawableImage;
        // 点击跳转的 Activity 类
        Class redirectClazz;
    }
}
