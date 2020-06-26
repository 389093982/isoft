package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CatalogNameAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.GetMyCatalogsResponse;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditCloudBlogActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    //文章分类显示做准备
    public CatalogNameAdapter catalogNameAdapter;
    public List<String> catalogNameList = new ArrayList<>();
    public AlertDialog.Builder builder;
    public AlertDialog dialog;

    //文章标题
    @BindView(R.id.blog_title)
    public TextView blog_title;

    //文章分类
    @BindView(R.id.catalog_name)
    public TextView catalog_name;

    //文章内容
    @BindView(R.id.content)
    public TextView content;

    //发表
    @BindView(R.id.submitBlog)
    public TextView submitBlog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cloud_blog);
        mContext = this;
        ButterKnife.bind(this);
        builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();

        catalogNameAdapter = new CatalogNameAdapter(mContext,catalogNameList);
        catalogNameAdapter.setOnclickCatalogName(catalogName -> {
            catalog_name.setText(catalogName);
            dialog.dismiss();
        });

        initView();
        initData();
    }


    public void initView(){
        initToolBar(toolbar,true,"发表博客");
        catalog_name.setOnClickListener(this);
        submitBlog.setOnClickListener(this);

        blog_title.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(StringUtils.trim(textView.getText().toString()))){
                    ToastUtil.showText(mContext, "文章标题不能为空！");
                }
            }
        });

        catalog_name.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //选择文章分类
                selectCatalogName();
            }
        });

        content.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(StringUtils.trim(textView.getText().toString()))){
                    ToastUtil.showText(mContext, "文章内容不能为空！");
                }
            }
        });
    };


    public void initData(){


    };

    @Override
    public void onClick(View v) {
        if (SecurityUtil.isFastClick()) {
            ToastUtil.showText(this, "您点击的太频繁");
            return;
        }
        switch (v.getId()) {
            case R.id.catalog_name:
                selectCatalogName();
                break;
            case R.id.submitBlog:
                BlogArticleEdit();
                break;
            default:
                break;
        }
    }


    //如果内容发生变化，自动调用
    @OnTextChanged({R.id.blog_title,R.id.catalog_name,R.id.content})
    public void onTextChanged() {
        String _blog_title = StringUtils.trim(blog_title.getText().toString());
        String _catalog_name = StringUtils.trim(catalog_name.getText().toString());
        String _content = StringUtils.trim(content.getText().toString());

        if (StringUtilEx.isAllNotEmpty(_blog_title,_catalog_name,_content)) {
            submitBlog.setEnabled(true);
        } else {
            submitBlog.setEnabled(false);
        }
    }


    //选择文章分类
    public void selectCatalogName(){
        LinkKnownApiFactory.getLinkKnownApi().GetMyCatalogs()
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<GetMyCatalogsResponse>() {
                    @Override
                    public void onNext(GetMyCatalogsResponse o) {
                        if (o.isSuccess()){
                            if (CollectionUtils.isNotEmpty(o.getCatalogs())){
                                catalogNameList.clear();
                                for (GetMyCatalogsResponse.Catalogs catalog:o.getCatalogs()){
                                    catalogNameList.add(catalog.getCatalog_name());
                                }
                                catalogNameAdapter.setList(catalogNameList);

                                //设置弹框
                                View view = View.inflate(mContext, R.layout.dialog_select_catalong_name, null);
                                RecyclerView recycleView = view.findViewById(R.id.recyclerView);
                                recycleView.setAdapter(catalogNameAdapter);
                                recycleView.setLayoutManager(new LinearLayoutManager(mContext));
                                dialog.setView(view);
                                dialog.show();

                            }else{
                                ToastUtil.showText(mContext,"目前没有分类，需要新建！");
                            }

                        }else{
                            ToastUtil.showText(mContext,"查询文章分类失败！");
                        }

                    };
                    @Override
                    public void onError(Throwable e) {
                        Log.e("GetMyCatalogs error", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                    }
                });
    };


    //提交博客
    public void BlogArticleEdit(){

        //再次校验参数

        //发送请求
        ToastUtil.showText(mContext,"提交博客...");


    };


}

