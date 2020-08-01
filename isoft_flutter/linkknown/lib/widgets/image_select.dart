
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:linkknown/utils/utils.dart';

class ImageSelectWidget extends StatefulWidget{

  ValueChanged onSelect;

  // 相册
  static const String SOURCE_GALLERY = "SOURCE_GALLERY";
  // 拍照
  static const String SOURCE_CAMERA = "SOURCE_CAMERA";

  String label;
  String source;

  ImageSelectWidget({this.label = "点击添加商品图片", this.source = SOURCE_GALLERY, this.onSelect});

  @override
  State<StatefulWidget> createState() => ImageSelectWidgetState();
}

class ImageSelectWidgetState extends State<ImageSelectWidget> {

  File imageFile;
  final ImagePicker picker = ImagePicker();

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

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: <Widget>[
          InkWell(
            borderRadius: BorderRadius.circular(16.0),
            onTap: _getImage,
            child: Container(
              width: 120,
              height: 120,
              decoration: BoxDecoration(
                // 图片圆角展示
                borderRadius: BorderRadius.circular(16.0),
                image: DecorationImage(
                  image: imageFile == null ? AssetImage("images/icon_add.png") : FileImage(imageFile),
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ),
          SizedBox(height: 5,),
          Text(widget.label, style: TextStyle(color: Colors.grey[500], fontSize: 12),),
        ],
      ),
    );
  }

}