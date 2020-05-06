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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SpaceFragment extends Fragment implements View.OnClickListener {

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

    private void init (){
        initRecyclerView();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ItemSpace {
        String itemName;
        int drawableImage;
    }

    private void initRecyclerView () {
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        List<ItemSpace> mData = new ArrayList<>();

        mData.add(new ItemSpace("直播0", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播2", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播3", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播4", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播5", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播6", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播7", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播8", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));
        mData.add(new ItemSpace("直播9", R.drawable.item_space_live));

        MultiTypeAdapter adapter = new MultiTypeAdapter();
        adapter.register(ItemSpace.class, new MultiItemView<ItemSpace>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_space;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ItemSpace itemSpace, int i) {
                viewHolder.setImageResource(R.id.item_icon, itemSpace.getDrawableImage());
                viewHolder.setText(R.id.item_title, itemSpace.getItemName());
            }
        });
        adapter.setItems(mData);
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
