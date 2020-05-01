package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.linkknown.ilearning.R;

public class ClassifyFragment extends Fragment {

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        mContext = getActivity();
        //加载view
        initViews(view);

//        //从服务器获取商品类别列表
//        getDataFromServer();
//
//        handler = new Handler();
//
//
//        //设置searchView点击事件
//        setSearchViewIntent();

        return view;
    }

    /**
     * 绑定操作控件
     *
     * @param view
     */
    private void initViews(View view) {

    }
}
