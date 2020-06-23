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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseTagActivity;
import com.linkknown.ilearning.activity.KaoShiShijuanListActivity;
import com.linkknown.ilearning.activity.PersonalCenterActivity;
import com.linkknown.ilearning.activity.ReceiveCouponCenterActivity;
import com.linkknown.ilearning.activity.TeacherZhaoPingActivity;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FindFragment extends Fragment {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_space, container, false);
        mContext = getActivity();

        ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<Item> itemList = new ArrayList<>();

        itemList.add(new Item("分类", R.drawable.ic_fenlei, CourseTagActivity.class));
        itemList.add(new Item("考试", R.drawable.ic_test, KaoShiShijuanListActivity.class));
        itemList.add(new Item("推荐视频", R.drawable.ic_video, CourseTagActivity.class));
        itemList.add(new Item("名师招募令", R.drawable.ic_hire, TeacherZhaoPingActivity.class));
        itemList.add(new Item("畅享图书", R.drawable.ic_books, TeacherZhaoPingActivity.class));
        itemList.add(new Item("锦鲤活动", R.drawable.ic_huodong, TeacherZhaoPingActivity.class));
        itemList.add(new Item("领券中心", R.drawable.ic_coupon, ReceiveCouponCenterActivity.class));
        itemList.add(new Item("个人中心", R.drawable.ic_personal_center, PersonalCenterActivity.class));

        BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<Item, BaseViewHolder> (R.layout.item_space, itemList) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, Item item) {
                viewHolder.setImageResource(R.id.item_icon, item.getDrawableImage());
                viewHolder.setText(R.id.item_title, item.getItemName());

                viewHolder.itemView.setOnClickListener(v -> {
                    // 跳转 Activity
                    UIUtils.gotoActivity(getActivity(), item.getRedirectClazz());
                });
            }
        };
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recycleView.setAdapter(baseQuickAdapter);
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
