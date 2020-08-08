
class FileUploadResponse {
  int duration;
  String errorMsg;
  String fileName;
  String fileServerPath;
  int fileSize;
  String status;

  FileUploadResponse(
      {this.duration,
        this.errorMsg,
        this.fileName,
        this.fileServerPath,
        this.fileSize,
        this.status});

  FileUploadResponse.fromJson(Map<String, dynamic> json) {
    duration = json['duration'];
    errorMsg = json['errorMsg'];
    fileName = json['fileName'];
    fileServerPath = json['fileServerPath'];
    fileSize = json['fileSize'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['duration'] = this.duration;
    data['errorMsg'] = this.errorMsg;
    data['fileName'] = this.fileName;
    data['fileServerPath'] = this.fileServerPath;
    data['fileSize'] = this.fileSize;
    data['status'] = this.status;
    return data;
  }
}
