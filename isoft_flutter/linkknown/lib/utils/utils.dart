
import 'package:fluttertoast/fluttertoast.dart';

class UIUtils {
  static void showToast(String msg) {
    Fluttertoast.showToast(msg: msg, gravity: ToastGravity.CENTER);
  }


  static String replaceMediaUrl (String url) {
    return url.replaceFirst("http://localhost:6001", "http://192.168.1.11:6001");
  }

  static bool isValidPrice (String price) {
    return double.parse(price) > 0;
  }
}
