//package com.linkknown.ilearning.common;
//
//import android.util.TypedValue;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//
//import java.util.List;
//
//import io.reactivex.Observer;
//
//public class BasePagingActivity<T> extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
//    private static final int PAGE_SIZE = 20;
//    private RecyclerView mRecyclerView;
//    private BaseQuickAdapter mQuickAdapter;
//    private IPagingService<List<T>> mPagingService;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private int currentPage;
//    private int lastPage;
//
//    private void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
//        if (swipeRefreshLayout != null) {
//            mSwipeRefreshLayout = swipeRefreshLayout;
//            mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
//            mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
//            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//            swipeRefreshLayout.setOnRefreshListener(this);
//        } else {
//            throw new NullPointerException("swipeRefreshLayout not null");
//        }
//    }
//
//    private void setRecyclerView(RecyclerView recyclerView) {
//        mRecyclerView = recyclerView;
//        if (mRecyclerView.getLayoutManager() == null) {
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        }
//    }
//
//    private void setQuickAdapter(BaseQuickAdapter quickAdapter) {
//        if (quickAdapter != null) {
//            mQuickAdapter = quickAdapter;
//            mQuickAdapter.openLoadAnimation();
//            mQuickAdapter.openLoadMore(PAGE_SIZE);
//            mQuickAdapter.setOnLoadMoreListener(this);
//            mRecyclerView.setAdapter(quickAdapter);
//        } else {
//            throw new NullPointerException("swipeRefreshLayout not null");
//        }
//    }
//
//    /**
//     * 开始获取数据,提供给子类调用
//     */
//    protected void startGetData(RecyclerView recyclerView,SwipeRefreshLayout swipeRefreshLayout,BaseQuickAdapter quickAdapter, IPagingService<List<T>> pagingService){
//        mPagingService = pagingService;
//        setRecyclerView(recyclerView);
//        setSwipeRefreshLayout(swipeRefreshLayout);
//        setQuickAdapter(quickAdapter);
//        onLoadFirstData();
//    }
//
//    @Override
//    public void onRefresh() {
//        currentPage = 1;
//        mPagingService.getData(currentPage, PAGE_SIZE, new Observer<List<T>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(BasePagingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                mSwipeRefreshLayout.setRefreshing(false);
//                currentPage = lastPage;
//            }
//
//            @Override
//            public void onNext(List<T> list) {
//                if (list == null) return;
//                if (!mQuickAdapter.isLoading()){
//                    mQuickAdapter.openLoadMore(PAGE_SIZE);
//                }
//                mQuickAdapter.getData().clear();
//                mQuickAdapter.addData(list);
//                mQuickAdapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }
//
//    @Override
//    public void onLoadMoreRequested() {
//        lastPage = currentPage;
//        currentPage++;
//        mPagingService.getData(currentPage, PAGE_SIZE, new Observer<List<T>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(BasePagingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                currentPage = lastPage;
//            }
//
//            @Override
//            public void onNext(List<T> list) {
//                if ((list != null && list.isEmpty())) {
//                    Toast.makeText(BasePagingActivity.this,"没有更多数据了",Toast.LENGTH_SHORT).show();
//                    mQuickAdapter.addData(list);
//                    mQuickAdapter.loadComplete();
//                } else {
//                    mQuickAdapter.addData(list);
//                }
//                lastPage = currentPage;
//            }
//        });
//    }
//
//    public void onLoadFirstData(){
//        lastPage = currentPage = 1;
//        mSwipeRefreshLayout.setRefreshing(true);
//        mPagingService.getData(currentPage, PAGE_SIZE, new Observer<List<T>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(BasePagingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onNext(List<T> list) {
//                if (list == null) return;
//                mQuickAdapter.addData(list);
//                mQuickAdapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }
//}