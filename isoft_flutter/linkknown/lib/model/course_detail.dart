
class CourseDetailResponse {
  List<CVideo> cVideos;
  Course course;
  String errorMsg;
  String status;

  CourseDetailResponse(
      {this.cVideos,
        this.course,
        this.errorMsg,
        this.status});

  CourseDetailResponse.fromJson(Map<String, dynamic> json) {
    if (json['cVideos'] != null) {
      cVideos = new List<CVideo>();
      json['cVideos'].forEach((v) {
        cVideos.add(new CVideo.fromJson(v));
      });
    }
    course =
    json['course'] != null ? new Course.fromJson(json['course']) : null;
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.cVideos != null) {
      data['cVideos'] = this.cVideos.map((v) => v.toJson()).toList();
    }
    if (this.course != null) {
      data['course'] = this.course.toJson();
    }
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}

class CVideo {
  int courseId;
  int duration;
  String firstPlay;
  int id;
  String secondPlay;
  String videoName;

  CVideo(
      {this.courseId,
        this.duration,
        this.firstPlay,
        this.id,
        this.secondPlay,
        this.videoName});

  CVideo.fromJson(Map<String, dynamic> json) {
    courseId = json['course_id'];
    duration = json['duration'];
    firstPlay = json['first_play'];
    id = json['id'];
    secondPlay = json['second_play'];
    videoName = json['video_name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['course_id'] = this.courseId;
    data['duration'] = this.duration;
    data['first_play'] = this.firstPlay;
    data['id'] = this.id;
    data['second_play'] = this.secondPlay;
    data['video_name'] = this.videoName;
    return data;
  }
}

class Course {
  int comments;
  String courseAuthor;
  String courseLabel;
  String courseName;
  int courseNumber;
  String courseShortDesc;
  String courseSubType;
  String courseType;
  String createdBy;
  String createdTime;
  String customTag;
  String customTagName;
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

  Course(
      {this.comments,
        this.courseAuthor,
        this.courseLabel,
        this.courseName,
        this.courseNumber,
        this.courseShortDesc,
        this.courseSubType,
        this.courseType,
        this.createdBy,
        this.createdTime,
        this.customTag,
        this.customTagName,
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

  Course.fromJson(Map<String, dynamic> json) {
    comments = json['comments'];
    courseAuthor = json['course_author'];
    courseLabel = json['course_label'];
    courseName = json['course_name'];
    courseNumber = json['course_number'];
    courseShortDesc = json['course_short_desc'];
    courseSubType = json['course_sub_type'];
    courseType = json['course_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    customTag = json['custom_tag'];
    customTagName = json['custom_tag_name'];
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
    data['comments'] = this.comments;
    data['course_author'] = this.courseAuthor;
    data['course_label'] = this.courseLabel;
    data['course_name'] = this.courseName;
    data['course_number'] = this.courseNumber;
    data['course_short_desc'] = this.courseShortDesc;
    data['course_sub_type'] = this.courseSubType;
    data['course_type'] = this.courseType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['custom_tag'] = this.customTag;
    data['custom_tag_name'] = this.customTagName;
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