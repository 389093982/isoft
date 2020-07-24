
import 'package:flutter/material.dart';
import 'package:linkknown/widgets/common_label.dart';

class CommonHeaderWidget extends StatefulWidget {

  String headerText;
  VoidCallback onClickCallBack;


  CommonHeaderWidget(this.headerText, {this.onClickCallBack});

  @override
  _CommonHeaderState createState() => _CommonHeaderState();
}

class _CommonHeaderState extends State<CommonHeaderWidget> {
  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Text(
          "${widget.headerText}",
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        ),
        Expanded(child: Text("")),
        GestureDetector(
          onTap: widget.onClickCallBack,
          child: CommonLabel.getCommonLabel3("查看更多"),
        ),
      ],
    );
  }

}

class CommonRefreshFooterWidget extends StatefulWidget {

  VoidCallback onClickCallBack;
  String text;

  CommonRefreshFooterWidget({this.onClickCallBack, this.text = "换一拨推荐"});

  @override
  _CommonRefreshFooterState createState() => _CommonRefreshFooterState();
}

class _CommonRefreshFooterState extends State<CommonRefreshFooterWidget> with TickerProviderStateMixin {

  AnimationController controller;

  @override
  void initState() {
    super.initState();

    controller = AnimationController(duration: const Duration(seconds: 2), vsync: this);
    controller.addStatusListener((status) {
      // 旋转动画执行完成后再次点击则可以重新执行
      if (controller.status == AnimationStatus.completed) {
        controller.reset();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Expanded(
          child: Divider(color: Colors.black45, height: 2,),
        ),
        SizedBox(width: 10,),
        InkWell(
          onTap: (){
            controller.forward();
            widget.onClickCallBack();
          },
          child: Padding(
            padding: EdgeInsets.symmetric(vertical: 5, horizontal: 5),
            child: Row(
              children: <Widget>[
                Text(widget.text),
                Transform.rotate(angle: 45, child: RotationTransition(
                  turns: controller,
                  child: Icon(Icons.autorenew, size: 16,),
                ),),
              ],
            ),
          ),
        ),
        SizedBox(width: 10,),
        Expanded(
          child: Divider(color: Colors.black45, height: 2,),
        ),
      ],
    );
  }

  @override
  void dispose() {
    super.dispose();
    controller?.dispose();
  }

}