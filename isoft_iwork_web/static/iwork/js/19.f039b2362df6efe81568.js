webpackJsonp([19],{"7tHR":function(t,e){},bhVh:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=n("Xxa5"),o=n.n(r),s=n("exGp"),i=n.n(s),a=n("gyMJ"),u=n("GCuT"),c={name:"RunLogList",props:{workId:{type:Number,default:-1}},data:function(){var t=this;return{current_page:1,total:0,offset:10,runLogRecords:[],logLevel:"",columns1:[{title:"tracking_id",key:"tracking_id",width:300},{title:"work_name",key:"work_name"},{title:"log_level",key:"log_level",width:100},{title:"last_updated_time",key:"last_updated_time",render:function(t,e){return t("div",Object(u.c)(new Date(e.row.last_updated_time),"yyyy-MM-dd hh:mm"))}},{title:"操作",key:"operate",render:function(e,n){return e("div",[e("Button",{props:{type:"success",size:"small"},style:{marginRight:"5px"},on:{click:function(){t.$router.push({path:"/iwork/runLogDetail",query:{tracking_id:t.runLogRecords[n.index].tracking_id}})}}},"查看")])}}]}},methods:{loadLevel:function(t){this.logLevel=t,this.refreshRunlogRecordList()},refreshRunlogRecordList:function(){var t=i()(o.a.mark(function t(){var e;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(a.H)(this.getWorkId(),this.logLevel,this.offset,this.current_page);case 2:"SUCCESS"==(e=t.sent).status&&(this.runLogRecords=e.runLogRecords,this.total=e.paginator.totalcount);case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}(),handleChange:function(t){this.current_page=t,this.refreshRunlogRecordList()},handlePageSizeChange:function(t){this.offset=t,this.refreshRunlogRecordList()},getWorkId:function(){return this.workId>0?this.workId:this.$route.query.work_id}},mounted:function(){this.refreshRunlogRecordList()},watch:{$route:function(t,e){this.refreshRunlogRecordList()}}},l={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("span",[n("div",{staticStyle:{"margin-bottom":"10px"}},[n("Button",{attrs:{type:"primary",size:"small"},on:{click:function(e){t.loadLevel("ERROR")}}},[t._v("Error")]),t._v(" "),n("Button",{attrs:{type:"success",size:"small"},on:{click:function(e){t.loadLevel("")}}},[t._v("All")])],1),t._v(" "),n("Table",{attrs:{columns:t.columns1,data:t.runLogRecords,size:"small"}}),t._v(" "),n("Page",{attrs:{total:t.total,"page-size":t.offset,"show-total":"","show-sizer":"",styles:{"text-align":"center","margin-top":"10px"}},on:{"on-change":t.handleChange,"on-page-size-change":t.handlePageSizeChange}})],1)},staticRenderFns:[]};var d=n("VU/8")(c,l,!1,function(t){n("7tHR")},"data-v-0a4d8d73",null);e.default=d.exports}});
//# sourceMappingURL=19.f039b2362df6efe81568.js.map