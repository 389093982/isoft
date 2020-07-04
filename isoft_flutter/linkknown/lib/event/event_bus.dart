
//Bus 初始化
import 'package:event_bus/event_bus.dart';
import 'package:linkknown/model/user.dart';

EventBus eventBus = EventBus();

// 广播数据(自定义的广播名称)
class LoginSuccessEvent {
  LoginUserResponse response;

  LoginSuccessEvent(this.response);
}

class ClassifyEvent {
  String text;

  ClassifyEvent(this.text);
}