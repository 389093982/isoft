import 'dart:io';

import 'package:path_provider/path_provider.dart';

///加载缓存
Future<String> getCacheSize() async {
  Directory tempDir = await getTemporaryDirectory();
  double value = await _getTotalSizeOfFilesInDir(tempDir);
  return _renderSize(value);
}

/// 递归方式 计算文件的大小
Future<double> _getTotalSizeOfFilesInDir(final FileSystemEntity file) async {
  try {
    if (file is File) {
      int length = await file.length();
      return double.parse(length.toString());
    }
    if (file is Directory) {
      final List<FileSystemEntity> children = file.listSync();
      double total = 0;
      if (children != null)
        for (final FileSystemEntity child in children)
          total += await _getTotalSizeOfFilesInDir(child);
      return total;
    }
    return 0;
  } catch (e) {
    print(e);
    return 0;
  }
}

Future<String> clearCache() async {
  Directory tempDir = await getTemporaryDirectory();
  //删除缓存目录
  await delDir(tempDir);
  return await getCacheSize();
}

///递归方式删除目录
Future<Null> delDir(FileSystemEntity file) async {
  try {
    if (file is Directory) {
      final List<FileSystemEntity> children = file.listSync();
      for (final FileSystemEntity child in children) {
        await delDir(child);
      }
    }
    await file.delete();
  } catch (e) {
    print(e);
  }
}

///格式化文件大小
_renderSize(double value) {
  if (null == value) {
    return 0;
  }
  List<String> unitArr = List()..add('B')..add('K')..add('M')..add('G');
  int index = 0;
  while (value > 1024) {
    index++;
    value = value / 1024;
  }
  String size = value.toStringAsFixed(2);
  return size + unitArr[index];
}
