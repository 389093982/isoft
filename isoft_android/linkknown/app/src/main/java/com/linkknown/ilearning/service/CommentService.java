package com.linkknown.ilearning.service;

import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CommentResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentService {

    public static String getKey(int theme_pk, String theme_type, String comment_type, int current_page) {
        return String.format("commentResponse_%d_%s_%s_%d", theme_pk, theme_type, comment_type, current_page);
    }

    public static void filterCommentFirstLevel(int theme_pk, String theme_type, String comment_type, int current_page, int offset) {
        LinkKnownApiFactory.getLinkKnownApi().filterCommentFirstLevel(theme_pk, theme_type, comment_type, current_page, offset)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CommentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommentResponse commentResponse) {
                        if (commentResponse.isSuccess()) {
                            LiveEventBus.get(getKey(theme_pk, theme_type, comment_type, current_page), CommentResponse.class).post(commentResponse);
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            LiveEventBus.get(getKey(theme_pk, theme_type, comment_type, current_page), CommentResponse.class).post(commentResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        CommentResponse result = new CommentResponse();
                        result.setErrorMsg("加载评论列表失败！");
                        LiveEventBus.get(getKey(theme_pk, theme_type, comment_type, current_page), CommentResponse.class).post(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
