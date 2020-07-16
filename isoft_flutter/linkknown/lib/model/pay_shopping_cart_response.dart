class PayShoppinpCartResponse {
  String status;
  String errorMsg;
  List<GoodsData> goodsData;
  Paginator paginator;

  PayShoppinpCartResponse({this.errorMsg, this.goodsData, this.paginator, this.status});

  PayShoppinpCartResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    if (json['goodsData'] != null) {
      goodsData = new List<GoodsData>();
      json['goodsData'].forEach((v) {
        goodsData.add(new GoodsData.fromJson(v));
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
    if (this.goodsData != null) {
      data['goodsData'] = this.goodsData.map((v) => v.toJson()).toList();
    }
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class GoodsData {
  String addTime;
  int comments;
  String courseAuthor;
  String courseLabel;
  String courseName;
  int courseNumber;
  String courseShortDes;
  String courseShortDesc;
  String courseSubType;
  String courseType;
  String createdBy;
  String createdTime;
  String customTag;
  String customTagName;
  String goodsCount;
  String goodsId;
  String goodsPriceOnAdd;
  String goodsType;
  int id;
  String isCharge;
  String isShowOldPrice;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String mediaType;
  String oldPrice;
  int preListFree;
  String price;
  int score;
  String smallImage;
  int watchNumber;

  GoodsData(
      {this.addTime,
        this.comments,
        this.courseAuthor,
        this.courseLabel,
        this.courseName,
        this.courseNumber,
        this.courseShortDes,
        this.courseShortDesc,
        this.courseSubType,
        this.courseType,
        this.createdBy,
        this.createdTime,
        this.customTag,
        this.customTagName,
        this.goodsCount,
        this.goodsId,
        this.goodsPriceOnAdd,
        this.goodsType,
        this.id,
        this.isCharge,
        this.isShowOldPrice,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.mediaType,
        this.oldPrice,
        this.preListFree,
        this.price,
        this.score,
        this.smallImage,
        this.watchNumber});

  GoodsData.fromJson(Map<String, dynamic> json) {
    addTime = json['add_time'];
    comments = json['comments'];
    courseAuthor = json['course_author'];
    courseLabel = json['course_label'];
    courseName = json['course_name'];
    courseNumber = json['course_number'];
    courseShortDes = json['course_short_des'];
    courseShortDesc = json['course_short_desc'];
    courseSubType = json['course_sub_type'];
    courseType = json['course_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    customTag = json['custom_tag'];
    customTagName = json['custom_tag_name'];
    goodsCount = json['goods_count'];
    goodsId = json['goods_id'];
    goodsPriceOnAdd = json['goods_price_on_add'];
    goodsType = json['goods_type'];
    id = json['id'];
    isCharge = json['isCharge'];
    isShowOldPrice = json['is_show_old_price'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    mediaType = json['media_type'];
    oldPrice = json['old_price'];
    preListFree = json['preListFree'];
    price = json['price'];
    score = json['score'];
    smallImage = json['small_image'];
    watchNumber = json['watch_number'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['add_time'] = this.addTime;
    data['comments'] = this.comments;
    data['course_author'] = this.courseAuthor;
    data['course_label'] = this.courseLabel;
    data['course_name'] = this.courseName;
    data['course_number'] = this.courseNumber;
    data['course_short_des'] = this.courseShortDes;
    data['course_short_desc'] = this.courseShortDesc;
    data['course_sub_type'] = this.courseSubType;
    data['course_type'] = this.courseType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['custom_tag'] = this.customTag;
    data['custom_tag_name'] = this.customTagName;
    data['goods_count'] = this.goodsCount;
    data['goods_id'] = this.goodsId;
    data['goods_price_on_add'] = this.goodsPriceOnAdd;
    data['goods_type'] = this.goodsType;
    data['id'] = this.id;
    data['isCharge'] = this.isCharge;
    data['is_show_old_price'] = this.isShowOldPrice;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['media_type'] = this.mediaType;
    data['old_price'] = this.oldPrice;
    data['preListFree'] = this.preListFree;
    data['price'] = this.price;
    data['score'] = this.score;
    data['small_image'] = this.smallImage;
    data['watch_number'] = this.watchNumber;
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