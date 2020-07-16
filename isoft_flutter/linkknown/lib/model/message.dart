
class MessageListResponse {
  String sTrackingId;
  String errorMsg;
  List<Message> messages;
  Paginator paginator;
  String status;

  MessageListResponse(
      {this.sTrackingId,
        this.errorMsg,
        this.messages,
        this.paginator,
        this.status});

  MessageListResponse.fromJson(Map<String, dynamic> json) {
    sTrackingId = json['__trackingId'];
    errorMsg = json['errorMsg'];
    if (json['messages'] != null) {
      messages = new List<Message>();
      json['messages'].forEach((v) {
        messages.add(new Message.fromJson(v));
      });
    }
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['__trackingId'] = this.sTrackingId;
    data['errorMsg'] = this.errorMsg;
    if (this.messages != null) {
      data['messages'] = this.messages.map((v) => v.toJson()).toList();
    }
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Message {
  int id;
  String lastUpdatedTime;
  String messageText;
  String messageType;
  String userName;

  Message(
      {this.id,
        this.lastUpdatedTime,
        this.messageText,
        this.messageType,
        this.userName});

  Message.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    lastUpdatedTime = json['last_updated_time'];
    messageText = json['message_text'];
    messageType = json['message_type'];
    userName = json['user_name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['message_text'] = this.messageText;
    data['message_type'] = this.messageType;
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