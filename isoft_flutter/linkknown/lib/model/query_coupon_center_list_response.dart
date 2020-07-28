class QueryCouponCenterListResponse {
  List<Coupon> coupons;
  String errorMsg;
  Paginator paginator;
  String status;

  QueryCouponCenterListResponse(
      {this.coupons, this.errorMsg, this.paginator, this.status});

  QueryCouponCenterListResponse.fromJson(Map<String, dynamic> json) {
    if (json['coupons'] != null) {
      coupons = new List<Coupon>();
      json['coupons'].forEach((v) {
        coupons.add(new Coupon.fromJson(v));
      });
    }
    errorMsg = json['errorMsg'];
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.coupons != null) {
      data['coupons'] = this.coupons.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Coupon {
  String activityDesc;
  String activityId;
  String activityType;
  String couponAmount;
  String couponType;
  String createdBy;
  String createdTime;
  String discountRate;
  String endDate;
  String goodsMinAmount;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String organizer;
  String startDate;
  String targetId;
  String targetName;
  String targetType;
  int typeEntityAccount;
  String youhuiType;

  Coupon(
      {this.activityDesc,
        this.activityId,
        this.activityType,
        this.couponAmount,
        this.couponType,
        this.createdBy,
        this.createdTime,
        this.discountRate,
        this.endDate,
        this.goodsMinAmount,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.organizer,
        this.startDate,
        this.targetId,
        this.targetName,
        this.targetType,
        this.typeEntityAccount,
        this.youhuiType});

  Coupon.fromJson(Map<String, dynamic> json) {
    activityDesc = json['activity_desc'];
    activityId = json['activity_id'];
    activityType = json['activity_type'];
    couponAmount = json['coupon_amount'];
    couponType = json['coupon_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    discountRate = json['discount_rate'];
    endDate = json['end_date'];
    goodsMinAmount = json['goods_min_amount'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    organizer = json['organizer'];
    startDate = json['start_date'];
    targetId = json['target_id'];
    targetName = json['target_name'];
    targetType = json['target_type'];
    typeEntityAccount = json['type_entity_account'];
    youhuiType = json['youhui_type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['activity_desc'] = this.activityDesc;
    data['activity_id'] = this.activityId;
    data['activity_type'] = this.activityType;
    data['coupon_amount'] = this.couponAmount;
    data['coupon_type'] = this.couponType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['discount_rate'] = this.discountRate;
    data['end_date'] = this.endDate;
    data['goods_min_amount'] = this.goodsMinAmount;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['organizer'] = this.organizer;
    data['start_date'] = this.startDate;
    data['target_id'] = this.targetId;
    data['target_name'] = this.targetName;
    data['target_type'] = this.targetType;
    data['type_entity_account'] = this.typeEntityAccount;
    data['youhui_type'] = this.youhuiType;
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