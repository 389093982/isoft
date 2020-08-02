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
            decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: new BorderRadius.all(new Radius.circular(40.0)),
            ),
            child: ConstrainedBox(
                      constraints: BoxConstraints(
                        maxHeight: 40,
                        //maxWidth: 300
                      ),
                      child: TextField(
                        controller: searchInputController,
                        //最大行数
                        maxLines: 1,
                        //输入文本的样式
                        style: TextStyle(fontSize: 15, color: Colors.black),
                        //光标颜色
                        cursorColor: Colors.red,
                        //光标宽度
                        cursorWidth: 2.0,
                        // 默认设置
                        decoration: InputDecoration(
                          contentPadding: EdgeInsets.only(left: 12,top:0),//光标距离左侧距离
                          hintText: "输入搜索内容",
                          hintStyle: new TextStyle(fontSize: 14, color: Colors.grey[400]),
                          enabledBorder: OutlineInputBorder(//未点击输入框的效果
                            borderSide: BorderSide(
                              color: Colors.white, //边框颜色
                              width: 2, //宽度为2
                            ),
                            borderRadius: BorderRadius.circular(30),//四个角弧度
                          ),
                          focusedBorder: OutlineInputBorder(//点击输入框后的效果
                            borderSide: BorderSide(
                              color: Colors.white, //边框颜色
                              width: 2, //宽度为2
                            ),
                            borderRadius: BorderRadius.circular(30),//四个角弧度
                          ),
                          suffixIcon: InkWell(
                            onTap: () {
                              handleSearch(searchInputController.text);
                            },
                            child: Icon(
                              Icons.search,
                              color: Colors.red,
                            ),
                          ),
                        ),
                      )
                  ),
          )),
    );
  }
}
