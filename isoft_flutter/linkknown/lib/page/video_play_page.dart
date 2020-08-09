
import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/response/course_detail_response.dart';
import 'package:linkknown/page/course_video_widget.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';

class VideoPlayPage extends StatefulWidget {

  Course course;
  List<CVideo> cVideos;
  int index;  // 播放当前视频的索引

  VideoPlayPage(this.course, this.cVideos, this.index);

  @override
  VideoPlayState createState() => VideoPlayState();

}

class VideoPlayState extends State<VideoPlayPage> {

  bool hasPaid = false;

  FijkPlayer player = FijkPlayer();
  GlobalKey<CourseVideosWidgetState> courseVideosWidgetKey = new GlobalKey();

  @override
  void initState() {
    //查询课程是否已经购买过
    queryHasPaid();
    super.initState();

    player.addListener(() {
      // 连播模式
      if (player.value.state == FijkState.completed && widget.index < widget.cVideos.length - 1) {
        playVideoByIndex(widget.index + 1);
      }
    });
    initVideoData();
  }

  //查询课程是否已经购买
  queryHasPaid() async {
    String user_name = await LoginUtil.getLoginUserName();
    if(StringUtil.checkEmpty(user_name)){
      return;
    }
    String goods_type = "course_theme_type";
    String goods_id = widget.course.id.toString();
    String pay_result = "SUCCESS";
    int current_page = 1;
    int pageSize = 10;
    LinkKnownApi.queryPayOrderListIsPaid(current_page, pageSize, goods_type, goods_id, user_name, pay_result).then((PayOrderResponse) async {
      if (PayOrderResponse.status == "SUCCESS") {
        if(PayOrderResponse.orders.length>0){
          this.hasPaid = true;
        }
      }
    }).catchError((err) {
      AutoLoginDialogHelper.checkCanShowUnLoginDialog(context, err);
    });
  }

  void initVideoData() async {
    // 记录观看记录
    CommonUtil.recordVideoPlayHistory(widget.course.id, widget.cVideos[widget.index].id);

    // 更新分集列表中的视频状态(样式)
    int delayedSecond = 0;
    // 没有 mounted 挂载则延迟 1 s之后再进行父子组件通信
    if (courseVideosWidgetKey.currentState == null) {
      delayedSecond = 1;
    }
    Future.delayed(Duration(seconds: delayedSecond), (){
      courseVideosWidgetKey.currentState?.updateState(currentVideoId:widget.cVideos[widget.index].id);
    });

    // 设置播放器播放源
    await player.setDataSource(UIUtil.replaceMediaUrl(widget.cVideos[widget.index].firstPlay), autoPlay: true, showCover: true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: <Widget>[
          FijkView(
            width: double.infinity,
            height: 250,
            // fit 实际控制 FijkView 的显示填充和裁剪模式。是本节内容重点。
            // fsFit 参数控制 FijkView 在全屏模式下的填充和裁剪模式。fsFit 取值的含义和 fit 相同。
            fit: FijkFit.cover,
            fsFit: FijkFit.cover,
            player: player,
            // TODO 封面图设置未生效
            // FijkView 中的参数 cover 可用于设置封面图。 cover 是 ImageProvider 类型。
            // 视频加载过程中，封面图显示在 FijkView 中，填充了 FijkView 中计算出的实际视频区域。
            // 视频开始播放后封面图不再显示。
            cover: NetworkImage(widget.course.smallImage,),
          ),
          Expanded(
            child: ListView(
              children: <Widget>[
                Container(
                  padding: EdgeInsets.all(10),
                  child: CourseVideosWidget(widget.course, widget.cVideos, clickCallBack: playVideoByIndex, key: courseVideosWidgetKey,),
                )
              ],
            ),
          ),
        ],
      ),
    );
  }

  playVideoByIndex (index) async {
    String loginUserName = await LoginUtil.getLoginUserName();
    String vipLevel = await LoginUtil.getVipLevel()??"0";
    String vipExpiredTime = DateUtil.format2StandardTime(await LoginUtil.getVipExpiredTime()??"19700101235959");
    String nowTime = DateUtil.getNow_yyyyMMddHHmmss();
    bool compare = DateUtil.compareStandardTime(vipExpiredTime, nowTime);

    if(widget.course.isCharge=="vip"){
      if((int.parse(vipLevel)>0 && compare) || widget.course.courseAuthor==loginUserName){
        toPlay(index);
      }else{
        UIUtil.showToast2("会员专享课程!");
      }
    }else{
      if(widget.course.isCharge=="free" || index+1<=widget.course.preListFree || widget.course.courseAuthor==loginUserName || this.hasPaid){
        toPlay(index);
      }else{
        UIUtil.showToast2("付费课程,请先购买");
      }
    }
  }

  //播放
  toPlay(index) async {
    widget.index = index;
    // 重置播放器进入 idle 状态，可以再次 setDataSource
    await player.reset();
    await initVideoData();
    setState(() {});
  }


  @override
  void dispose() {
    //录观看记录
    CommonUtil.recordVideoPlayHistory(widget.course.id, widget.cVideos[widget.index].id, progress: player.currentPos.inSeconds);

    player?.release();
    super.dispose();
  }
}
