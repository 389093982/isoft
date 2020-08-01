import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/query_general_coupon_targets_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

import 'button_label.dart';

class CouponGoodsItemWidget extends StatefulWidget {
  Course course;

  CouponGoodsItemWidget(this.course);

  @override
  _CouponGoodsItemState createState() => _CouponGoodsItemState();
}

class _CouponGoodsItemState extends State<CouponGoodsItemWidget> with TickerProviderStateMixin {
  _CouponGoodsItemState();

  @override
  Widget build(BuildContext context) {
    return widget.course==null?Text(""):Card(
      color: Colors.white,
      //z轴的高度，设置card的阴影
      elevation: 1.2,
      //设置shape，这里设置成了R角
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(4.0)),
      ),
      // 对Widget截取的行为，比如这里 Clip.antiAlias 指抗锯齿
      clipBehavior: Clip.antiAlias,
      semanticContainer: false,
      child: Row(
        children: <Widget>[
          InkWell(
            onTap: () {
              NavigatorUtil.goRouterPage(
                  context, "${Routes.courseDetail}?course_id=${widget.course.id}");
            },
            // AspectRatio的作用是调整 child 到设置的宽高比
            child:Container(
              padding: EdgeInsets.all(10),
              child: Image.network(
                UIUtils.replaceMediaUrl(widget.course.smallImage),
                width: 130,
                height: 100,
                fit: BoxFit.cover,
              ),
            ),
          ),
          Expanded(
            child: Container(
              padding: EdgeInsets.only(right: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(widget.course.courseName,style: TextStyle(fontSize: 15),),
                  SizedBox(height: 5,),
                  Text(widget.course.courseShortDesc,
                      softWrap: true,
                      textAlign: TextAlign.left,
                      overflow: TextOverflow.ellipsis,
                      maxLines: 2,
                      style: TextStyle(fontSize: 12,color: Colors.black54)
                  ),
                  SizedBox(height: 5,),
                  Container(
                    margin: EdgeInsets.only(top: 2),
                    child: Row(
                      children: <Widget>[
                        // 课程集数和播放次数
                        Image.asset(
                          "images/ic_views.png",
                          width: 15,
                          height: 15,
                        ),
                        SizedBox(
                          width: 5,
                        ),
                        Text(widget.course.courseNumber.toString()),
                        SizedBox(
                          width: 10,
                        ),
                        Image.asset(
                          "images/ic_list_counts.png",
                          width: 15,
                          height: 15,
                        ),
                        SizedBox(
                          width: 5,
                        ),
                        Text(widget.course.watchNumber.toString()),
                      ],
                    ),
                  ),
                  SizedBox(height: 5,),
                  Row(
                    children: <Widget>[
                      // offstage 组件控制组件是否隐藏
                      // 通过offsatge字段控制child是否显示,比较常用的控件
                      Offstage(
                        offstage: !UIUtils.isValidPrice(widget.course.price),
                        child: Padding(
                          padding: EdgeInsets.only(right: 5),
                          child: Text(
                            Constants.RMB + widget.course.price,
                            style: TextStyle(color: Colors.red),
                          ),
                        ),
                      ),
                      Offstage(
                        offstage: widget.course == null
                            ? false
                            : widget.course.isShowOldPrice == "N",
                        child: Text(
                          Constants.RMB + widget.course.oldPrice,
                          style: TextStyle(
                              color: Colors.grey,
                              decoration: TextDecoration.lineThrough),
                        ),
                      ),
                    ],
                  )

                ],
              ),
            ),
          ),
        ],
      ),
    );
  }


}
