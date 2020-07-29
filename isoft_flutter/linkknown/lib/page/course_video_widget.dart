
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/utils/string_util.dart';

class CourseVideosWidget extends StatefulWidget {

  Course course;
  List<CVideo> cVideos;

  // 定义接收父类回调函数的指针
  ValueChanged<int> clickCallBack;

  CourseVideosWidget(this.course, this.cVideos, {Key key,this.clickCallBack}): super(key: key);

  @override
  CourseVideosWidgetState createState() => CourseVideosWidgetState();
}

class CourseVideosWidgetState extends State<CourseVideosWidget> {
  // 是否是列表模式,默认是列表模式
  bool isListPattern = true;
  // 默认顺序
  bool order_asc = true;

  // 已播放历史
  List<String> playHistories = [];
  // 当前正在播放的视频 id
  int currentVideoId = -1;

  void updateState ({int currentVideoId = -1}) async {
    if (currentVideoId > 0) {
      this.currentVideoId = currentVideoId;
    } else {
      CourseVideoCurrentPlaying playing = await CommonUtil.readVideoPlaying();
      if (playing.courseId == widget.course.id){
        this.currentVideoId = playing.videoId;
      }
    }
    initVideoPlayHistory();
  }

  @override
  void initState() {
    super.initState();

    initVideoPlayHistory();
  }

  void initVideoPlayHistory () async {
    // 记录观看记录
    List<String> playHistories = await CommonUtil.readVideoPlayHistory(widget.course.id);
    setState(() {
      this.playHistories = playHistories;
    });
  }

  // TODO 顺序倒序
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        Container(
          color: Colors.grey[200],
          padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
          child: Row(
            children: <Widget>[
              SvgPicture.asset("images/ic_play2.svg", width: 25, height: 25, fit: BoxFit.fill, color: Colors.deepOrangeAccent,),
              SizedBox(width: 5,),
              Text("分集视频", style: LinkKnownTextStyle.commonTitle2,),
              SizedBox(width: 5,),
              Text("(共${(widget.cVideos??[]).length}集)"),
              // 中间用Expanded控件
              Expanded(
                child: Text(''),
              ),
              GestureDetector(
                onTap: (){
                  setState(() {
                    isListPattern = !isListPattern;
                  });
                },
                child: Padding(
                  padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
                  child: SvgPicture.asset(
                    isListPattern ? "images/icon_list.svg" : "images/icon_grid.svg",
                    width: ScreenUtil().setWidth(30),
                    height: ScreenUtil().setHeight(30),
                    color: Colors.deepOrangeAccent,
                  ),
                ),
              ),
              GestureDetector(
                onTap: (){
                  setState(() {
                    order_asc = !order_asc;
                  });
                },
                child: Padding(
                  padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
                  child: Row(
                    children: <Widget>[
                      SvgPicture.asset(
                        order_asc ? "images/order_asc.svg" : "images/order_desc.svg",
                        width: ScreenUtil().setWidth(40),
                        height: ScreenUtil().setHeight(40),
                        color: Colors.deepOrangeAccent,
                      ),
                      SizedBox(width: 5,),
                      Text( order_asc ? "正序" : "倒序",),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
        // Flexible，Expanded,Spacer这三个小控件用于Row, Column, or Flex这三个容器；
        // Spacer
        // 顾名思义只是一个间距控件，可以用于调节小部件之间的间距，它有一个flex可以进行设置；
        // Expanded
        // Expanded会尽可能的充满分布在Row, Column, or Flex的主轴方向上；
        // Flexible
        // Flexible也是为小部件提供空间的，但是不会要求子空间填满可用空间；
        Flexible(     // 此处使用 Flexible 解决溢出问题
          child: isListPattern ? getListWidget() : getGridWidget(),
        ),
      ],
    );
  }
  
  Widget getGridWidget () {
    return GridView.count(
      reverse: !order_asc,      // 是否反序
      shrinkWrap: true,
      //水平子 Widget 之间间距
      crossAxisSpacing: 10.0,
      //垂直子 Widget 之间间距
      mainAxisSpacing: 10.0,
      //GridView内边距
      padding: EdgeInsets.all(10.0),
      //一行的Widget数量
      crossAxisCount: 5,
      //子Widget宽高比例
      childAspectRatio: 1.0,
      physics: new NeverScrollableScrollPhysics(), // 解决嵌套滑动问题：禁用滑动事件
      //子Widget列表
      children: (widget.cVideos??[]).asMap().keys.map((index) {
        return GestureDetector(
          onTap: () {
            widget.clickCallBack(index);
          },
          child: Container(
            margin: EdgeInsets.all(5),
            padding: EdgeInsets.all(10),
            color: Colors.grey[200],
            child: Center(
              child: Text("${index + 1}"),
            ),
          ),
        );
      }).toList(),
    );
  }

  Widget getListWidget(){
    return ListView.builder(
        reverse: !order_asc,      // 是否反序
        shrinkWrap: true,
        itemCount: (widget.cVideos ?? []).length,
        physics: new NeverScrollableScrollPhysics(), // 解决嵌套滑动问题：禁用滑动事件
        itemBuilder: (BuildContext context, int index) {
          return InkWell(
            onTap: () {
              widget.clickCallBack(index);
            },
            child: Column(
              children: <Widget>[
                Container(
                  padding: EdgeInsets.symmetric(vertical: 10, horizontal: 10),
                  child: Row(
                    children: <Widget>[
                      Text("${index+1}", style: TextStyle(color: renderColor(widget.cVideos[index].id)),),
                      Container(
                        width: 20,
                      ),
                      Text(StringUtil.getFileName(widget.cVideos[index].videoName), style: TextStyle(color: renderColor(widget.cVideos[index].id)),),
                      Expanded(child: Text(""),),
                      SvgPicture.asset("images/ic_clock.svg", width: 15, height: 15, fit: BoxFit.fill, color: renderColor(widget.cVideos[index].id),),
                      Text("${CommonUtil.formateDuration(widget.cVideos[index].duration)}", style: TextStyle(color: renderColor(widget.cVideos[index].id), fontSize: 12),),
                    ],
                  ),
                ),
                Divider(height: 2, color: Colors.grey[400],),
              ],
            ),
          );
        });
  }

  Color renderColor (int videoId) {
    if (currentVideoId == videoId) {
      return Colors.deepOrangeAccent;
    }
    else if (playHistories.contains(videoId.toString())) {
      return Colors.grey[500];
    }
    return Colors.black;
  }
}