webpackJsonp([10],{"8caN":function(e,t){},bhVh:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n("Xxa5"),o=n.n(r),s=n("exGp"),i=n.n(s),a=n("gyMJ"),c=n("GCuT"),u={name:"RunLogList",props:{workId:{type:Number,default:-1}},data:function(){var e=this;return{current_page:1,total:0,offset:10,runLogRecords:[],logLevel:"",columns1:[{title:"tracking_id",key:"tracking_id",width:300},{title:"work_name",key:"work_name"},{title:"log_level",key:"log_level",width:100},{title:"last_updated_time",key:"last_updated_time",render:function(e,t){return e("div",Object(c.c)(new Date(t.row.last_updated_time),"yyyy-MM-dd hh:mm"))}},{title:"操作",key:"operate",render:function(t,n){return t("div",[t("Button",{props:{type:"success",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.$router.push({path:"/iwork/runLogDetail",query:{tracking_id:e.runLogRecords[n.index].tracking_id}})}}},"查看")])}}]}},methods:{loadLevel:function(e){this.logLevel=e,this.refreshRunLogRecordList()},refreshRunLogRecordList:function(){var e=i()(o.a.mark(function e(){var t;return o.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(a.H)(this.getWorkId(),this.logLevel,this.offset,this.current_page);case 2:"SUCCESS"==(t=e.sent).status&&(this.runLogRecords=t.runLogRecords,this.total=t.paginator.totalcount);case 4:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}(),handleChange:function(e){this.current_page=e,this.refreshRunLogRecordList()},handlePageSizeChange:function(e){this.offset=e,this.refreshRunLogRecordList()},getWorkId:function(){return this.workId>0?this.workId:this.$route.query.work_id}},mounted:function(){this.refreshRunLogRecordList()},watch:{$route:function(e,t){this.refreshRunLogRecordList()}}},l={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("span",[n("div",{staticStyle:{"margin-bottom":"10px"}},[n("Button",{attrs:{type:"primary",size:"small"},on:{click:function(t){e.loadLevel("ERROR")}}},[e._v("Error")]),e._v(" "),n("Button",{attrs:{type:"success",size:"small"},on:{click:function(t){e.loadLevel("")}}},[e._v("All")])],1),e._v(" "),n("Table",{attrs:{columns:e.columns1,data:e.runLogRecords,size:"small"}}),e._v(" "),n("Page",{attrs:{total:e.total,"page-size":e.offset,"show-total":"","show-sizer":"",styles:{"text-align":"center","margin-top":"10px"}},on:{"on-change":e.handleChange,"on-page-size-change":e.handlePageSizeChange}})],1)},staticRenderFns:[]};var d=n("VU/8")(u,l,!1,function(e){n("8caN")},"data-v-c3786eb0",null);t.default=d.exports}});
//# sourceMappingURL=10.e8e3bdf06cfdb9a8e8c7.js.map