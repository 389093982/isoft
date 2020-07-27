
//Bus 初始化
import 'package:event_bus/event_bus.dart';

EventBus eventBus = EventBus();


class LoginStateChangeEvent {

}


class ClassifyEvent {
  int levelOneId;

  ClassifyEvent(this.levelOneId);
}

