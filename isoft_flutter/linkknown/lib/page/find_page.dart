import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class FindPage extends StatefulWidget {
  @override
  _FindPageState createState() => _FindPageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _FindPageState extends State<FindPage> with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<ClassifyItem> getDataList() {
    List<ClassifyItem> list = [];
    list..add(ClassifyItem("分类", "images/fenlei.png", Routes.login))
      ..add(ClassifyItem("考试", "images/kaoshi.png", Routes.login))
      ..add(ClassifyItem("推荐视频", "images/video.png", Routes.login))
      ..add(ClassifyItem("名师招募令", "images/zhaopin.png", Routes.login))
      ..add(ClassifyItem("畅享图书", "images/book.png", Routes.login))
      ..add(ClassifyItem("锦鲤活动", "images/huodong.png", Routes.login))
      ..add(ClassifyItem("领券中心", "images/coupon.png", Routes.receiveCouponCenter))
      ..add(ClassifyItem("个人中心", "images/personalCenter.png", Routes.login));
    return list;
  }

  List<Widget> getWidgetList() {
    return getDataList().map((item) => getItemContainer(item)).toList();
  }

  Widget getItemContainer(ClassifyItem item) {
    return new InkWell(
      //点击事件回调
      onTap: () {
        NavigatorUtil.goRouterPage(context, item.redirectUrl);
      },
      child: Column(
        children: <Widget>[
          Container(
            padding:EdgeInsets.all(5),
            child: Image.asset(
                item.classifyIcon,
                width: 40,
                height: 40,
            ),
          ),
          Expanded(child: Text(item.classifyName,style: TextStyle(fontSize: 12),),),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        // 禁用返回，也可以使用    leading: Text(''),
        automaticallyImplyLeading: false,
        title: Text("发现精彩"),
        centerTitle: true,
      ),
      body: Container(
        padding: EdgeInsets.all(10),
        // infinity 无穷大
        height: double.infinity,
        width: double.infinity,
        child: GridView.count(
          //水平子 Widget 之间间距
          crossAxisSpacing: 10.0,
          //垂直子 Widget 之间间距
          mainAxisSpacing: 10.0,
          //GridView内边距
          padding: EdgeInsets.all(10.0),
          //一行的Widget数量
          crossAxisCount: 4,
          //子Widget宽高比例
          childAspectRatio: 1.0,
          //子Widget列表
          children: getWidgetList(),
        ),
      ),
    );
    // GridView.count 创建网格布局
  }
}

class ClassifyItem {

  String classifyName;
  String classifyIcon;
  String redirectUrl;

  ClassifyItem(this.classifyName, this.classifyIcon, this.redirectUrl);

}