class UserFavoriteListResponse {
  String errorMsg;
  List<Favorite> favorites;
  Paginator paginator;
  String status;

  UserFavoriteListResponse(
      {this.errorMsg, this.favorites, this.paginator, this.status});

  UserFavoriteListResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    if (json['favorites'] != null) {
      favorites = new List<Favorite>();
      json['favorites'].forEach((v) {
        favorites.add(new Favorite.fromJson(v));
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
    if (this.favorites != null) {
      data['favorites'] = this.favorites.map((v) => v.toJson()).toList();
    }
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Favorite {
  int favoriteId;
  String favoriteType;
  int id;
  String userName;

  Favorite({this.favoriteId, this.favoriteType, this.id, this.userName});

  Favorite.fromJson(Map<String, dynamic> json) {
    favoriteId = json['favorite_id'];
    favoriteType = json['favorite_type'];
    id = json['id'];
    userName = json['user_name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['favorite_id'] = this.favoriteId;
    data['favorite_type'] = this.favoriteType;
    data['id'] = this.id;
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