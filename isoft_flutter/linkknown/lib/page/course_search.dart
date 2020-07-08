
import 'package:flutter/material.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_search.dart';

class CourseSearchPage extends StatefulWidget {

  String search;
  String isCharge;

  CourseSearchPage(this.search, this.isCharge);

  @override
  _CourseSearchPageState createState() => _CourseSearchPageState();

}

class _CourseSearchPageState extends State<CourseSearchPage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        preferredSize:
        Size.fromHeight(MediaQuery.of(context).size.height * 0.07),
        child: SafeArea(
          top: true,
          child: Offstage(),
        ),
      ),
      backgroundColor: Colors.white,
      body: Column(
        children: <Widget>[
          SearchInputWidget(
            handleSearch: (data) {
              this.setState(() {
                widget.search = data;
              });
            },
          ),
          CourseFilterWidget(widget.search, widget.isCharge),
        ],
      ),
    );
  }
}