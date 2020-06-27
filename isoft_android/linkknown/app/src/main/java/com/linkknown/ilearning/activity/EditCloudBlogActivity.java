package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CatalogNameAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.GetMyCatalogsResponse;
import com.linkknown.ilearning.popup.BottomAddMyCatalogNameDialog;
import com.linkknown.ilearning.popup.BottomQuickEidtDialog;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

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

    //添加分类的弹框
    BottomAddMyCatalogNameDialog addMyCatalogName;

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
                            //设置"+文章分类" 点击事件
                            view.findViewById(R.id.addMyCatalogName).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //选择框先隐藏
                                    dialog.dismiss();
                                    //添加博客分类--准备底部弹框
                                    BlogCatalogEdit();
                                }
                            });

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


    //添加博客分类
    public void BlogCatalogEdit(){
        addMyCatalogName = new BottomAddMyCatalogNameDialog(mContext, (_catalog_name, _catalog_desc) ->
                LinkKnownApiFactory.getLinkKnownApi().BlogCatalogEdit(_catalog_name, _catalog_desc)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            addMyCatalogName.dismiss();
                            // 分类框弹出--分类重新查
                            selectCatalogName();
                        } else {
                            ToastUtil.showText(mContext, baseResponse.getErrorMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        addMyCatalogName.dismiss();
                    }
                }));
    };


    //提交博客
    public void BlogArticleEdit(){

        //再次校验参数
        String _blog_title = StringUtils.trim(blog_title.getText().toString());
        String _catalog_name = StringUtils.trim(catalog_name.getText().toString());
        String _content = StringUtils.trim(content.getText().toString());
        if (StringUtils.isEmpty(StringUtils.trim(_blog_title))){
            ToastUtil.showText(mContext, "文章标题不能为空！");
            return;
        }
        if (_blog_title.length()>40){
            ToastUtil.showText(mContext, "文章标题不能超过40个字符！");
            return;
        }


        if (StringUtils.isEmpty(StringUtils.trim(_catalog_name))){
            ToastUtil.showText(mContext, "文章分类不能为空！");
            return;
        }
        if (!catalogNameList.contains(_catalog_name)){
            ToastUtil.showText(mContext, "文章分类有误！");
            return;
        }

        if (StringUtils.isEmpty(StringUtils.trim(_content))){
            ToastUtil.showText(mContext, "文章内容不能为空！");
            return;
        }
        if (_content.length()>20000){
            ToastUtil.showText(mContext, "文章内容不能超过20000个字符！");
            return;
        }

        String article_id = "";
        String blog_title = _blog_title;
        String key_words = "";
        String catalog_name = _catalog_name;
        Integer blog_status = 1;
        String content = _content;
        String link_href = "";
        String first_img = "";
        //发送请求
        LinkKnownApiFactory.getLinkKnownApi().BlogArticleEdit(article_id,blog_title,key_words,catalog_name,blog_status,content,link_href,first_img)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse o) {
                        if (o.isSuccess()){
                            ToastUtil.showText(mContext,"发表成功");
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("status",o.getStatus());
                            intent.putExtra("bundle", bundle);
                            setResult(200,intent);
                            finish();
                        }else{
                            ToastUtil.showText(mContext,o.getErrorMsg());
                        }

                    };
                    @Override
                    public void onError(Throwable e) {
                        Log.e("BlogArticleEdit error", e.getMessage());
                        ToastUtil.showText(mContext,"发表博客失败！");
                    }
                });

    };


}

