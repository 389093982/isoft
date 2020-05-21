package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseSearchResponse;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseSearchActivity extends BaseActivity {

    private Context mContext;
    private String search;

    @BindView(R.id.searchEditText)
    public EditText searchEditText;
    @BindView(R.id.searchEditTextClear)
    public ImageView searchEditTextClear;
    @BindView(R.id.searchBtn)
    public ImageView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);

        ButterKnife.bind(this);
        mContext = this;

        initView();

        initData();
    }

    private void initView () {
        initSearchText();
    }

    private void initSearchText () {
        search = getIntent().getStringExtra("search");
        if (StringUtils.isNotEmpty(search)) {
            searchEditText.setText(search);
        }

        // 有内容显示 clear 图标,没有则隐藏
        RxTextView.textChanges(searchEditText)
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
                .subscribe(aVoid -> searchEditText.setText(""));

        RxView.clicks(searchBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .map(aVoid -> searchEditText.getText().toString().trim())
                .filter(s -> !TextUtils.isEmpty(s))
                .subscribe(s -> {
                    // 关闭软键盘
                    KeyBoardUtil.closeKeybord(searchEditText, mContext);
//                    showSearchAnim();
//                    clearData();
                    search = s;
                    initSearchData();
                });

    }

    private void initSearchData () {
        if (StringUtils.isNotEmpty(search)) {
            LinkKnownApiFactory.getLinkKnownApi().searchCourseList(search)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<CourseSearchResponse>() {
                        @Override
                        public void onNext(CourseSearchResponse courseSearchResponse) {
                            if (courseSearchResponse.isSuccess()) {
                                ToastUtil.showText(mContext, courseSearchResponse.toString());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("searchCourseList error", e.getMessage());
                        }
                    });
        }
    }

    private void initData () {
        if (StringUtils.isNotEmpty(search)) {
            initSearchData();
        }
    }

}
