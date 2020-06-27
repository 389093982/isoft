package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.BlogListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CatalogNameAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    public OnclickCatalogName onclickCatalogName;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CatalogNameAdapter(Context mContext, List<String> catalogName) {
        super(R.layout.layout_catalog_name_item, catalogName);
        this.mContext = mContext;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @NotNull String catalogName) {
        //博客分类
        viewHolder.setText(R.id.catalog_name,catalogName);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickCatalogName.clickCatalogName(catalogName);
            }
        });
    }

    public interface OnclickCatalogName{
        public void clickCatalogName(String catalogName);
    };

    public void setOnclickCatalogName(OnclickCatalogName onclickCatalogName) {
        this.onclickCatalogName = onclickCatalogName;
    }
}