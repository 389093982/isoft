package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.R;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CourseSearchActivity extends BaseActivity {

    private Context mContext;
    private String search;

    @BindView(R.id.searchEditText)
    public TextView searchEditText;
    @BindView(R.id.searchEditTextClear)
    public ImageView searchEditTextClear;

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

        RxTextView.textChanges(searchEditText)
            .map(CharSequence::toString)
            .subscribe(s -> {
                if (!TextUtils.isEmpty(s)) {
                    searchEditTextClear.setVisibility(View.VISIBLE);
                } else {
                    searchEditTextClear.setVisibility(View.GONE);
                }
            });
    }

    private void initData () {

    }

}
