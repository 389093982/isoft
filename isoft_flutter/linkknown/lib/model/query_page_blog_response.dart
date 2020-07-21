class QueryPageBlogResponse {
  List<Blog> blogs;
  String errorMsg;
  Paginator paginator;
  String status;

  QueryPageBlogResponse(
      {this.blogs, this.errorMsg, this.paginator, this.status});

  QueryPageBlogResponse.fromJson(Map<String, dynamic> json) {
    if (json['blogs'] != null) {
      blogs = new List<Blog>();
      json['blogs'].forEach((v) {
        blogs.add(new Blog.fromJson(v));
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
    if (this.blogs != null) {
      data['blogs'] = this.blogs.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Blog {
  String author;
  int blogStatus;
  String blogTitle;
  String catalogName;
  int comments;
  String createdTime;
  String firstImg;
  int id;
  String lastUpdatedTime;
  int toTop;
  int views;

  Blog(
      {this.author,
        this.blogStatus,
        this.blogTitle,
        this.catalogName,
        this.comments,
        this.createdTime,
        this.firstImg,
        this.id,
        this.lastUpdatedTime,
        this.toTop,
        this.views});

  Blog.fromJson(Map<String, dynamic> json) {
    author = json['author'];
    blogStatus = json['blog_status'];
    blogTitle = json['blog_title'];
    catalogName = json['catalog_name'];
    comments = json['comments'];
    createdTime = json['created_time'];
    firstImg = json['first_img'];
    id = json['id'];
    lastUpdatedTime = json['last_updated_time'];
    toTop = json['to_top'];
    views = json['views'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['author'] = this.author;
    data['blog_status'] = this.blogStatus;
    data['blog_title'] = this.blogTitle;
    data['catalog_name'] = this.catalogName;
    data['comments'] = this.comments;
    data['created_time'] = this.createdTime;
    data['first_img'] = this.firstImg;
    data['id'] = this.id;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['to_top'] = this.toTop;
    data['views'] = this.views;
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