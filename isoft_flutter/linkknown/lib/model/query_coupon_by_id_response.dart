class QueryCouponByIdResponse {
  SelectedCoupon coupon;
  String errorMsg;
  String status;

  QueryCouponByIdResponse({this.coupon, this.errorMsg, this.status});

  QueryCouponByIdResponse.fromJson(Map<String, dynamic> json) {
    coupon =
    json['coupon'] != null ? new SelectedCoupon.fromJson(json['coupon']) : null;
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.coupon != null) {
      data['coupon'] = this.coupon.toJson();
    }
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}

class SelectedCoupon {
  String activityId;
  String couponAmount;
  String couponId;
  String couponOwner;
  String couponState;
  String couponType;
  String createdBy;
  String createdTime;
  String discountRate;
  String goodsMinAmount;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String targetId;
  String targetName;
  String targetType;
  String youhuiType;

  SelectedCoupon(
      {this.activityId,
        this.couponAmount,
        this.couponId,
        this.couponOwner,
        this.couponState,
        this.couponType,
        this.createdBy,
        this.createdTime,
        this.discountRate,
        this.goodsMinAmount,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.targetId,
        this.targetName,
        this.targetType,
        this.youhuiType});

  SelectedCoupon.fromJson(Map<String, dynamic> json) {
    activityId = json['activity_id'];
    couponAmount = json['coupon_amount'];
    couponId = json['coupon_id'];
    couponOwner = json['coupon_owner'];
    couponState = json['coupon_state'];
    couponType = json['coupon_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    discountRate = json['discount_rate'];
    goodsMinAmount = json['goods_min_amount'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    targetId = json['target_id'];
    targetName = json['target_name'];
    targetType = json['target_type'];
    youhuiType = json['youhui_type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['activity_id'] = this.activityId;
    data['coupon_amount'] = this.couponAmount;
    data['coupon_id'] = this.couponId;
    data['coupon_owner'] = this.couponOwner;
    data['coupon_state'] = this.couponState;
    data['coupon_type'] = this.couponType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['discount_rate'] = this.discountRate;
    data['goods_min_amount'] = this.goodsMinAmount;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['target_id'] = this.targetId;
    data['target_name'] = this.targetName;
    data['target_type'] = this.targetType;
    data['youhui_type'] = this.youhuiType;
    return data;
  }
}