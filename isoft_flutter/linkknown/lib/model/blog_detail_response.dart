class BlogDetailResponse {
  Blog blog;
  String errorMsg;
  String status;

  BlogDetailResponse({this.blog, this.errorMsg, this.status});

  BlogDetailResponse.fromJson(Map<String, dynamic> json) {
    blog = json['blog'] != null ? new Blog.fromJson(json['blog']) : null;
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.blog != null) {
      data['blog'] = this.blog.toJson();
    }
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}

class Blog {
  String author;
  int blogStatus;
  String blogTitle;
  int blogType;
  String catalogName;
  int comments;
  String content;
  String createdBy;
  String createdTime;
  String customTag;
  int edits;
  String firstImg;
  int id;
  String keyWords;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String linkHref;
  int toTop;
  int views;

  Blog(
      {this.author,
        this.blogStatus,
        this.blogTitle,
        this.blogType,
        this.catalogName,
        this.comments,
        this.content,
        this.createdBy,
        this.createdTime,
        this.customTag,
        this.edits,
        this.firstImg,
        this.id,
        this.keyWords,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.linkHref,
        this.toTop,
        this.views});

  Blog.fromJson(Map<String, dynamic> json) {
    author = json['author'];
    blogStatus = json['blog_status'];
    blogTitle = json['blog_title'];
    blogType = json['blog_type'];
    catalogName = json['catalog_name'];
    comments = json['comments'];
    content = json['content'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    customTag = json['custom_tag'];
    edits = json['edits'];
    firstImg = json['first_img'];
    id = json['id'];
    keyWords = json['key_words'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    linkHref = json['link_href'];
    toTop = json['to_top'];
    views = json['views'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['author'] = this.author;
    data['blog_status'] = this.blogStatus;
    data['blog_title'] = this.blogTitle;
    data['blog_type'] = this.blogType;
    data['catalog_name'] = this.catalogName;
    data['comments'] = this.comments;
    data['content'] = this.content;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['custom_tag'] = this.customTag;
    data['edits'] = this.edits;
    data['first_img'] = this.firstImg;
    data['id'] = this.id;
    data['key_words'] = this.keyWords;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['link_href'] = this.linkHref;
    data['to_top'] = this.toTop;
    data['views'] = this.views;
    return data;
  }
}