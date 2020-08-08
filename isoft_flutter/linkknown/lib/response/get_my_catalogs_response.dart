class GetMyCatalogsResponse {
  List<Catalog> catalogs;
  String errorMsg;
  String status;

  GetMyCatalogsResponse({this.catalogs, this.errorMsg, this.status});

  GetMyCatalogsResponse.fromJson(Map<String, dynamic> json) {
    if (json['catalogs'] != null) {
      catalogs = new List<Catalog>();
      json['catalogs'].forEach((v) {
        catalogs.add(new Catalog.fromJson(v));
      });
    }
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.catalogs != null) {
      data['catalogs'] = this.catalogs.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}

class Catalog {
  String author;
  String catalogDesc;
  String catalogName;
  String createdBy;
  String createdTime;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;

  Catalog(
      {this.author,
        this.catalogDesc,
        this.catalogName,
        this.createdBy,
        this.createdTime,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime});

  Catalog.fromJson(Map<String, dynamic> json) {
    author = json['author'];
    catalogDesc = json['catalog_desc'];
    catalogName = json['catalog_name'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['author'] = this.author;
    data['catalog_desc'] = this.catalogDesc;
    data['catalog_name'] = this.catalogName;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    return data;
  }
}