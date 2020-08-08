import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/my_coupon_response.dart';
import 'package:linkknown/response/query_attention_or_fensi_response.dart';
import 'package:linkknown/response/user_link_agent_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/header_icon.dart';

import 'clickable_textimage.dart';

class AttentionOrFensiItemWidget extends StatefulWidget {
  AttentionOrFensi attentionOrFensi;

  AttentionOrFensiItemWidget(this.attentionOrFensi);

  @override
  _AttentionOrFensiItemState createState() => _AttentionOrFensiItemState();
}

class _AttentionOrFensiItemState extends State<AttentionOrFensiItemWidget>
    with TickerProviderStateMixin {
  _AttentionOrFensiItemState();

  @override
  Widget build(BuildContext context) {
    return Card(
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
          // Stack类似FrameLayout,子 widget可以通过父容器的四个角固定位置,子widget可以重叠
          Stack(
            children: <Widget>[
              InkWell(
                onTap: () {
                  NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${widget.attentionOrFensi.userName}");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child:Container(
                  margin: EdgeInsets.only(left: 10),
                  child: ClipOval(
                    child: HeaderIconWidget(widget.attentionOrFensi.smallIcon, HeaderIconSize.SIZE_NORMAL_50),
                  ),
                ),
              ),
            ],
          ),
          Expanded(
            child: Container(
              padding: EdgeInsets.all(10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  SizedBox(height: 5,),
                  Row(
                    children: <Widget>[
                      Text(widget.attentionOrFensi.nickName,style: TextStyle(fontSize: 15),),
                      Image.asset(
                        "male"==widget.attentionOrFensi.gender?"images/ic_male.png":"images/ic_female.png",
                        height: 20,
                        fit: BoxFit.fill,
                      ),
                    ],
                  ),
                  SizedBox(height: 5,),
                  SizedBox(height: 5,),
                  Text(""==widget.attentionOrFensi.userSignature?"暂无签名":widget.attentionOrFensi.userSignature,
                      softWrap: true,
                      textAlign: TextAlign.left,
                      overflow: TextOverflow.ellipsis,
                      maxLines: 1,
                      style: TextStyle(fontSize: 12,color: Colors.black54)
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }


}
