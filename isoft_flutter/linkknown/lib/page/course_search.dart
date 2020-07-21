import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/common_search.dart';

class CourseSearchPage extends StatefulWidget {
  String search;
  String isCharge;

  CourseSearchPage(this.search, this.isCharge);

  @override
  _CourseSearchPageState createState() => _CourseSearchPageState();
}

class _CourseSearchPageState extends State<CourseSearchPage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Flutter去掉AppBar避免body溢出到状态栏
      // 没有AppBar的Flutter，如果不在Scaffold中使用AppBar会发现默认是沉浸式，预留出状态栏的高度方法
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(MediaQuery.of(context).size.height * 0),
        child: Container(
          width: double.infinity,
          height: double.infinity,
          decoration: BoxDecoration(
            color: Colors.red,
          ),
        ),
      ),
      body: SafeArea(
        child: Column(
          children: <Widget>[
            Container(
              height: ScreenUtil().setHeight(130),
              color: Colors.red,
              child: SearchInputWidget(
                handleSearch: (data) {
                  this.setState(() {
                    widget.search = data;
                     CommonUtil.recordSearchHistory(data);
                  });
                },
              ),
            ),
            SizedBox(
              height: 10,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                Container(
                  margin: EdgeInsets.only(right: 10),
                  child: InkWell(
                    onTap: (){
                      showHotOrHistory('hot');
                    },
                    child: Text(
                      "热门搜索",
                      style: TextStyle(color: Colors.grey),
                    ),
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(right: 10),
                  child: InkWell(
                    onTap: (){
                      showHotOrHistory('history');
                    },
                    child: Text(
                      "搜索历史",
                      style: TextStyle(color: Colors.grey),
                    ),
                  ),
                ),
              ],
            ),
            SizedBox(
              height: 10,
            ),
            Expanded(
              child: CourseFilterWidget(widget.search, widget.isCharge),
            ),
          ],
        ),
      ),
    );
  }

  List hotSearch1 = ["前端", "后端", "数据库", "基础", "运维", "测试", "游戏", "全栈", "人工智能", "github"];
  List hotSearch2 = ["大数据", "云计算", "爬虫", "算法", "自动化", "区块链", "深度学习"];

  bool showHotMore = true;

  showHotOrHistory (String type) {
    showModalBottomSheet(context: context, builder: (BuildContext context) {
      return StatefulBuilder(
          builder:(context1, state) {///这里的state就是setState
            if (type == "hot") {
              return showHotWidget(state, context);
            } else {
              return showHistoryWidget(state, context);
            }
          });
    });
  }

  /// Flutter 更新ModalBottomSheet中的状态，为了区分把 setState 改个名字
  Widget showHotWidget (Function _setState, BuildContext context) {
    List hotSearch = [];
    hotSearch..addAll(hotSearch1)..addAll(showHotMore ? [] : hotSearch2);

    return Container(
      height: MediaQuery.of(context).size.height / 2,
      padding: EdgeInsets.all(10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("大家都在搜"),
          SizedBox(height: 10,),
          // Wrap是一个可以使子控件自动换行的控件，默认的方向是水平的
          Wrap(
            spacing: 5, //主轴上子控件的间距
            runSpacing: 5, //交叉轴上子控件之间的间距
            children: List.generate(hotSearch.length, (index) {
              return CommonLabel.getCommonLabel2(hotSearch[index]);
            }),
          ),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 5),
            child: Row(
              children: <Widget>[
                Expanded(
                  child: Divider(thickness: 2, color: Colors.grey,),
                ),
                SizedBox(width: 10,),
                InkWell(
                  onTap: () {
                    _setState(() {      ///为了区分把setState改个名字
                      showHotMore = !showHotMore;
                    });
                  },
                  child: Padding(
                    padding: EdgeInsets.all(5),
                    child: Row(
                      children: <Widget>[
                        Container(
                          alignment: Alignment.center,
                          decoration: BoxDecoration(
                            color: Colors.grey[600],
                            borderRadius: BorderRadius.all(Radius.circular(10)),
                          ),
                          child: Icon(showHotMore ? Icons.keyboard_arrow_down : Icons.keyboard_arrow_up, size: 20, color: Colors.white,),
                        ),
                        SizedBox(width: 10,),
                        Text(showHotMore ? "查看更多" : "收起"),
                      ],
                    ),
                  ),
                ),
                SizedBox(width: 10,),
                Expanded(
                  child: Divider(thickness: 2, color: Colors.grey,),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  List<String> searchHistory = [];

  getSearchHistory (Function _setState) async {
    List<String> _searchHistory = await CommonUtil.getSearchHistory();
    _setState(() {
      searchHistory = _searchHistory;
    });
  }

  Widget showHistoryWidget (Function _setState, BuildContext context) {
    getSearchHistory(_setState);

    return Container(
      height: MediaQuery.of(context).size.height / 2,
      child: Padding(
        padding: EdgeInsets.all(10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Row(
              children: <Widget>[
                Text("搜索历史"),
                Expanded(child: Text("")),
                InkWell(
                  onTap: () {
                    CommonUtil.clearHistory();
                  },
                  child: Text("清空"),
                ),
              ],
            ),
            SizedBox(height: 10),
            Wrap(
              spacing: 5, //主轴上子控件的间距
              runSpacing: 5, //交叉轴上子控件之间的间距
              children: List.generate(searchHistory.length, (index) {
                return CommonLabel.getCommonLabel2(searchHistory[index]);
              }),
            ),
          ],
        ),
      ),
    );
  }
}
