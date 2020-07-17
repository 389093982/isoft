import 'package:flutter/material.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class UserAgreementPage extends StatefulWidget {
  @override
  _UserAgreementState createState() => _UserAgreementState();
}

class _UserAgreementState extends State<UserAgreementPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('用户协议'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      body: Container(
        padding: EdgeInsets.all(10),
        child: ListView(
          children: <Widget>[
            Image.asset(
              "images/nuli.jpg",
              height: 200,
              fit: BoxFit.fill,
            ),
            Text(
              "链知课堂用户使用协议",
              style: LinkKnownTextStyle.commonTitle,
            ),
            Card(
              color: Colors.white,
              elevation: 2.0, //设置阴影
              child: Padding(
                padding: EdgeInsets.all(5),
                child: Text(
                    "感谢您选择链知网络科技有限公司出品的链知课堂APP！本协议是您在使用链知课堂时与链知网络科技有限公司（以下简称“链知科技”）所订立的协议。"
                    "\n\n 您注册成功后，即成为链知科技注册用户，用户须对在链知科技的注册信息的真实性、合法性、有效性承担全部责任。您可自行创建、修改昵称，但用户名和昵称的命名及使用应遵守相关法律法规并符合网络道德，不得冒充他人或恶意注册使人误认；不得利用他人的名义发布任何信息；不得恶意使用注册帐号导致其他用户误认；用户名和昵称中不能含有任何侮辱、诽谤、淫秽或暴力等侵害他人合法权益或违反公序良俗的词语。如您违反前述规定，链知有权随时限制或拒绝您使用该账号，甚至注销该账号。"
                    "\n\n 在您使用链知科技相关产品，并产生消费行为，表示您已经具备中华人民共和国法律规定的与您行为相适应的民事行为能力。若您不具备前述与您行为相适应的民事行为能力，您需要在您的监护人陪同下使用本产品。"),
              ),
            ),
            VEmptyView(20),
            Text(
              "联系我",
              style: LinkKnownTextStyle.commonTitle,
            ),
            Card(
              color: Colors.white,
              elevation: 2.0, //设置阴影
              child: Padding(
                padding: EdgeInsets.all(5),
                child: Text("Email: linkknown@163.com", style: TextStyle(color: Colors.red, fontWeight: FontWeight.bold),),
              ),
            ),
            VEmptyView(20),
            Text(
              "主办单位",
              style: LinkKnownTextStyle.commonTitle,
            ),
            Card(
              color: Colors.white,
              elevation: 2.0, //设置阴影
              child: Padding(
                padding: EdgeInsets.all(5),
                child: Text("链知网络科技有限公司\n链知课堂\n链知科技文艺部"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
