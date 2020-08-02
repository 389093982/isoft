
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:linkknown/utils/utils.dart';

class ImageSelectWidget extends StatefulWidget{
  static const String SOURCE_GALLERY = "SOURCE_GALLERY";  // 相册
  static const String SOURCE_CAMERA = "SOURCE_CAMERA";  // 拍照

  ValueChanged onSelect;
  String label;
  String source;
  ImageSelectWidget({this.label = "点击设置头像", this.source = SOURCE_GALLERY, this.onSelect});

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
              _getImage();
            },
            child: Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                // 图片圆角展示
                borderRadius: BorderRadius.circular(40),
                image: DecorationImage(
                  image: imageFile == null ? AssetImage("images/icon_add.png") : FileImage(imageFile),
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ),
          SizedBox(height: 5,),
        ],
      ),
    );
  }


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
      UIUtils.showToast(widget.source == ImageSelectWidget.SOURCE_GALLERY ? '没有权限，无法打开相册！' : '没有权限，无法打开照相机！');
    }
  }

}