import 'package:json_annotation/json_annotation.dart';

@JsonSerializable()
class Paginator {
  int currpage;
  int firstpage;
  int lastpage;
  List<int> pages;
  int pagesize;
  int totalcount;
  int totalpages;

  Paginator(this.currpage, this.firstpage, this.lastpage, this.pages,
      this.pagesize, this.totalcount, this.totalpages);

}
