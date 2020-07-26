import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/utils/utils.dart';

enum HeaderIconSize {
  SIZE_SMALL,
  SIZE_NORMAL,
  SIZE_BIG,
}

class HeaderIconWidget extends StatefulWidget {
  String headerIcon;
  double width;
  double height;
  HeaderIconSize size;

  HeaderIconWidget(this.headerIcon,
      {this.size, this.width = 30, this.height = 30});

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
      if (widget.size == HeaderIconSize.SIZE_SMALL) {
        widget.width = 10;
        widget.height = 10;
      } else if (widget.size == HeaderIconSize.SIZE_NORMAL) {
        widget.width = 30;
        widget.height = 30;
      } else if (widget.size == HeaderIconSize.SIZE_BIG) {
        widget.width = 60;
        widget.height = 60;
      }
    }
  }
}
