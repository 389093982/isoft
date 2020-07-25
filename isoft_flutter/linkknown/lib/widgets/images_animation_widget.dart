
import 'package:flutter/material.dart';
import 'package:sprintf/sprintf.dart';

// 多张图片来回切换动画
class ImagesAnimationWidget extends StatefulWidget {

  final double width;
  final double height;
  final ImagesAnimationEntry entry;
  final int durationSeconds;

  ImagesAnimationWidget({Key key, this.width : 80, this.height : 80, this.entry, this.durationSeconds : 3}):super(key:key);

  @override
  _ImagesAnimationState createState() => _ImagesAnimationState();
}

class _ImagesAnimationState extends State<ImagesAnimationWidget> with TickerProviderStateMixin{

  AnimationController _controller;
  Animation<int> _animation;

  @override
  void initState() {
    super.initState();
    _controller = new AnimationController(vsync: this, duration: Duration(seconds: widget.durationSeconds))
      ..repeat();
    _animation = new IntTween(begin: widget.entry.lowIndex, end: widget.entry.highIndex).animate(_controller);
    //widget.entry.lowIndex 表示从第几下标开始，如0；widget.entry.highIndex表示最大下标：如 7

    _controller.forward();
  }

  @override
  Widget build(BuildContext context) {
    return new AnimatedBuilder(
      animation: _animation,
      builder: (BuildContext context, Widget child) {
        String frame = _animation.value.toString();
        return new Image.asset(
          sprintf(widget.entry.basePath, [frame]), //根据传进来的参数拼接路径
          gaplessPlayback: true, //避免图片闪烁
          width: widget.width,
          height: widget.height,
        );
      },
    );
  }

  @override
  void dispose() {
    super.dispose();
    _controller?.dispose();
  }
}

class ImagesAnimationEntry {
  int lowIndex = 0;
  int highIndex = 0;
  String basePath;

  ImagesAnimationEntry(this.lowIndex, this.highIndex, this.basePath);
}