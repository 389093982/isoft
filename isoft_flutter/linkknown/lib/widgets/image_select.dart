
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/header_icon.dart';

class ImageSelectWidget extends StatefulWidget{
  ValueChanged onSelect;
  String label;
  String source;
  String currentSmallIcon;
  ImageSelectWidget({this.label = "点击设置头像", this.currentSmallIcon,this.onSelect});

  static const String SOURCE_GALLERY = "SOURCE_GALLERY";  // 相册
  static const String SOURCE_CAMERA = "SOURCE_CAMERA";  // 拍照

  @override
  State<StatefulWidget> createState() => ImageSelectWidgetState();
}

class ImageSelectWidgetState extends State<ImageSelectWidget> {

  File imageFile;
  final ImagePicker picker = ImagePicker();

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: <Widget>[
          InkWell(
            borderRadius: BorderRadius.circular(40),
            onTap: (){
              showStyleDialogList();
            },
            child: Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                // 图片圆角展示
                borderRadius: BorderRadius.circular(40),
                image: DecorationImage(
                  image: imageFile != null ? FileImage(imageFile):AssetImage("images/linkknown.jpg"),
                  fit: BoxFit.cover,
                ),
              ),
              child: imageFile != null ?
                      Text("")
                      :
                      ClipOval(child: HeaderIconWidget(widget.currentSmallIcon, HeaderIconSize.SIZE_BIG_80),),
            ),
          ),
          SizedBox(height: 5,),
        ],
      ),
    );
  }


  //选择图片来源选择框
  showStyleDialogList() {
    showDialog<Null>(
      context: context,
      builder: (BuildContext context) {
        return new SimpleDialog(
          children: <Widget>[
            Column(
              children: <Widget>[
                new SimpleDialogOption(
                  child: Text('从相册中选择',style: TextStyle(fontSize: 18),),
                  onPressed: () {
                    widget.source = ImageSelectWidget.SOURCE_GALLERY;
                    _getImage();
                    Navigator.of(context).pop();
                  },
                ),
                new SimpleDialogOption(
                  child: Divider(),
                ),
                new SimpleDialogOption(
                  child: Text('拍摄照片',style: TextStyle(fontSize: 18),),
                  onPressed: () {
                    widget.source = ImageSelectWidget.SOURCE_CAMERA;
                    _getImage();
                    Navigator.of(context).pop();
                  },
                ),
              ],
            )
          ],
        );
      },
    );
  }


  //获取照片：从相册选择、拍照
  Future<void> _getImage() async {
    try {
      PickedFile pickedFile = await picker.getImage(source: widget.source == ImageSelectWidget.SOURCE_GALLERY ? ImageSource.gallery : ImageSource.camera, maxWidth: 800);
      if (pickedFile != null) {
        setState(() {
          imageFile = File(pickedFile.path);
          widget.onSelect(imageFile.path);
        });
      }
    } catch (e) {
      UIUtil.showToast(widget.source == ImageSelectWidget.SOURCE_GALLERY ? '没有权限，无法打开相册！' : '没有权限，无法打开照相机！');
    }
  }

}