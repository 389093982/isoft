import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/my_coupon_response.dart';
import 'package:linkknown/response/user_link_agent_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/header_icon.dart';

import 'accept_invite_button_label.dart';
import 'button_label.dart';
import 'clickable_textimage.dart';

class SomeBodyInviteMeItemWidget extends StatefulWidget {
  UserLinkAgent userLinkAgent;

  SomeBodyInviteMeItemWidget(this.userLinkAgent);

  @override
  _SomeBodyInviteMeItemState createState() => _SomeBodyInviteMeItemState();
}

class _SomeBodyInviteMeItemState extends State<SomeBodyInviteMeItemWidget>
    with TickerProviderStateMixin {
  _SomeBodyInviteMeItemState();

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
                  NavigatorUtil.goRouterPage(context, "${Routes.courseDetail}?course_id=11");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child:Container(
                  margin: EdgeInsets.only(left: 10),
                  child: ClipOval(
                    child: HeaderIconWidget(widget.userLinkAgent.smallIcon, HeaderIconSize.SIZE_NORMAL_50),
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
                      Text(widget.userLinkAgent.nickName,style: TextStyle(fontSize: 15),),
                      Image.asset(
                        "male"==widget.userLinkAgent.gender?"images/ic_male.png":"images/ic_female.png",
                        height: 20,
                        fit: BoxFit.fill,
                      ),
                    ],
                  ),
                  SizedBox(height: 5,),
                  Row(
                    children: <Widget>[
                      Text(widget.userLinkAgent.agentUserName,style: TextStyle(fontSize: 12,color: Colors.black54),),
                    ],
                  ),
                  SizedBox(height: 5,),
                  Row(children: <Widget>[
                    Text("邀请时间:"+DateUtil.format2StandardTime(widget.userLinkAgent.bindTime),style: TextStyle(fontSize: 12,color: Colors.black54),),
                  ],),
                ],
              ),
            ),
          ),
          Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Container(
                padding: EdgeInsets.all(10),
                child: InkWell(
                  onTap: () {
                    AgreeUserLinkAgent(widget.userLinkAgent.agentUserName);
                  },
                  child: AcceptInviteButtonLabel("接受邀请"),
                ),
              )
            ],
          )
        ],
      ),
    );
  }


  //接受邀请
  AgreeUserLinkAgent(String agent_user_name){
    LinkKnownApi.AgreeUserLinkAgent(agent_user_name).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("关联成功！");
      }else{
        UIUtils.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }


}
