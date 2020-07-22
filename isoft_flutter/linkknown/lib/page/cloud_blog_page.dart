import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/page/course_comment.dart';
import 'package:linkknown/page/course_introduce.dart';
import 'package:linkknown/page/user_course.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/accept_invite_button_label.dart';
import 'package:linkknown/widgets/attention_off_button_label.dart';
import 'package:linkknown/widgets/attention_on_button_label.dart';

import 'cloud_blog.dart';
import 'my_customer.dart';

class CloudBlogPage extends StatefulWidget {
  CloudBlogPage();

  @override
  _CloudBlogPageState createState() => _CloudBlogPageState();
}

class _CloudBlogPageState extends State<CloudBlogPage> with TickerProviderStateMixin {
  _CloudBlogPageState();

  GlobalKey<CloudBlogState> scope_all_key = GlobalKey();
  GlobalKey<CloudBlogState> myself_key = GlobalKey();

  String headIcon;
  String nickName;
  TabController tabController;
  final searchInputController = TextEditingController();
  //查询博客--初始值就设置为空串
  String searchData = "";
  int tabCounts = 2;

  @override
  void initState() {
    super.initState();
    // 用来控制controller对应widget的各种各样交互行为以及状态变化的控制（类似于widget本身只是一个静态的物品，而通过对controller的操作控制让这个widget活了起来）
    this.tabController = TabController(length: tabCounts, vsync: this);

    initData();
  }

  void initData() async {
    headIcon = await LoginUtil.getSmallIcon();
    nickName = await LoginUtil.getNickName();
    //拿到数据后做个通知，重新执行build
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: tabCounts,
      child: Scaffold(
        body: NestedScrollView(
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return [
              SliverAppBar(
                leading: Container(
                    margin: EdgeInsets.all(10), // 设置边距
                    child: IconButton(
                      icon: Icon(
                        Icons.arrow_back,
                        size: 20,
                      ),
                      onPressed: () {
                        // 返回上一页
                        NavigatorUtil.goBack(context);
                      },
                    )),
                // appBar是否置顶
                pinned: true,
                elevation: 0,
                expandedHeight: 300,
                // 一个显示在 AppBar 下方的控件，高度和 AppBar 高度一样，可以实现一些特殊的效果，该属性通常在 SliverAppBar 中使用
                flexibleSpace: FlexibleSpaceBar(
                  //centerTitle: true,//靠左显示
                  title: Text(""),
                  background: Container(
                    height: 100,
                    color: Colors.white,
                    child: Stack(
                      children: <Widget>[
                        Image.asset(
                          "images/personal_center_img02.jpg",
                          fit: BoxFit.fill,
                          width: 750,
                          height: 180,
                        ),
                        Positioned(
                          top: 140,
                          left: 30,
                          child: ClipOval(
                            child:
                            StringUtil.checkNotEmpty(headIcon)?Image.network(
                              UIUtils.replaceMediaUrl(headIcon??""),
                              width: 80,
                              height: 80,
                            ):Image.asset(
                                "images/linkknown.jpg",
                                width: 80,
                                height: 80,
                            )
                          ),
                        ),
                        Positioned(top: 190, left: 110, child: Text(nickName,style: TextStyle(fontSize: 18,color: Colors.black54),)),
                        Positioned(top: 190, left: 300,
                          child: InkWell(
                            onTap:(){
                              NavigatorUtil.goRouterPage(context, "${Routes.editBlog}");
                            },
                            child: SvgPicture.asset(
                              "images/ic_add_blog.svg",
                              height: 20,
                              color: Colors.black54,
                            ),
                          ),
                        ),
                        Positioned(
                          top: 250,
                          child: ConstrainedBox(
                            constraints: BoxConstraints(
                              maxHeight: 35,
                              maxWidth: 325
                            ),
                            child: Container(
                              color: Colors.grey[50],
                              margin: EdgeInsets.only(left: 30),
                              alignment: Alignment.center,
                              child: TextField(
                                //最大行数
                                maxLines: 1,
                                //光标颜色
                                cursorColor: Colors.grey,
                                //光标宽度
                                cursorWidth: 2.0,
                                //输入文本的样式
                                style: TextStyle(fontSize: 15.0),
                                controller: searchInputController,
                                decoration: new InputDecoration(
                                  contentPadding: EdgeInsets.only(left: 12,top:0),//光标距离左侧距离
                                  hintText: '搜索..',
                                  enabledBorder: OutlineInputBorder(//未点击输入框的效果
                                    borderSide: BorderSide(
                                      color: Colors.grey[300], //边框颜色
                                      width: 1, //宽度为2
                                    ),
                                    borderRadius: BorderRadius.circular(1),//四个角弧度
                                  ),
                                  focusedBorder: OutlineInputBorder(//点击输入框后的效果
                                    borderSide: BorderSide(
                                      color: Colors.grey[400], //边框颜色
                                      width: 1, //宽度为2
                                    ),
                                    borderRadius: BorderRadius.circular(1),//四个角弧度
                                  ),
                                  suffixIcon: InkWell(
                                    onTap: () {
                                      //查询博客
                                      searchBlog(searchInputController.text);
                                    },
                                    child: Icon(
                                      Icons.search,
                                      color: Colors.blue,
                                    ),
                                  ),
                                ),
                                // onChanged: onSearchTextChanged,
                              ),
                            )
                          )),
                      ],
                    ))
                ),
              ),
              SliverPersistentHeader(
                pinned: true,
                delegate: StickyTabBarDelegate(
                  child: TabBar(
                    indicatorColor: Colors.pinkAccent,
                    unselectedLabelColor: Colors.grey[600],
                    labelColor: Colors.pinkAccent,
                    indicatorSize: TabBarIndicatorSize.label,
                    controller: this.tabController,
                    tabs: <Widget>[
                      Tab(text: '云博客'),
                      Tab(text: '我的博客'),
                    ],
                  ),
                ),
              ),
            ];
          },
          body: TabBarView(controller: this.tabController, children: [
            CloudBlogWidget("SCOPE_ALL",searchData, key: scope_all_key,),
            CloudBlogWidget("SCOPE_MYSELF",searchData, key: myself_key,),
          ]),
        ),
      ),
    );
  }


  //查询博客
  void searchBlog(String searchData){
    if(StringUtil.checkNotEmpty(searchData)){
      this.searchData = searchData;
      scope_all_key.currentState.onRefresh(searchData:searchData);
      myself_key.currentState.onRefresh(searchData:searchData);
    }else{
      UIUtils.showToast("请输入搜索内容..");
    }
  }


}

// SliverPersistentHeaderDelegate的实现类必须实现其4个方法
// minExtent：收起状态下组件的高度；
// maxExtent：展开状态下组件的高度；
// shouldRebuild：类似于react中的shouldComponentUpdate；
// build：构建渲染的内容。
class StickyTabBarDelegate extends SliverPersistentHeaderDelegate {
  final TabBar child;

  StickyTabBarDelegate({@required this.child});

  @override
  Widget build(
      BuildContext context, double shrinkOffset, bool overlapsContent) {
    return Material(
      child: Container(
        margin: EdgeInsets.only(right: 180),
        child: this.child,
      ),
    );
  }

  @override
  double get maxExtent => this.child.preferredSize.height;

  @override
  double get minExtent => this.child.preferredSize.height;

  @override
  bool shouldRebuild(SliverPersistentHeaderDelegate oldDelegate) {
    return true;
  }
}
