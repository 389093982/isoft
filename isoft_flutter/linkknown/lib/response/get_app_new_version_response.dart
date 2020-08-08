
class GetAppNewVersionResponse {
  String errorMsg;
  NewAppVersion newAppVersion;
  String status;

  GetAppNewVersionResponse({this.errorMsg, this.newAppVersion, this.status});

  GetAppNewVersionResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    newAppVersion = json['newAppVersion'] != null
        ? new NewAppVersion.fromJson(json['newAppVersion'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    if (this.newAppVersion != null) {
      data['newAppVersion'] = this.newAppVersion.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class NewAppVersion {
  String androidDownloadUrl;
  String appPaddingVersion;
  String appShowVersion;
  String createdBy;
  String createdTime;
  String forceUpdate;
  int id;
  String iosDownloadUrl;

  NewAppVersion(
      {this.androidDownloadUrl,
        this.appPaddingVersion,
        this.appShowVersion,
        this.createdBy,
        this.createdTime,
        this.forceUpdate,
        this.id,
        this.iosDownloadUrl});

  NewAppVersion.fromJson(Map<String, dynamic> json) {
    androidDownloadUrl = json['android_download_url'];
    appPaddingVersion = json['app_padding_version'];
    appShowVersion = json['app_show_version'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    forceUpdate = json['force_update'];
    id = json['id'];
    iosDownloadUrl = json['ios_download_url'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['android_download_url'] = this.androidDownloadUrl;
    data['app_padding_version'] = this.appPaddingVersion;
    data['app_show_version'] = this.appShowVersion;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['force_update'] = this.forceUpdate;
    data['id'] = this.id;
    data['ios_download_url'] = this.iosDownloadUrl;
    return data;
  }
}
