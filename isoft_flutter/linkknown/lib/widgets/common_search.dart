import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

typedef HandleSearch<T> = void Function(T value);

class SearchInputWidget extends StatelessWidget {

  TextEditingController searchInputController = TextEditingController();

  HandleSearch handleSearch;

  SearchInputWidget({this.handleSearch});

  Widget buildTextField() {
    // theme设置局部主题
    return Theme(
        data: new ThemeData(primaryColor: Colors.grey),
        child: Container(
          padding: EdgeInsets.only(top: 5.0, left: 5.0),
          decoration: BoxDecoration(
              color: Colors.white,
              border: Border(
                bottom: BorderSide(width: 1.0, color: Colors.black12),
                top: BorderSide(width: 1.0, color: Colors.black12),
                right: BorderSide(width: 1.0, color: Colors.black12),
                left: BorderSide(width: 1.0, color: Colors.black12),
              )),
          child: TextField(
            controller: searchInputController,
            cursorColor: Colors.grey, // 光标颜色
            // 默认设置
            decoration: InputDecoration(
              contentPadding: const EdgeInsets.symmetric(vertical: 10.0),
              border: InputBorder.none,
              suffixIcon: InkWell(
                onTap: () {
                  handleSearch(searchInputController.text);
                },
                child: Icon(Icons.search),
              ),
              hintText: "搜索文件",
              hintStyle: new TextStyle(
                  fontSize: 14, color: Color.fromARGB(50, 0, 0, 0))),
            style: new TextStyle(fontSize: 14, color: Colors.black),
          ),
        ));
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      // 修饰搜索框, 白色背景与圆角
      decoration: new BoxDecoration(
        color: Colors.white,
        borderRadius: new BorderRadius.all(new Radius.circular(5.0)),
      ),
      alignment: Alignment.center,
      height: ScreenUtil().setHeight(120),
      padding: EdgeInsets.fromLTRB(0.0, 0.0, 0.0, 0.0),
      child: buildTextField(),
    );
  }
}
