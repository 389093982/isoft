package com.linkknown.ilearning.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.internal.FlowLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

public class KaoshiCenterPopupView extends CenterPopupView {

    private Context mContext;
    private CallBackListener listener;
    private List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> kaoshiShijuanDetailList;

    AppCompatButton submitAll;

    public KaoshiCenterPopupView(@NonNull Context context,
                                 List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> kaoshiShijuanDetailList, CallBackListener listener) {
        super(context);
        this.mContext = context;
        this.kaoshiShijuanDetailList = kaoshiShijuanDetailList;
        this.listener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        submitAll = findViewById(R.id.submitAll);

        FlowLayout flowLayout = findViewById(R.id.flowLayout);
        for (int i=1; i<= kaoshiShijuanDetailList.size(); i++) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.textview_common, flowLayout, false);

            textView.setText(i + "");
            textView.setOnClickListener(v -> {
                listener.updateKaoshiTimuWithIndex(Integer.parseInt(textView.getText().toString()) - 1);
                dismiss();
            });
            flowLayout.addView(textView);
        }
    }

    public void updateKaoshiTimeCost (long time) {
        if (submitAll != null) {
            submitAll.setText("交卷（" + DateUtil.secToMinuteAndSec((int)(3600 - time)) + "）");
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_kaoshi_progress;
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.5f);
    }

    public interface CallBackListener {
        void updateKaoshiTimuWithIndex(int index);
    }
}
