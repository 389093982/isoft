class SearchCouponForPayResponse {
  List<Coupon> coupons;
  String errorMsg;
  String status;

  SearchCouponForPayResponse({this.coupons, this.errorMsg, this.status});

  SearchCouponForPayResponse.fromJson(Map<String, dynamic> json) {
    if (json['coupons'] != null) {
      coupons = new List<Coupon>();
      json['coupons'].forEach((v) {
        coupons.add(new Coupon.fromJson(v));
      });
    }
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.coupons != null) {
      data['coupons'] = this.coupons.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}

class Coupon {
  String activityDesc;
  String activityId;
  String couponAmount;
  String couponId;
  String couponOwner;
  String couponState;
  String couponType;
  String createdBy;
  String createdTime;
  String discountRate;
  String endDate;
  String goodsMinAmount;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String startDate;
  String targetId;
  String targetName;
  String targetType;
  String youhuiType;

  Coupon(
      {this.activityDesc,
        this.activityId,
        this.couponAmount,
        this.couponId,
        this.couponOwner,
        this.couponState,
        this.couponType,
        this.createdBy,
        this.createdTime,
        this.discountRate,
        this.endDate,
        this.goodsMinAmount,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.startDate,
        this.targetId,
        this.targetName,
        this.targetType,
        this.youhuiType});

  Coupon.fromJson(Map<String, dynamic> json) {
    activityDesc = json['activity_desc'];
    activityId = json['activity_id'];
    couponAmount = json['coupon_amount'];
    couponId = json['coupon_id'];
    couponOwner = json['coupon_owner'];
    couponState = json['coupon_state'];
    couponType = json['coupon_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    discountRate = json['discount_rate'];
    endDate = json['end_date'];
    goodsMinAmount = json['goods_min_amount'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    startDate = json['start_date'];
    targetId = json['target_id'];
    targetName = json['target_name'];
    targetType = json['target_type'];
    youhuiType = json['youhui_type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['activity_desc'] = this.activityDesc;
    data['activity_id'] = this.activityId;
    data['coupon_amount'] = this.couponAmount;
    data['coupon_id'] = this.couponId;
    data['coupon_owner'] = this.couponOwner;
    data['coupon_state'] = this.couponState;
    data['coupon_type'] = this.couponType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['discount_rate'] = this.discountRate;
    data['end_date'] = this.endDate;
    data['goods_min_amount'] = this.goodsMinAmount;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['start_date'] = this.startDate;
    data['target_id'] = this.targetId;
    data['target_name'] = this.targetName;
    data['target_type'] = this.targetType;
    data['youhui_type'] = this.youhuiType;
    return data;
  }
}