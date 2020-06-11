package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.CourseFilterFragment;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseSearchActivity extends BaseActivity {

    private Context mContext;
    private String search;

    @BindView(R.id.searchTextView)
    public EditText searchTextView;
    @BindView(R.id.searchEditTextClear)
    public ImageView searchEditTextClear;
    @BindView(R.id.searchBtn)
    public ImageView searchBtn;
    @BindView(R.id.goback)
    public ImageView goback;

    private Disposable searchTextViewDisposable;
    private int showHintIndex = 0;

    // 过滤课程的 fragment
    private CourseFilterFragment courseFilterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 解决安卓软键盘遮挡输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.activity_course_search);

        ButterKnife.bind(this);
        mContext = this;

        initView();
    }

    // 初始化搜索框和 fragment
    private void initView() {
        goback.setVisibility(View.VISIBLE);
        initSearchText();

        courseFilterFragment = new CourseFilterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.courseFilterFragment, courseFilterFragment).commit();
    }

    // 初始化完成后调用刷新 fragment 数据
    @Override
    protected void onResume() {
        super.onResume();
        // onStart()方法完成之后，此时activity进入onResume()方法中，当前activity状态属于运行状态 (Running)，可与用户进行交互
        // 必须在 fragment 初始化完成后执行
        if (StringUtils.isNotEmpty(search)) {
            refreshFragment();
        }
    }

    private void initSearchText() {
        search = getIntent().getStringExtra("search");
        if (StringUtils.isNotEmpty(search)) {
            searchTextView.setText(search);
        }

        // 有内容显示 clear 图标,没有则隐藏
        RxTextView.textChanges(searchTextView)
                .map(CharSequence::toString)
                .subscribe(s -> {
                    if (!TextUtils.isEmpty(s)) {
                        searchEditTextClear.setVisibility(View.VISIBLE);
                    } else {
                        searchEditTextClear.setVisibility(View.GONE);
                    }
                });
        // 清空输入框
        RxView.clicks(searchEditTextClear)
                .subscribe(aVoid -> searchTextView.setText(""));

        RxView.clicks(searchBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .map(aVoid -> searchTextView.getText().toString().trim())
                .filter(s -> !TextUtils.isEmpty(s))
                .subscribe(s -> {
                    // 关闭软键盘
                    KeyBoardUtil.closeKeybord(searchTextView, mContext);
//                    showSearchAnim();
//                    clearData();
                    search = s;

                    // 记录搜索历史
                    CommonUtil.recordSearchHistory(mContext, search);

                    refreshFragment();
                });

        // 定时任务定时修改 TextView 中的提示文字
        searchTextViewDisposable = Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())                   // 在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(aLong -> {
                    searchTextView.setHint(Constants.COURSE_SEARCH_HINT_LIST.get(showHintIndex));
                    showHintIndex = showHintIndex == Constants.COURSE_SEARCH_HINT_LIST.size() - 1 ? 0 : ++ showHintIndex;
                });

        // 软键盘搜索
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = searchTextView.getText().toString().trim();
                    search = searchText;
                    // 关闭软键盘
                    KeyBoardUtil.closeKeybord(searchTextView, mContext);
                    // 记录搜索历史
                    CommonUtil.recordSearchHistory(mContext, search);

                    refreshFragment();
                    return true;
                }
                return false;
            }
        });
    }

    private void refreshFragment() {
        if (StringUtils.isEmpty(search)) {
            return;
        }
        courseFilterFragment.refreshFragment(search, "");
    }

    // 返回
    @OnClick(R.id.goback)
    public void goback() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchTextViewDisposable != null) {
            searchTextViewDisposable.dispose();
        }
    }
}
