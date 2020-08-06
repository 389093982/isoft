import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/utils/utils.dart';

enum HeaderIconSize {
  SIZE_BIG_80,                //用与设置大头像：登录、‘我的’、...
  SIZE_NORMAL_50,             //正常头像：课程详情里面作者、发表博客...
  SIZE_SMALL_40,             //评论的头像 (比正常的要小一点点)、首页
}

class HeaderIconWidget extends StatefulWidget {
  String headerIcon;
  HeaderIconSize size;

  double width;
  double height;

  HeaderIconWidget(this.headerIcon, this.size);

  @override
  _HeaderIconWidgetState createState() => _HeaderIconWidgetState();
}

class _HeaderIconWidgetState extends State<HeaderIconWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    checkWidgetSize();

    if (widget.headerIcon == null || widget.headerIcon == "") {
      return ClipOval(
        child: Image.asset(
          "images/not_found.png",
          width: widget.width,
          height: widget.height,
          fit: BoxFit.fill,
        ),
      );
    }
    return ClipOval(
      child: CachedNetworkImage(
        imageUrl:  UIUtils.replaceMediaUrl(widget.headerIcon),
        fit: BoxFit.fill,
        width: widget.width,
        height: widget.height,
        placeholder: (context, url) => Image.asset(
          "images/unlogin_head_no_circle.png",
          fit: BoxFit.fill,
          width: widget.width,
          height: widget.height,
        ),
        errorWidget: (context, url, error) => Image.asset(
          "images/not_found.png",
          fit: BoxFit.fill,
          width: widget.width,
          height: widget.height,
        ),
      ),
    );
  }

  void checkWidgetSize() {
    if (widget.size != null) {
      if (widget.size == HeaderIconSize.SIZE_BIG_80) {
        widget.width = 80;
        widget.height = 80;
      } else if (widget.size == HeaderIconSize.SIZE_NORMAL_50) {
        widget.width = 50;
        widget.height = 50;
      } else if (widget.size == HeaderIconSize.SIZE_SMALL_40) {
        widget.width = 40;
        widget.height = 40;
      }
    }
  }
}
