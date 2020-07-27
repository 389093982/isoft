import 'package:flutter/material.dart';
import 'package:linkknown/widgets/images_animation_widget.dart';

enum LoadingStatus {
  LOADING, // 加载中
  LOADED_COMPLETED, // 加载完成
  LOADED_COMPLETED_ALL, // 加载完成，没有更多数据
  LOADED_FAILED, // 加载失败
  LOADED_EMPTY, // 暂无数据
  NET_ERROR, // 网络异常
}

class FooterLoadingWidget extends StatefulWidget {
  dynamic loadingStatus;
  ValueChanged refreshOnFailCallBack;

  FooterLoadingWidget({
    this.loadingStatus = LoadingStatus.LOADED_COMPLETED,
    this.refreshOnFailCallBack,
  });

  @override
  _FooterLoadingState createState() => _FooterLoadingState();
}

class _FooterLoadingState extends State<FooterLoadingWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(10),
      child: GestureDetector(
        onTap: _onTap,
        child: Center(
          child: getLoadingStatusWidget(),
        ),
      ),
    );
  }

  Widget getLoadingStatusWidget() {
    // 空布局
    if (widget.loadingStatus == LoadingStatus.LOADED_EMPTY) {
      return Image.asset("images/no_data.png");
    } else {
      // 其它布局
      return Row(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          getLoadingImageWidget(),
          getLoadingStatusTextWidget(),
        ],
      );
    }
  }

  Widget getLoadingImageWidget() {
    if (widget.loadingStatus == LoadingStatus.LOADING) {
      return Padding(
        padding: EdgeInsets.only(right: 10),
        child: ImagesAnimationWidget(
          width: 15,
          height: 15,
          entry: ImagesAnimationEntry(
            1,
            7,
            "images/loading_0%s.png",
          ),
          durationSeconds: 3,
        ),
      );
    } else {
      return SizedBox(
        width: 1,
      );
    }
  }

  Widget getLoadingStatusTextWidget() {
    String statusText = "";
    if (widget.loadingStatus == LoadingStatus.LOADING) {
      statusText = "正在加载中...";
    } else if (widget.loadingStatus == LoadingStatus.LOADED_COMPLETED) {
      statusText = "查看更多数据";
    } else if (widget.loadingStatus == LoadingStatus.LOADED_COMPLETED_ALL) {
      statusText = "没有更多数据";
    } else if (widget.loadingStatus == LoadingStatus.LOADED_FAILED) {
      statusText = "加载失败，请点我重试";
    }
    return Text(
      statusText,
      style: TextStyle(fontSize: 12, color: Colors.grey[500]),
    );
  }

  _onTap() {
    if (widget.loadingStatus == LoadingStatus.LOADED_EMPTY ||
        widget.loadingStatus == LoadingStatus.NET_ERROR ||
        widget.loadingStatus == LoadingStatus.LOADED_FAILED) {
      widget.refreshOnFailCallBack(widget.loadingStatus);
    }
  }
}
