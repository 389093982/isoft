import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/model/favorite_count_response.dart';
import 'package:linkknown/model/favorite_is_response.dart';
import 'package:linkknown/model/get_user_detail_response.dart';
import 'package:linkknown/page/course_video_widget.dart';
import 'package:linkknown/route/reoutes_handler.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/attention_off_button_label.dart';
import 'package:linkknown/widgets/attention_on_button_label.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/divider_line.dart';
import 'package:linkknown/widgets/function_button_label.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

// 课程简介组件
class CourseIntroduceWidget extends StatefulWidget {
  Course course;
  List<CVideo> cVideos;

  CourseIntroduceWidget(this.course, this.cVideos);

  @override
  _CourseIntroduceState createState() => _CourseIntroduceState();
}

// State 构造函数传参的话，只会执行一次，所以不使用 State 传参
// 改用 widget.xxx 参数，widget 的构造器会重复执行
class _CourseIntroduceState extends State<CourseIntroduceWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(left: 10,top: 10,right: 10),
      child: ListView(
        physics: new NeverScrollableScrollPhysics(), // 解决嵌套滑动问题：禁用滑动事件
        children: <Widget>[
          Text(
            widget.course != null ? widget.course.courseName : '',
            style: LinkKnownTextStyle.commonTitle,
          ),
          VEmptyView(5),
          Row(
            children: <Widget>[
              // 课程集数和播放次数
              widget.course!=null? Image.asset(
                "images/ic_views.png",
                width: 15,
                height: 15,
              ):Text("加载中.."),
              // 设置间距
              Container(
                width: 5,
              ),
              Text(widget.course != null
                  ? widget.course.courseNumber.toString()
                  : ""),
              Container(
                width: 15,
              ),
              widget.course!=null? Image.asset(
                "images/ic_list_counts.png",
                width: 15,
                height: 15,
              ):Text(""),
              Container(
                width: 5,
              ),
              Text(widget.course != null
                  ? widget.course.watchNumber.toString()
                  : ""),
              SizedBox(width: 80,),
              Text(widget.course!=null?Constants.RMB+widget.course.price:"",style: TextStyle(color: Colors.red,fontSize: 16),),
              SizedBox(width: 5,),
              Offstage(
                offstage: widget.course==null?false:widget.course.isShowOldPrice=="N",
                child:
                  Text(widget.course!=null?Constants.RMB+widget.course.oldPrice:"",
                    style: TextStyle(color: Colors.black45,fontSize: 14,decoration: TextDecoration.lineThrough),
                  ),
              ),


            ],
          ),
          SizedBox(height: 8,),
          Text(
            widget.course != null ? widget.course.courseShortDesc : '',
            // 设置行间距 1.3
            strutStyle: StrutStyle(
                forceStrutHeight: true, height: 0.8, leading: 0.9),
          ),
          VEmptyView(20),
          Row(
            children: <Widget>[
              SizedBox(width: 70,),
              GestureDetector(
                onTap: (){
                  NavigatorUtil.goRouterPage(context, Routes.shoppingCart);
                },
                child: Image.asset(
                  "images/shoppingCart_green.png",
                  width: 45,
                ),
              ),
              SizedBox(width: 20,),
              GestureDetector(
                onTap: (){
                  addToShoppingCart("course_theme_type",widget.course.id.toString(),widget.course.price);
                },
                child: FunctionButtonLabel(labelText: "加入购物车",borderRadius: 20,),
              ),
              SizedBox(width: 20,),
              GestureDetector(
                onTap: (){
                  UIUtils.showToast("立即购买");
                },
                child: FunctionButtonLabel(labelText: "立即购买",borderRadius: 20,),
              ),
            ],
          ),
          VEmptyView(20),
          //分享点赞收藏播放
          Offstage(
            offstage: widget.course == null,
            child: widget.course != null
                ? CourseOperateWidget(widget.course, widget.cVideos)
                : null,
          ),
          //黑色分割线
          widget.course != null? DividerLineView(
            margin: EdgeInsets.symmetric(vertical: 20),
          ):Text(""),
          //作者信息
          widget.course != null ? CourseAuthorWidget(widget.course) : VEmptyView(1),
          //黑色分割线
          widget.course != null? DividerLineView(
            margin: EdgeInsets.symmetric(vertical: 20),
          ):Text(""),
          // 课程标签语
          CourseLabelWidget(widget.course != null ? widget.course.courseLabel : ''),
          // 分集视频
          Container(child: CourseVideosWidget(widget.course, widget.cVideos, clickCallBack: goToVideoPlay),
          ),
        ],
      ),
    );
  }

  goToVideoPlay(index) {
    routerParamMap["videoplay_courseKey"] = widget.course;
    routerParamMap["videoplay_cVideosKey"] = widget.cVideos;
    NavigatorUtil.goRouterPage(context, "${Routes.videoPlay}?index=${index}");
  }

  //添加购物车
  addToShoppingCart(String goods_type,String goods_id,String goods_price_on_add){
    LinkKnownApi.addToShoppingCart(goods_type, goods_id,goods_price_on_add).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("添加成功");
      }else{
        UIUtils.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }


}

class CourseAuthorWidget extends StatefulWidget {
  Course course;
  CourseAuthorWidget(this.course);

  @override
  _CourseAuthorState createState() => _CourseAuthorState();
}

class _CourseAuthorState extends State<CourseAuthorWidget> {

  // 作者信息
  User user;
  //是否展示关注按钮
  bool showAttentionButton = false;
  //是否已关注
  bool isAttention = false;

  @override
  void initState() {
    super.initState();
    initAuthorData();
    QueryIsAttention();
  }

  initAuthorData() async {
    GetUserDetailResponse userDetailResponse =
        await LinkKnownApi.getUserDetail(widget.course.courseAuthor);
    if (userDetailResponse.status == "SUCCESS") {
      setState(() {
        this.user = userDetailResponse.user;
      });
    }
  }

  //是否显示关注按钮
  QueryIsAttention() async {
    String loginUserName = await LoginUtil.getLoginUserName();
    if(StringUtil.checkNotEmpty(loginUserName)){
      //登录人和作者不是同一个人，才能看到按钮
      if(loginUserName!=widget.course.courseAuthor){
        LinkKnownApi.QueryIsAttention("user",widget.course.courseAuthor).then((value) {
          if(value.status=="SUCCESS"){
            //展示按钮
            showAttentionButton = true;
            value.attentionRecords>0 ? isAttention=true : isAttention=false;
          }
          setState(() {});
        }).catchError((e) {
          UIUtils.showToast((e as LinkKnownError).errorMsg);
        });
      }else{
        //自己，则不展示按钮
        showAttentionButton = false;
        setState(() {});
      }
    }else{
      //未登录，可以看到按钮
      showAttentionButton = true;
      //可以看到按钮,但是必须是未关注的
      isAttention = false;
      setState(() {});
    }

  }

  @override
  Widget build(BuildContext context) {
    return this.user != null
        ? Row(
            children: <Widget>[
              ClipOval(
                child: Image.network(
                  UIUtils.replaceMediaUrl(this.user.smallIcon),
                  width: 50,
                  height: 50,
                  fit: BoxFit.cover,
                ),
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Row(
                    children: <Widget>[
                      Text(" "+this.user.nickName,style: TextStyle(fontSize: 16,color: Colors.black45),),
                      Image.asset(
                          this.user.gender=="male"?"images/ic_male.png":"images/ic_female.png",width: 20,
                      )
                    ],
                  ),
                  SizedBox(height: 2,),
                  Row(
                    children: <Widget>[
                      Text(" 关注:${this.user.attentionCounts}",style: TextStyle(fontSize: 12,color: Colors.grey),),
                      Text("   粉丝:${this.user.fensiCounts}",style: TextStyle(fontSize: 12,color: Colors.grey),),
                    ],
                  ),
                  Row(children: <Widget>[
                    Container(
                      width: 200,
                      child: Text(this.user.userSignature,style: TextStyle(color:Colors.grey,fontSize: 12,),maxLines: 1,overflow: TextOverflow.ellipsis,),
                    )
                  ],),
                ],
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: <Widget>[
                  Row(children: <Widget>[
                    showAttentionButton ?
                    (isAttention?
                        GestureDetector(
                          onTap: (){doAttention("user", widget.course.courseAuthor, "off");},
                          child: AttentionOnButtonLabel("已关注"),
                        )
                        :
                        GestureDetector(
                          onTap: (){doAttention("user", widget.course.courseAuthor, "on");},
                          child: AttentionOffButtonLabel("+ 关注"),
                        )
                    )
                    :
                    Text(""),
                  ],),
                ],
              )
            ],
          )
        : VEmptyView(10);
  }


  //关注和取消
  doAttention(String attention_object_type,String attention_object_id,String state) async {
    bool isLogin = await LoginUtil.checkHasLogin();
    if(!isLogin){
      UIUtils.showToast("未登录..");
      return;
    }
    LinkKnownApi.DoAttention(attention_object_type,attention_object_id,state).then((value) {
      if(value.status=="SUCCESS"){
        if(state=="on"){
          UIUtils.showToast("关注成功");
          isAttention = true;
        }else if(state=="off"){
          UIUtils.showToast("取消成功");
          isAttention = false;
        }
        showAttentionButton = true;
      }else{
        UIUtils.showToast(value.errorMsg);
      }
      setState(() {});
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }

}

class CourseLabelWidget extends StatefulWidget {
  String label;

  CourseLabelWidget(this.label);

  @override
  _CourseLabelState createState() => _CourseLabelState();
}

class _CourseLabelState extends State<CourseLabelWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    List<String> labels = StringUtil.splitLabel(widget.label);
    return Container(
      padding: EdgeInsets.only(bottom: 20),
      child: Wrap(
        spacing: 5, //主轴上子控件的间距
        runSpacing: 5, //交叉轴上子控件之间的间距
        children: List.generate(labels.length, (index) {
          return CommonLabel.getCommonLabel2(labels[index]);
        }),
      ),
    );
  }
}

class CourseOperateWidget extends StatefulWidget {
  Course course;
  List<CVideo> cVideos;

  CourseOperateWidget(this.course, this.cVideos);

  @override
  _CourseOperateState createState() => _CourseOperateState();
}

class _CourseOperateState extends State<CourseOperateWidget> {

  int collectNumber = 0;
  int priaseNumber = 0;
  int watchNumber = 0;
  bool isCollect = false;
  bool isPriase = false;

  @override
  void initState() {
    super.initState();

    initCourseOperateData();
  }

  initCourseOperateData() async {
    FavoriteCountResponse collectFavoriteCountResponse =
        await LinkKnownApi.queryFavoriteCount(
            widget.course.id, Constants.FAVORITE_TYPE_COURSE_COLLECT);
    FavoriteCountResponse priaseFavoriteCountResponse =
        await LinkKnownApi.queryFavoriteCount(
            widget.course.id, Constants.FAVORITE_TYPE_COURSE_PRIASE);

    String userName = await LoginUtil.getUserName();

    IsFavoriteResponse collectIsFavoriteResponse =
        await LinkKnownApi.isFavorite(
            userName, widget.course.id, Constants.FAVORITE_TYPE_COURSE_COLLECT);
    IsFavoriteResponse priaseIsFavoriteResponse = await LinkKnownApi.isFavorite(
        userName, widget.course.id, Constants.FAVORITE_TYPE_COURSE_PRIASE);

    setState(() {
      if (collectFavoriteCountResponse.status == "SUCCESS") {
        this.collectNumber = collectFavoriteCountResponse.counts;
      }
      if (priaseFavoriteCountResponse.status == "SUCCESS") {
        this.priaseNumber = priaseFavoriteCountResponse.counts;
      }

      if (collectIsFavoriteResponse.status == "SUCCESS") {
        this.isCollect = collectIsFavoriteResponse.isFavorite;
      }
      if (priaseIsFavoriteResponse.status == "SUCCESS") {
        this.isPriase = priaseIsFavoriteResponse.isFavorite;
      }
    });
  }

  void handleFavoriteClick(String favorite_type) async {
    String userName = await LoginUtil.getUserName();
    BaseResponse baseResponse =
        await LinkKnownApi.toggleFavorite(widget.course.id, favorite_type);
    if (baseResponse.status == "SUCCESS") {
      // 重新刷新状态
      initCourseOperateData();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Expanded(
          child: CourseOperateItemWidget(
              label: "分享",
              imagepath: "images/ic_share.svg",
              enable: false,
              number: 0,
              clickCallback: () {}),
        ),
        Expanded(
          child: CourseOperateItemWidget(
              label: "点赞",
              imagepath: "images/ic_praise.svg",
              enable: isPriase,
              number: priaseNumber,
              clickCallback: () {
                // 处理点击事件
                handleFavoriteClick(Constants.FAVORITE_TYPE_COURSE_PRIASE);
              }),
        ),
        Expanded(
          child: CourseOperateItemWidget(
              label: "收藏",
              imagepath: "images/ic_collect.svg",
              enable: isCollect,
              number: collectNumber,
              clickCallback: () {
                // 处理点击事件
                handleFavoriteClick(Constants.FAVORITE_TYPE_COURSE_COLLECT);
              }),
        ),
        Expanded(
          child: CourseOperateItemWidget(
              label: "播放",
              imagepath: "images/ic_play.svg",
              enable: false,
              number: widget.course.watchNumber,
              clickCallback: () {
                if (widget.course.courseNumber == 0) {
                  UIUtils.showToast2("暂无剧情，敬请期待！");
                } else {
                  // 默认前去播放第 0 集
                  goToVideoPlay(0);
                }
              }),
        ),
      ],
    );
  }

  goToVideoPlay(index) {
    routerParamMap["videoplay_courseKey"] = widget.course;
    routerParamMap["videoplay_cVideosKey"] = widget.cVideos;
    NavigatorUtil.goRouterPage(context, "${Routes.videoPlay}?index=${index}");
  }
}

class CourseOperateItemWidget extends StatefulWidget {
  String label;
  String imagepath;
  bool enable;
  int number; // 显示收藏点赞观看数量
  VoidCallback clickCallback;

  CourseOperateItemWidget(
      {this.label,
      this.imagepath,
      this.enable,
      this.number,
      this.clickCallback});

  @override
  _CourseOperateItemState createState() => _CourseOperateItemState();
}

class _CourseOperateItemState extends State<CourseOperateItemWidget> {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        children: <Widget>[
          Text(
            "${widget.number}",
            style: TextStyle(color: Colors.green),
          ),
          VEmptyView(3),
          GestureDetector(
            onTap: widget.clickCallback,
            child: Stack(
              alignment: Alignment.center,
              children: <Widget>[
                Image.asset(
                  "images/ic_operate_bg.png",
                  width: 50,
                  height: 50,
                ),
                Align(
                    alignment: Alignment.center,
                    child: SvgPicture.asset(
                      widget.imagepath,
                      width: 30,
                      height: 30,
                      color: widget.enable ? Colors.red[300] : Colors.grey,
                    )),
              ],
            ),
          ),
          VEmptyView(3),
          Text(widget.label),
        ],
      ),
    );
  }
}
