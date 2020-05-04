package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.manager.CourseServiceManager;
import com.linkknown.ilearning.model.CourseSearchResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassifyFragment extends Fragment {

    private Context mContext;

    //一级菜单，两个ListView控件
    @BindView(R.id.left)
    public ListView leftClassifyListView;
    @BindView(R.id.right)
    public ListView rightCourseListView;
    @BindView(R.id.progress)
    public RelativeLayout mProgressBar;
    @BindView(R.id.home_serachview)
    public SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classify, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        init();

//        //从服务器获取商品类别列表
//        getDataFromServer();
//
//        handler = new Handler();
//
//
//        //设置searchView点击事件
//        setSearchViewIntent();

        return rootView;
    }

    private void init() {
        ArrayList<String> mData = new ArrayList<>();
        mData.add("前端");
        mData.add("后台");
        mData.add("数据库");
        mData.add("docker");
        mData.add("自动化测试");
        mData.add("其它");
        mData.add("全部");

        CommonAdapter leftClassifyListAdapter = new CommonAdapter<String>(mData, R.layout.classify_left_item) {
            @Override
            public void bindView(ViewHolder holder, String str) {
                holder.setText(R.id.classifyName, str);
            }
        };
        leftClassifyListView.setAdapter(leftClassifyListAdapter);
        leftClassifyListView.setOnItemClickListener((parent, view, position, id) -> {
            leftClassifyListAdapter.forEachHolder((position1, viewHolder) -> {
                if (position == position1){
                    viewHolder.setChecked(R.id.radioButton, true);
                } else {
                    viewHolder.setChecked(R.id.radioButton, false);
                }
            });
            //加载中动效
            showLoading(true);

            // 加载右侧视频数据
            loadCourseList(mData.get(position));
        });

        LiveEventBus.get("courseSearchResponse", CourseSearchResponse.class).observe(this, courseSearchResponse -> {
            if (StringUtils.isEmpty(courseSearchResponse.getErrorMsg())) {
                CommonAdapter rightCourseListAdapter = new CommonAdapter<CourseSearchResponse.Course>((ArrayList<CourseSearchResponse.Course>) courseSearchResponse.getCourses(), R.layout.classify_right_item) {

                    @Override
                    public void bindView(ViewHolder holder, CourseSearchResponse.Course course) {
                        holder.setText(R.id.courseName, course.getCourse_name());
                    }
                };
                rightCourseListView.setAdapter(rightCourseListAdapter);
            } else {
                ToastUtil.showText(getContext(),"数据加载异常");
            }
            // 去掉加载效果
            showLoading(false);
        });

//        //商品类别菜单点击事件
//        mCommodityTypeListView.setOnItemClickListener((parent, view, position, id) -> {
//            if (mCommodityTypeListAdapter != null) {
//                mCommodityTypeListAdapter.setSelectedPos(position);
//            }
//            if (mListView2Adapter != null) {
//                mListView2Adapter.setSelectedPos(-1);
//            }
//
//            CommodityTypeModel menuData = (CommodityTypeModel) parent.getItemAtPosition(position);
//
//            //根据类型id从服务器获取商品列表
//            getCommodityFromServerByTypeId(menuData.getId());
//            //加载中动效
//            loadingAnim(true);
//        });

//        //商品item点击事件
//        mCommodityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                CommodityModel commodity = (CommodityModel) adapterView.getItemAtPosition(position);
//                //跳转到详情
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                intent.putExtra("id", commodity.getId() + "");
//                startActivity(intent);
//            }
//        });
    }

    /**
     * 当进行网络请求时，播放进度条动画
     *
     * @param isLoading 是否正在网络请求
     */
    private void showLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rightCourseListView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void loadCourseList (String search) {
        CourseServiceManager.search(search);
    }
}
