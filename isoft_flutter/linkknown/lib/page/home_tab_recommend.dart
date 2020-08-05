import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/customtag_course_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_header_footer.dart';
import 'package:linkknown/widgets/swiper_info.dart';

class TabRecommendWidget extends StatefulWidget {
  @override
  _TabRecommenState createState() => _TabRecommenState();
}

class _TabRecommenState extends State<TabRecommendWidget>
    with TickerProviderStateMixin {
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      //滑动的方向 Axis.vertical为垂直方向滑动，Axis.horizontal 为水平方向
      scrollDirection: Axis.vertical,
      //true 滑动到底部
      reverse: false,
      padding: EdgeInsets.all(0.0),
      ////滑动到底部回弹效果
      physics: BouncingScrollPhysics(),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          SwiperDataWidget(),
          SpecialContentWidget(),
          CustomCourseWidget("hot"),
          CustomCourseWidget("recommend"),
          CustomCourseWidget("views"),
        ],
      ),
    );
  }
}

class SpecialContentWidget extends StatefulWidget {
  @override
  _SpecialContentState createState() => _SpecialContentState();
}

class _SpecialContentState extends State<SpecialContentWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(10),
      child: Column(
        children: <Widget>[
          CommonHeaderWidget("特色功能"),
          SizedBox(height: 5,),
          GridView.count(
            physics: NeverScrollableScrollPhysics(),
            shrinkWrap: true,
            //水平子 Widget 之间间距
            crossAxisSpacing: 10.0,
            //垂直子 Widget 之间间距
            mainAxisSpacing: 10.0,
            //GridView内边距
//            padding: EdgeInsets.all(10.0),
            //一行的Widget数量
            crossAxisCount: 2,
            //子Widget宽高比例
            childAspectRatio: 2.0,
            //子Widget列表
            children: <Widget>[
              ClipRRect(
                borderRadius: BorderRadius.circular(5),
                child: Image.asset(
                  "images/image_wenjuan.png",
                  fit: BoxFit.fill,
                ),
              ),
              ClipRRect(
                borderRadius: BorderRadius.circular(5),
                child: Image.asset(
                  "images/image_kaoshi.png",
                  fit: BoxFit.fill,
                ),
              ),
            ],
          )
        ],
      ),
    );
  }
}



class CustomCourseWidget extends StatefulWidget {

  String custom_tag;

  CustomCourseWidget(this.custom_tag);

  @override
  _CustomCourseState createState() => _CustomCourseState();
}

class _CustomCourseState extends State<CustomCourseWidget> {

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

  loadPageData (int current_page) async {
    CustomTagCourseResponse customTagCourseResponse = await LinkKnownApi.queryCustomTagCourse(widget.custom_tag, current_page, 6);
    if (customTagCourseResponse.status == "SUCCESS") {
      setState(() {
        paginator = customTagCourseResponse.paginator;
        customTagCourses = customTagCourseResponse.customTagCourses;
        custom_tag_name = customTagCourseResponse.customTagCourses.first.customTagName;
      });
    }
  }

  loadNextPageData () async {
    if (paginator != null) {
      if (paginator.currpage < paginator.totalpages) {
        loadPageData(paginator.currpage + 1);
      } else {
        loadPageData(1);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    // 动态计算子元素宽高比
//    double cellWidth = ((MediaQuery.of(context).size.width - allHorizontalPadding) / columnCount);
    double cellWidth = ((MediaQuery.of(context).size.width - 40) / 3);
    double desiredCellHeight = 150;
    double _childAspectRatio = cellWidth / desiredCellHeight;

    return Container(
      padding: EdgeInsets.all(10),
      child: Column(
        children: <Widget>[
          CommonHeaderWidget(custom_tag_name, onClickCallBack: () {
            NavigatorUtil.goRouterPage(context, "${Routes.customTagCourse}?custom_tag=${widget.custom_tag}");
          },),
          SizedBox(height: 5,),
          GridView.builder(
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
              }),
          CommonRefreshFooterWidget(onClickCallBack: () {
            loadNextPageData();
          },),
        ],
      ),
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
              UIUtils.replaceMediaUrl(customTagCourse.smallImage), height: 100,
              width: double.infinity,
              fit: BoxFit.fill,),
          ),
        ),
        Text(customTagCourse.courseName, style: TextStyle(color: Colors.black, fontWeight: FontWeight.w300),overflow: TextOverflow.ellipsis,),
        Text("${customTagCourse.courseType}/${customTagCourse.courseSubType}",
            overflow: TextOverflow.ellipsis,
            style: TextStyle(color: Colors.grey[700], fontSize: 12)),
      ],
    );
  }

}