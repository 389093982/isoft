import 'dart:async';

import 'package:flutter/material.dart';
import 'package:infinite_cards/infinite_cards.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/click_item.dart';
import 'package:linkknown/widgets/copy_right.dart';
import 'package:package_info/package_info.dart';
import 'package:photo_view/photo_view.dart';
import 'package:photo_view/photo_view_gallery.dart';

class AboutPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _AboutPageState();
}

class _AboutPageState extends State<AboutPage> {
  String version = "";

  List<String> assetNames = [
    'images/about_linkknown01.jpg',
    'images/about_linkknown02.jpg',
    'images/about_linkknown03.jpg',
  ];

  InfiniteCardsController infiniteCardsController;
  Timer timer;

  @override
  void initState() {
    super.initState();

    infiniteCardsController = InfiniteCardsController(
      itemBuilder: _renderItem,
      itemCount: assetNames.length,
      animType: AnimType.SWITCH,
    );
    timer = Timer.periodic(Duration(seconds: 3), (Timer t) {
      infiniteCardsController.reset(animType: AnimType.TO_END);
      infiniteCardsController.next();
    });

    initData();
  }

  Widget _renderItem(BuildContext context, int index) {
    return GestureDetector(
      onTap: (){
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context) {
          return PhotoPreview(
            initialIndex: index,
            photoList: assetNames,
          );
        }));
      },
      child: Image(
        image: AssetImage(assetNames[index]),
      ),
    );
  }

  initData() async {
    PackageInfo packageInfo = await CommonUtil.getPackageInfo();
    setState(() {
      version = packageInfo.version;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('关于链知'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      body: Stack(
        children: <Widget>[
          Align(
            alignment: Alignment.topCenter,
            child: Container(
              margin: EdgeInsets.only(top: 50),
              child: Column(
                children: <Widget>[
                  InfiniteCards(
                    controller: infiniteCardsController,
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.height / 3,
                  ),
                  SizedBox(height: 10,),
                  ClickItem(
                    title: "应用名称",
                    content: "链知课堂",
                  ),
                  ClickItem(
                    title: "当前版本",
                    content: "v$version",
                  ),
                  ClickItem(
                    title: "用户协议",
                    onTap: () {
                      NavigatorUtil.goRouterPage(context, Routes.userAgreement);
                    },
                  ),
                  ClickItem(
                    title: "商业合作",
                    onTap: () {
                      NavigatorUtil.goRouterPage(context, Routes.business);
                    },
                  ),
                ],
              ),
            ),
          ),
          Align(
            alignment: Alignment.bottomCenter,
            child: Padding(
              padding: EdgeInsets.only(bottom: 20),
              child: CopyRightWidget(),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }
}

//PhotoPreview 点击小图后的效果
class PhotoPreview extends StatefulWidget {
  final int initialIndex;
  final List<String> photoList;
  final PageController pageController;

  PhotoPreview({this.initialIndex, this.photoList})
      : pageController = PageController(initialPage: initialIndex);

  @override
  _PhotoPreviewState createState() => _PhotoPreviewState();
}

class _PhotoPreviewState extends State<PhotoPreview> {
  int currentIndex;

  @override
  void initState() {
    currentIndex = widget.initialIndex;
    super.initState();
  }

  //图片切换
  void onPageChanged(int index) {
    setState(() {
      currentIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: PhotoViewGallery.builder(
        scrollPhysics: const BouncingScrollPhysics(),
        onPageChanged: onPageChanged,
        itemCount: widget.photoList.length,
        pageController: widget.pageController,
        builder: (BuildContext context, int index) {
          return PhotoViewGalleryPageOptions(
            imageProvider: AssetImage(widget.photoList[index]),
            minScale: PhotoViewComputedScale.contained * 0.6,
            maxScale: PhotoViewComputedScale.covered * 1.1,
            initialScale: PhotoViewComputedScale.contained,
          );
        },
      ),
    );
  }
}