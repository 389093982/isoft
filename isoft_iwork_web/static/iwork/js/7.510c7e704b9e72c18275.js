webpackJsonp([7],{"7Y5H":function(t,e){},"JHo/":function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=n("Xxa5"),a=n.n(r),o=n("exGp"),i=n.n(o),s=n("gyMJ"),u={name:"DBDatacount",props:{tableName:{type:String,default:""},app_id:{type:Number,default:0},resource_name:{type:String,default:""}},data:function(){return{monitors:[]}},methods:{loadDBMonitorData:function(){var t=i()(a.a.mark(function t(){var e;return a.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(s.Q)({_app_id:this.app_id,resource_name:this.resource_name,table_name:this.tableName});case 2:"SUCCESS"===(e=t.sent).status&&(this.monitors=e.monitors);case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}()},mounted:function(){this.loadDBMonitorData()}},c={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return t.tableName?n("div",[n("span",{staticStyle:{color:"red"}},[t._v(t._s(t.tableName))]),t._v(" "),t._l(t.monitors,function(e,r){return n("div",[t._v(t._s(e.table_name)+" - "+t._s(e.data_count)+" -\n    "+t._s(e.last_updated_time)+"\n  ")])})],2):t._e()},staticRenderFns:[]};var _={name:"DashBoard",components:{DBDatacount:n("VU/8")(u,c,!1,function(t){n("7Y5H")},"data-v-f73923d8",null).exports},data:function(){return{monitorMeta1:[],monitorMeta2:[],filterMonitors:[],app_id:null,resource_name:null}},methods:{clickMeta:function(t,e){this.app_id=t,this.resource_name=e,this.filterMonitors=this.monitorMeta2.filter(function(n){return n[0]===t&&n[1]===e})},refreshDBMonitor:function(){var t=i()(a.a.mark(function t(){var e;return a.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(s.H)();case 2:"SUCCESS"===(e=t.sent).status&&(this.monitorMeta1=e.monitorMeta1,this.monitorMeta2=e.monitorMeta2);case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}()},mounted:function(){this.refreshDBMonitor()}},l={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("a",[t._v("数据库数据量监控")]),t._v(" "),n("a",[t._v("iwork 框架健康检查")]),t._v(" "),t._l(t.monitorMeta1,function(e,r){return n("Tag",[n("span",{on:{click:function(n){t.clickMeta(e[0],e[1])}}},[t._v(t._s(e[0])+" - "+t._s(e[1]))])])}),t._v(" "),t._l(t.filterMonitors,function(t,e){return n("div",[n("DBDatacount",{key:t[0]+t[1]+t[2],attrs:{app_id:t[0],resource_name:t[1],"table-name":t[2]}})],1)})],2)},staticRenderFns:[]};var f=n("VU/8")(_,l,!1,function(t){n("bb7t")},"data-v-81aea8e6",null);e.default=f.exports},bb7t:function(t,e){}});
//# sourceMappingURL=7.510c7e704b9e72c18275.js.map