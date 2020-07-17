import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

typedef HandleSearch<T> = void Function(T value);

class SearchInputWidget extends StatelessWidget {
  TextEditingController searchInputController = TextEditingController();

  HandleSearch handleSearch;

  SearchInputWidget({this.handleSearch});

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      padding: EdgeInsets.symmetric(vertical: 13, horizontal: 30),
      // theme设置局部主题
      child: Theme(
          data: new ThemeData(primaryColor: Colors.grey),
          child: Container(
            padding: EdgeInsets.only(top: 5.0, left: 5.0),
            decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: new BorderRadius.all(new Radius.circular(40.0)),
                border: Border(
                  bottom: BorderSide(width: 1.0, color: Colors.black12),
                  top: BorderSide(width: 1.0, color: Colors.black12),
                  right: BorderSide(width: 1.0, color: Colors.black12),
                  left: BorderSide(width: 1.0, color: Colors.black12),
                )),
            child: TextField(
              controller: searchInputController,
              cursorColor: Colors.red, // 光标颜色
              // 默认设置
              decoration: InputDecoration(
                  contentPadding: const EdgeInsets.symmetric(
                      vertical: 10.0, horizontal: 35.0),
                  border: InputBorder.none,
                  suffixIcon: InkWell(
                    onTap: () {
                      handleSearch(searchInputController.text);
                    },
                    child: Icon(
                      Icons.search,
                      color: Colors.red,
                    ),
                  ),
                  hintText: "输入搜索内容",
                  hintStyle:
                      new TextStyle(fontSize: 14, color: Colors.grey[400])),
              style: new TextStyle(fontSize: 14, color: Colors.black),
            ),
          )),
    );
  }
}
