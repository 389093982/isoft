class PayOrderResponse {
  String status;
  String errorMsg;
  List<Order> orders;
  Paginator paginator;

  PayOrderResponse({this.errorMsg, this.orders, this.paginator, this.status});

  PayOrderResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    if (json['orders'] != null) {
      orders = new List<Order>();
      json['orders'].forEach((v) {
        orders.add(new Order.fromJson(v));
      });
    }
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    if (this.orders != null) {
      data['orders'] = this.orders.map((v) => v.toJson()).toList();
    }
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Order {
  String activityType;
  String activityTypeBindId;
  String codeUrl;
  String goodsDesc;
  String goodsId;
  String goodsImg;
  String goodsOriginalPrice;
  String goodsType;
  int id;
  String orderId;
  String orderTime;
  String paidAmount;
  String payResult;
  String transTime;
  String userName;

  Order(
      {this.activityType,
        this.activityTypeBindId,
        this.codeUrl,
        this.goodsDesc,
        this.goodsId,
        this.goodsImg,
        this.goodsOriginalPrice,
        this.goodsType,
        this.id,
        this.orderId,
        this.orderTime,
        this.paidAmount,
        this.payResult,
        this.transTime,
        this.userName});

  Order.fromJson(Map<String, dynamic> json) {
    activityType = json['activity_type'];
    activityTypeBindId = json['activity_type_bind_id'];
    codeUrl = json['code_url'];
    goodsDesc = json['goods_desc'];
    goodsId = json['goods_id'];
    goodsImg = json['goods_img'];
    goodsOriginalPrice = json['goods_original_price'];
    goodsType = json['goods_type'];
    id = json['id'];
    orderId = json['order_id'];
    orderTime = json['order_time'];
    paidAmount = json['paid_amount'];
    payResult = json['pay_result'];
    transTime = json['trans_time'];
    userName = json['user_name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['activity_type'] = this.activityType;
    data['activity_type_bind_id'] = this.activityTypeBindId;
    data['code_url'] = this.codeUrl;
    data['goods_desc'] = this.goodsDesc;
    data['goods_id'] = this.goodsId;
    data['goods_img'] = this.goodsImg;
    data['goods_original_price'] = this.goodsOriginalPrice;
    data['goods_type'] = this.goodsType;
    data['id'] = this.id;
    data['order_id'] = this.orderId;
    data['order_time'] = this.orderTime;
    data['paid_amount'] = this.paidAmount;
    data['pay_result'] = this.payResult;
    data['trans_time'] = this.transTime;
    data['user_name'] = this.userName;
    return data;
  }
}

class Paginator {
  int currpage;
  int firstpage;
  int lastpage;
  List<int> pages;
  int pagesize;
  int totalcount;
  int totalpages;

  Paginator(
      {this.currpage,
        this.firstpage,
        this.lastpage,
        this.pages,
        this.pagesize,
        this.totalcount,
        this.totalpages});

  Paginator.fromJson(Map<String, dynamic> json) {
    currpage = json['currpage'];
    firstpage = json['firstpage'];
    lastpage = json['lastpage'];
    pages = json['pages'].cast<int>();
    pagesize = json['pagesize'];
    totalcount = json['totalcount'];
    totalpages = json['totalpages'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['currpage'] = this.currpage;
    data['firstpage'] = this.firstpage;
    data['lastpage'] = this.lastpage;
    data['pages'] = this.pages;
    data['pagesize'] = this.pagesize;
    data['totalcount'] = this.totalcount;
    data['totalpages'] = this.totalpages;
    return data;
  }
}