import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/provider/cloud_blog_refresh_notifer.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:provider/provider.dart';

class EditBlogPage extends StatefulWidget {
  @override
  _EditBlogPage createState() => _EditBlogPage();
}

class _EditBlogPage extends State<EditBlogPage> with TickerProviderStateMixin {
  //发布博客字段
  TextEditingController blogTitleController;
  TextEditingController catalogNameController;
  TextEditingController blogContentController;

  //添加博客分类字段
  String catalog_name;
  String catalog_desc;

  List<String> catalogNameList = new List();

  @override
  Future<void> initState() {
    blogTitleController = new TextEditingController(text: '');
    catalogNameController = new TextEditingController(text: '');
    blogContentController = new TextEditingController(text: '');
    initData();
    super.initState();
  }

  void initData() {
    queryCatalogNames();
  }

  //查询博客分类
  void queryCatalogNames(){
    //获取文章分类
    LinkKnownApi.GetMyCatalogs().catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if (value.status == "SUCCESS") {
        catalogNameList.clear();
        for (var catalog in value.catalogs) {
          catalogNameList.add(catalog.catalogName);
        }
      } else {
        UIUtils.showToast(value.errorMsg);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HeaderWidget(),
          ),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.all(20),
          child: Column(
            children: <Widget>[
              TextField(
                controller: blogTitleController,
                decoration: InputDecoration(
                  labelText: '文章标题',
                ),
              ),
              VEmptyView(40),
              TextField(
                controller: catalogNameController,
                enableInteractiveSelection: false,
                decoration: InputDecoration(
                  labelText: '文章分类',
                ),
                onTap: () {
                  FocusScope.of(context).requestFocus(new FocusNode());
                  showDialog<Null>(
                    context: context,
                    builder: (BuildContext context) {
                      return showCatalogDialogList();
                    },
                  ).then((val) {
                    print(val);
                  });
                },
              ),
              VEmptyView(40),
              TextField(
                controller: blogContentController,
                maxLines: 12,
                decoration: InputDecoration(
                  labelText: '文章内容',
                ),
              ),
              VEmptyView(40),
              VEmptyView(40),
              CommonButton(
                callback: () {
                  publishBlog(context);
                },
                content: '提 交',
                //width: double.infinity,
              ),
            ],
          ),
        ),
      ),
    );
  }

  //分类弹框方法
  SimpleDialog showCatalogDialogList() {
    return new SimpleDialog(
      title: Row(
        children: <Widget>[
          Text('文章分类'),
          SizedBox(width: 100,),
          InkWell(
            onTap: (){
              Navigator.of(context).pop();
              showCatalogNameDialog();
            },
            child: Text('新建',style: TextStyle(color: Colors.blue,fontSize: 16),),
          )
        ],
      ),
      children: catalogNameList.map((_catalogName) =>
          SimpleDialogOption(
                child: new Text(_catalogName),
                onPressed: () {
                  catalogNameController = new TextEditingController(text: _catalogName);
                  Navigator.of(context).pop();
                },
              )
      ).toList(),
    );
  }

  //底部弹框，创建分类
  showCatalogNameDialog(){
    showModalBottomSheet(
      isScrollControlled:true,
      context: context,
      builder: (BuildContext context) {
        return SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                Container(
                  padding: EdgeInsets.all(20),
                  child: Column(children: <Widget>[
                    TextField(
                      decoration: InputDecoration(
                        labelText: '分类名称',
                      ),
                      onChanged: (String value) {
                        catalog_name = value;
                      },
                    ),
                    VEmptyView(40),
                    TextField(
                      decoration: InputDecoration(
                        labelText: '分类描述',
                      ),
                      onChanged: (String value) {
                        catalog_desc = value;
                      },
                    ),
                    VEmptyView(40),
                    VEmptyView(40),
                    CommonButton(
                      callback: () {
                        addBlogCatalogName(context);
                      },
                      content: '确 认',
                      //width: double.infinity,
                    ),
                    VEmptyView(40),
                    VEmptyView(40),
                  ],),
                )
              ],
            ),
          ),
        );
      },
    ).then((val) {
      print(val);
    });
  }

  //添加分类
  void addBlogCatalogName(BuildContext context){
    if (StringUtil.checkEmpty(catalog_name)) {
      UIUtils.showToast("博客分类不能为空");
      return;
    }
    if (catalog_name.contains('官方')) {
      UIUtils.showToast("分类名称不能含有 ‘官方’");
      return;
    }
    if (catalog_name.contains('热门博客')) {
      UIUtils.showToast("分类名称不能含有‘热门博客’");
      return;
    }

    if (StringUtil.checkEmpty(catalog_desc)) {
      UIUtils.showToast("分类描述不能为空");
      return;
    }

    LinkKnownApi.BlogCatalogEdit(catalog_name, catalog_desc).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if (value.status == "SUCCESS") {
        Navigator.of(context).pop();
        UIUtils.showToast("添加成功");
        queryCatalogNames();
      } else {
        UIUtils.showToast(value.errorMsg);
      }
    });
  }

  //发布博客
  void publishBlog(BuildContext context) {
    if (StringUtil.checkEmpty(blogTitleController.text)) {
      UIUtils.showToast("文章标题不能为空");
      return;
    }
    if (blogTitleController.text.length > 40) {
      UIUtils.showToast("文章标题不能超过40个字符");
      return;
    }

    if (StringUtil.checkEmpty(catalogNameController.text)) {
      UIUtils.showToast("文章分类不能为空");
      return;
    }
    if (catalogNameList.indexOf(catalogNameController.text) < 0) {
      UIUtils.showToast("文章分类有误");
      return;
    }

    if (StringUtil.checkEmpty(blogContentController.text)) {
      UIUtils.showToast("文章内容不能为空");
      return;
    }
    if (blogContentController.text.length > 20000) {
      UIUtils.showToast("文章内容不能超过20000个字符");
      return;
    }

    String article_id = "";
    String blog_title = blogTitleController.text;
    String key_words = "";
    String catalog_name = catalogNameController.text;
    int blog_status = 1;
    String content = blogContentController.text;
    String link_href = "";
    String first_img = "";
    LinkKnownApi.BlogArticleEdit(article_id, blog_title, key_words,
            catalog_name, blog_status, content, link_href, first_img)
        .catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if (value.status == "SUCCESS") {
        UIUtils.showToast("发布成功");
        Provider.of<CloudBlogRefreshNotifer>(context).update(true);
        NavigatorUtil.goBack(context);
      } else {
        UIUtils.showToast(value.errorMsg);
      }
    });
  }

  @override
  void dispose() {
    blogTitleController?.dispose();
    catalogNameController?.dispose();
    blogContentController?.dispose();
    super.dispose();
  }

}

class _HeaderWidget extends StatefulWidget {
  @override
  _HeaderWidgetState createState() => _HeaderWidgetState();
}

class _HeaderWidgetState extends State<_HeaderWidget>
    with TickerProviderStateMixin {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Container(
          child: Transform(
            transform: Matrix4.translationValues(0, 0, 0),
            child: Text("发布博客"),
          ),
        ),
      ],
    );
  }
}
