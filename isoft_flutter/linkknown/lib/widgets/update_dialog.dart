
import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flustars/flustars.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:install_apk_plugin/install_apk_plugin.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:package_info/package_info.dart';

class UpdateDialog extends StatefulWidget {

  String androidDownloadUrl;
  bool forceUpdate;

  UpdateDialog(this.androidDownloadUrl, this.forceUpdate);

  @override
  _UpdateDialogState createState() => _UpdateDialogState();
}

class _UpdateDialogState extends State<UpdateDialog> {
  
  final CancelToken _cancelToken = CancelToken();
  bool _isDownload = false;
  double _value = 0;
  
  @override
  void dispose() {
    if (!_cancelToken.isCancelled && _value != 1) {
      _cancelToken.cancel();
    }
    super.dispose();
  }
  
  @override
  Widget build(BuildContext context) {
    final Color primaryColor = Theme.of(context).primaryColor;
    return WillPopScope(
      onWillPop: () async {
        /// 使用false禁止返回键返回，达到强制升级目的
        return widget.forceUpdate ? false : true;
      },
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: Colors.transparent,
        body: Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Container(
                height: 120.0,
                width: 280.0,
                decoration: BoxDecoration(
                  borderRadius: const BorderRadius.only(topLeft: Radius.circular(8.0), topRight: Radius.circular(8.0)),
                  image: DecorationImage(
                    image: AssetImage("images/update_head.jpg"),
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              Container(
                width: 280.0,
                decoration: BoxDecoration(
                  color: Theme.of(context).canvasColor,
                  borderRadius: const BorderRadius.only(bottomLeft: Radius.circular(8.0), bottomRight: Radius.circular(8.0))
                ),
                padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 15.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text('新版本更新', style: TextStyle(
                      fontSize: 16,
                    )),
                    SizedBox(height: 10,),
                    Text('1.又双叒修复了一大堆bug。\n\n2.祭天了多名程序猿。'),
                    SizedBox(height: 15,),
                    // 下载中显示进度条,否则显示提示按钮
                    _isDownload ? LinearProgressIndicator(
                      backgroundColor: Color(0xFFEEEEEE),
                      valueColor: AlwaysStoppedAnimation<Color>(primaryColor),
                      value: _value,
                    ) : _buildButton(context),
                  ],
                ),
              ),
            ],
          ),
        )
      ),
    );
  }

  Widget _buildButton(BuildContext context) {
    final Color primaryColor = Theme.of(context).primaryColor;
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Container(
          width: 110.0,
          height: 36.0,
          child: FlatButton(
            onPressed: () {
              NavigatorUtil.goBack(context);
            },
            textColor: primaryColor,
            color: Colors.transparent,
            disabledTextColor: Colors.white,
            disabledColor: Color(0xFFcccccc),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(18.0),
              side: BorderSide(
                color: primaryColor,
                width: 0.8,
              ),
            ),
            child: const Text(
              '残忍拒绝',
              style: TextStyle(fontSize: 16.0),
            ),
          ),
        ),
        Container(
          width: 110.0,
          height: 36.0,
          child: FlatButton(
            onPressed: () {
              if (defaultTargetPlatform == TargetPlatform.iOS) {
                NavigatorUtil.goBack(context);
                onClickGotoAppStore("");
              } else {
                setState(() {
                  _isDownload = true;
                });
                _download();
              }
            },
            textColor: Colors.white,
            color: primaryColor,
            disabledTextColor: Colors.white,
            disabledColor: Color(0xFFcccccc),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(18.0),
            ),
            child: const Text(
              '立即更新',
              style: TextStyle(fontSize: 16.0),
            ),
          ),
        )
      ],
    );
  }

  ///下载apk
  Future<void> _download() async {
    try {
      setInitDir(initStorageDir: true);
      await DirectoryUtil.getInstance();
      DirectoryUtil.createStorageDirSync(category: 'Download');
      String path = DirectoryUtil.getStoragePath(fileName: 'deer', category: 'Download', format: 'apk');
      File file = File(path);
      /// 链接可能会失效
      await Dio().download(widget.androidDownloadUrl,
        file.path,
        cancelToken: _cancelToken,
        onReceiveProgress: (int count, int total) {
          if (total != -1) {
            _value = count / total;
            setState(() {

            });
            if (count == total) {
              NavigatorUtil.goBack(context);
              onClickInstallApk(path);
            }
          }
        },
      );
    } catch (e) {
      UIUtil.showToast('下载失败!');
      print(e);
      setState(() {
        _isDownload = false;
      });
    }
  }
}


void onClickInstallApk(String _apkFilePath) async {
  if (_apkFilePath.isEmpty) {
    print('make sure the apk file is set');
    return;
  }
//  Map<PermissionGroup, PermissionStatus> permissions =
//  await PermissionHandler().requestPermissions([PermissionGroup.storage]);
//  if (permissions[PermissionGroup.storage] == PermissionStatus.granted) {
    // TODO 此处 appId 需要根据实际情况修改，可使用以下方式获取
    PackageInfo packageInfo = await CommonUtil.getPackageInfo();
    InstallPlugin.installApk(_apkFilePath, packageInfo.packageName)
        .then((result) {
      print('install apk $result');
    }).catchError((error) {
      print('install apk error: $error');
    });
//  } else {
//    print('Permission request fail!');
//  }
}

void onClickGotoAppStore(String url) {
  url = url.isEmpty
      ? 'https://itunes.apple.com/cn/app/%E5%86%8D%E6%83%A0%E5%90%88%E4%BC%99%E4%BA%BA/id1375433239?l=zh&ls=1&mt=8'
      : url;
  InstallPlugin.gotoAppStore(url);
}