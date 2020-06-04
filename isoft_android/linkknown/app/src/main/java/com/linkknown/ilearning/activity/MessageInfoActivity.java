package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.MessageAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.MessageListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private BaseQuickAdapter adapter;

    private List<MessageListResponse.Message> messageList = new ArrayList<>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);

        mContext = this;

        ButterKnife.bind(this);
        initToolBar(toolbar, true, "我的消息");

        initView();
        initData();
    }

    private void initView () {

    }

    private void initData () {
        if (adapter == null) {
            initRecyclerViewAdapter();
        }

        LinkKnownApiFactory.getLinkKnownApi().queryPageMessageList(1, Constants.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<MessageListResponse>() {
                    @Override
                    public void onNext(MessageListResponse messageListResponse) {
                        if (messageListResponse.isSuccess()) {
                            messageList.addAll(messageListResponse.getMessages());
                            adapter.setList(messageList);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void initRecyclerViewAdapter () {
        adapter = new MessageAdapter(mContext, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }
}
