
class DateUtil {

  //获取今日 ： yyyyMMdd
  static String getToday_YYYYMMDD(){
    String today = DateTime.fromMillisecondsSinceEpoch(DateTime.now().millisecondsSinceEpoch).toString();
    return today.substring(0,10).replaceAll("-", "");
  }

  //获取今日 ： yyyy-MM-dd
  static String getToday_YYYY_MM_DD(){
    String today = DateTime.fromMillisecondsSinceEpoch(DateTime.now().millisecondsSinceEpoch).toString();
    return today.substring(0,10);
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
    if(dateTime.length==14){
      return dateTime.substring(0,4)+"-"+dateTime.substring(4,6)+"-"+dateTime.substring(6,8) + " "
          + dateTime.substring(8,10)+":"+dateTime.substring(10,12)+":"+dateTime.substring(12,14);
    }else {
      String standardTime = DateTime.fromMillisecondsSinceEpoch(DateTime.parse(dateTime).millisecondsSinceEpoch).toString();
      return standardTime.substring(0,19);
    }
  }


  //格式化发布时间
  static String formatPublishTime(String timeStamp) {
    String dateTime = format2StandardTime(timeStamp);
    String today = getToday_YYYY_MM_DD();
    //1.判断是不是今年
    if (dateTime.substring(0,4)==(today.substring(0,4))){
      var difference = new DateTime.now().difference(DateTime.parse(dateTime));
      int days = difference.inDays;
      //2.添加修饰
      if (days<=1){
        if (days==0 && today==dateTime.substring(0,10)){
          return "今天"+dateTime.substring(dateTime.length-8,dateTime.length);
        }else if ((days==0 && today!=dateTime.substring(0,10)) || days==1){
          return "昨天"+dateTime.substring(dateTime.length-8,dateTime.length);
        }else{
          return dateTime;
        }
      }else{
        return dateTime.substring(5,dateTime.length);
      }
    }else{
      //非今年
      return dateTime;
    }
  }

}