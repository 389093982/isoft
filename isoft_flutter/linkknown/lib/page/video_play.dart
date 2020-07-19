
import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/page/course_video_widget.dart';
import 'package:linkknown/utils/utils.dart';

class VideoPlayPage extends StatefulWidget {

  Course course;
  List<CVideo> cVideos;
  int index;  // 播放当前视频的索引

  VideoPlayPage(this.course, this.cVideos, this.index);

  @override
  VideoPlayState createState() => VideoPlayState();

}

class VideoPlayState extends State<VideoPlayPage> {

  FijkPlayer player;

  @override
  void initState() {
    super.initState();

    initVideoData();
  }

  void initVideoData() {
    if (player != null) {
      player.release();
      player.dispose();
    }

    player = FijkPlayer();
    player.setDataSource(UIUtils.replaceMediaUrl(widget.cVideos[widget.index].firstPlay), autoPlay: true);
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
          ),
          Padding(
            padding: EdgeInsets.all(10),
            child: CourseVideosWidget(widget.course, widget.cVideos, clickCallBack: _clickCallBack,),
          ),
        ],
      ),
    );
  }

  _clickCallBack (index){
    widget.index = index;

    initVideoData();

    setState(() {});
  }

  @override
  void dispose() {
    super.dispose();
    player.release();
  }

}
