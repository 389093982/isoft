
class DateUtil {

  //获取今日 ： yyyyMMdd
  static String getToday_YYYYMMDD(){
    String today = DateTime.fromMillisecondsSinceEpoch(DateTime.now().millisecondsSinceEpoch).toString();
    return today.substring(0,10).replaceAll("-", "");
  }

  //获取今日 ： yyyy-MM-dd
  static String getToday_YYYY_MM_DD(){
    String today = DateTime.fromMillisecondsSinceEpoch(DateTime.now().millisecondsSinceEpoch).toString();
    return today.substring(0,10).replaceAll("-", "");
  }

  //获取此刻时间 ： HH:mm:ss   带有冒号
  static String getNowTime_HHmmss(){
    String today = DateTime.fromMillisecondsSinceEpoch(DateTime.now().millisecondsSinceEpoch).toString();
    return today.substring(11,19);
  }

  //获取此刻时间戳 ： yyyyMMddHHmmss
  static String getNow_yyyyMMddHHmmss(){
    String today = DateTime.fromMillisecondsSinceEpoch(DateTime.now().millisecondsSinceEpoch).toString();
    return today.substring(0,19).replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
  }

  //国际时间转为正常时间串: yyyy-MM-dd HH:mm:ss
  //先将带有 UTC的国际时间转为带有000Z的时间
  static String format2StandardTime (String dateTime) {
    String standardTime = DateTime.fromMillisecondsSinceEpoch(DateTime.parse(dateTime).millisecondsSinceEpoch).toString();
    return standardTime.substring(0,19);
  }


}