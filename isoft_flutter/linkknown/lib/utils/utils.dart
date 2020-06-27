
import 'package:fluttertoast/fluttertoast.dart';

class UIUtils {
  static void showToast(String msg) {
    Fluttertoast.showToast(msg: msg, gravity: ToastGravity.CENTER);
  }
}
