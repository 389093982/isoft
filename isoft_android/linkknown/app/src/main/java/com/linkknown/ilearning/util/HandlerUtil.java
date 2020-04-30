package com.linkknown.ilearning.util;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

// 安全的消息传递机制 Handler 工具类
public class HandlerUtil {

    public static class HandlerHolder extends Handler {
        WeakReference<OnReceiveMessageListener> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param listener 收到消息回调接口
         */
        public HandlerHolder(OnReceiveMessageListener listener) {
            mListenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }

    // 供 activity 实现刷新数据
    public interface OnReceiveMessageListener {
        void handlerMessage(Message msg);
    }
}
