import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/customtag_course_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class CustomTagCoursePage extends StatefulWidget {
  String custom_tag;

  CustomTagCoursePage(this.custom_tag);

  @override
  _CustomTagCoursePageState createState() => _CustomTagCoursePageState();
}

class _CustomTagCoursePageState extends State<CustomTagCoursePage> {
  bool showGrid = true;

  List<CustomTagCourse> customTagCourses = [];
  String custom_tag_name = "";
  Paginator paginator;

  @override
  void initState() {
    super.initState();

    initData();
  }

  initData() {
    loadPageData(1);
  }

  loadPageData(int current_page) async {
    CustomTagCourseResponse customTagCourseResponse =
        await LinkKnownApi.queryCustomTagCourse(
            widget.custom_tag, current_page, 10);
    if (customTagCourseResponse.status == "SUCCESS") {
      setState(() {
        paginator = customTagCourseResponse.paginator;
        if (current_page == 1) {
          customTagCourses.clear();
        }
        customTagCourses.addAll(customTagCourseResponse.customTagCourses);
        custom_tag_name =
            customTagCourseResponse.customTagCourses.first.customTagName;
      });
    }
  }

  loadNextPageData() async {
    if (paginator != null) {
      if (paginator.currpage < paginator.totalpages) {
        loadPageData(paginator.currpage + 1);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('链知课堂'),
        centerTitle: true,
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
        actions: <Widget>[
          IconButton(
            icon: SvgPicture.asset(
              showGrid ? "images/icon_grid.svg" : "images/icon_list.svg",
              color: Colors.white,
              width: 20,
              height: 20,
            ),
            onPressed: () {
              setState(() {
                showGrid = !showGrid;
              });
            },
          ),
        ],
      ),
      body: Container(
        padding: EdgeInsets.all(10),
        child: ScrollConfiguration(
          behavior: NoShadowScrollBehavior(),
          child: getBodyWidget(),
        ),
      ),
    );
  }

  Widget getBodyWidget() {
    if (showGrid) {
      return getGridBodyWidget();
    } else {
      return getListBodyWidget();
    }
  }

  Widget getGridBodyWidget() {
    // 动态计算子元素宽高比
//    double cellWidth = ((MediaQuery.of(context).size.width - allHorizontalPadding) / columnCount);
    double cellWidth = ((MediaQuery.of(context).size.width - 40) / 3);
    double desiredCellHeight = 150;
    double _childAspectRatio = cellWidth / desiredCellHeight;

    return GridView.builder(
        physics: NeverScrollableScrollPhysics(),
        shrinkWrap: true,
        itemCount: customTagCourses.length,
        //controller: scrollController,//注释掉就可以达到个人中心协调效果了
        // SliverGridDelegateWithFixedCrossAxisCount 构建一个横轴固定数量Widget
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
          //横轴元素个数
          crossAxisCount: 3,
          //纵轴间距
          mainAxisSpacing: 10.0,
          //横轴间距
          crossAxisSpacing: 10.0,
          //子组件宽高长度比例
          childAspectRatio: _childAspectRatio,
        ),
        itemBuilder: (BuildContext context, int index) {
          return getCustomTagCourseWidget(customTagCourses[index]);
        });
  }

  Widget getListBodyWidget() {
    return ListView.builder(
      shrinkWrap: true,
      itemCount: customTagCourses.length,
      itemBuilder: (BuildContext context, int position) {
        return Container(
          padding: EdgeInsets.all(5),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Image.network(
                UIUtils.replaceMediaUrl(customTagCourses[position].smallImage),
                height: 80,
                width: 120,
                fit: BoxFit.fill,
              ),
              SizedBox(width: 10,),
              Container(
                  child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(customTagCourses[position].courseName),
                  Text(
                      "${customTagCourses[position].courseType}/${customTagCourses[position].courseSubType}"),
                  Text(customTagCourses[position].courseShortDesc),
                ],
              )),
            ],
          ),
        );
      },
    );
  }

  Widget getCustomTagCourseWidget(CustomTagCourse customTagCourse) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        GestureDetector(
          onTap: () {
            NavigatorUtil.goRouterPage(context,
                "${Routes.courseDetail}?course_id=${customTagCourse.id}");
          },
          child: ClipRRect(
            borderRadius: BorderRadius.circular(5),
            child: Image.network(
              UIUtils.replaceMediaUrl(customTagCourse.smallImage),
              height: 100,
              width: double.infinity,
              fit: BoxFit.fill,
            ),
          ),
        ),
        Text(
          customTagCourse.courseName,
          style: TextStyle(color: Colors.black, fontWeight: FontWeight.w300),
        ),
        Text("${customTagCourse.courseType}/${customTagCourse.courseSubType}",
            overflow: TextOverflow.ellipsis,
            style: TextStyle(color: Colors.grey[700], fontSize: 12)),
      ],
    );
  }
}
