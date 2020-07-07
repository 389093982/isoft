import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/divider_line.dart';

class AdviseEditPage extends StatefulWidget {
  @override
  _AdviseEditPageState createState() => _AdviseEditPageState();
}

class _AdviseEditPageState extends State<AdviseEditPage> {
  String adviseType = "";
  TextEditingController _adviseController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('意见反馈'),
        centerTitle: true,
        leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      // SafeArea（安全区域）: 异形屏适配利器, 优化页面显示区域
      body: SafeArea(
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              getChooseAdviseTypeWidget(),
              DividerLineView(),
              getAdivseEditWidget(),
              DividerLineView(),
              getSubmitWidget(),
            ],
          ),
        ),
      ),
    );
  }

  Widget getSubmitWidget() {
    return Container(
      height: ScreenUtil().setHeight(100),
      margin: EdgeInsets.only(top: ScreenUtil().setHeight(50)),
      padding: EdgeInsets.symmetric(horizontal: ScreenUtil().setWidth(20)),
      child: MaterialButton(
        color: Colors.deepOrangeAccent,
        splashColor: Colors.deepOrange,
        minWidth: double.infinity,
        onPressed: _submit,
        child: Text(
          "提交",
          style: TextStyle(
            color: Colors.white,
            fontSize: ScreenUtil().setSp(40),
          ),
        ),
      ),
    );
  }

  _submit () {
    if (adviseType.isEmpty) {
      UIUtils.showToast2("请选择反馈类型");
      return;
    }
    if (_adviseController.text.isEmpty) {
      UIUtils.showToast2("请输入您的反馈内容");
      return;
    }
    UIUtils.showToast2("提交成功");
  }

  Widget getAdivseEditWidget() {
    return Container(
      margin: EdgeInsets.all(ScreenUtil().setWidth(20.0)),
      width: double.infinity,
      height: ScreenUtil().setHeight(400),
      child: TextField(
        controller: _adviseController,
        maxLines: 15,
        style: TextStyle(
          color: Colors.black54,
          fontSize: ScreenUtil().setSp(26),
        ),
        decoration: InputDecoration(
          hintText: "请输入您的反馈内容",
          hintStyle: TextStyle(
            color: Colors.grey,
            fontSize: ScreenUtil().setSp(26),
          ),
          focusedBorder: OutlineInputBorder(
            borderSide:
                BorderSide(color: Colors.grey, width: ScreenUtil().setWidth(1)),
          ),
          enabledBorder: OutlineInputBorder(
            borderSide:
                BorderSide(color: Colors.grey, width: ScreenUtil().setWidth(1)),
          ),
        ),
      ),
    );
  }

  Widget getChooseAdviseTypeWidget() {
    return InkWell(
      onTap: _showChooseAdivseTypeDialog,
      child: Container(
        height: ScreenUtil().setHeight(100),
        padding: EdgeInsets.symmetric(horizontal: 20),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Text("请选择反馈类型",
                style: TextStyle(
                  color: Colors.black54,
                  fontSize: ScreenUtil().setSp(26),
                )),
            Expanded(
                child: Container(
                    alignment: Alignment.centerRight,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: <Widget>[
                        Text(
                          adviseType,
                          style: TextStyle(
                              color: Colors.grey,
                              fontSize: ScreenUtil().setSp(26)),
                        ),
                        Padding(
                            padding: EdgeInsets.only(
                                left: ScreenUtil().setWidth(10))),
                        Icon(
                          Icons.navigate_next,
                          color: Colors.grey[350],
                          size: ScreenUtil().setWidth(40),
                        )
                      ],
                    )))
          ],
        ),
      ),
    );
  }

  Widget getAdviseTypeItem(String text) {
    return SimpleDialogOption(
        onPressed: () {
          Navigator.pop(context);
          setState(() {
            adviseType = text;
          });
        },
        child: Container(
          alignment: Alignment.centerLeft,
          height: ScreenUtil().setHeight(80.0),
          child: Text(
            text,
            style: TextStyle(
                color: Colors.black54, fontSize: ScreenUtil().setSp(26.0)),
          ),
        ));
  }

  _showChooseAdivseTypeDialog() {
    showDialog(
        context: context,
        barrierDismissible: true,
        builder: (BuildContext context) {
          return SimpleDialog(
            title: Text(
              "请选择反馈类型",
              style: TextStyle(
                  color: Colors.black54, fontSize: ScreenUtil().setSp(28.0)),
            ),
            children: <Widget>[
              getAdviseTypeItem("功能异常"),
              DividerLineView(),
              getAdviseTypeItem("优化建议"),
              DividerLineView(),
              getAdviseTypeItem("其它"),
            ],
          );
        });
  }
}
