webpackJsonp([17],{"7Mv1":function(t,e){},GmnW:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("Xxa5"),r=a.n(n),s=a("exGp"),o=a.n(s),i=a("gyMJ"),u={name:"IWorkLayout",data:function(){return{appId:null}},methods:{saveProject:function(){var t=o()(r.a.mark(function t(){return r.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(i._16)();case 2:"SUCCESS"==t.sent.status&&this.$Message.success("保存成功!");case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}(),importProject:function(){var t=o()(r.a.mark(function t(){return r.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(i.X)();case 2:"SUCCESS"==t.sent.status&&this.$Message.success("导入成功!");case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}(),toggleAppid:function(){this.$router.push({path:"/iwork/appidList"})}},mounted:function(){var t=localStorage.getItem("appId");null==this.appId&&null!=t&&void 0!=t&&(this.appId=JSON.parse(t),this.$Message.success("已选择 AppID "+this.appId.app_name))}},p={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"layout"},[a("Layout",[a("Header",[a("Menu",{attrs:{mode:"horizontal",theme:"dark","active-name":"1"}},[a("div",{staticClass:"layout-iwork"},[t._v("\n          IWork V1.0.0\n        ")]),t._v(" "),a("div",{staticClass:"layout-nav"},[a("MenuItem",{attrs:{name:"1"}},[a("Icon",{attrs:{type:"ios-navigate"}}),t._v("\n            使用说明\n          ")],1),t._v(" "),a("MenuItem",{attrs:{name:"2"}},[a("Icon",{attrs:{type:"ios-keypad"}}),t._v("\n            用户协议\n          ")],1),t._v(" "),a("MenuItem",{attrs:{name:"3"}},[a("Icon",{attrs:{type:"ios-analytics"}}),t._v("\n            License管理\n          ")],1),t._v(" "),a("MenuItem",{attrs:{name:"4"}},[a("Icon",{attrs:{type:"ios-paper"}}),t._v(" "),a("span",{on:{click:t.saveProject}},[t._v("保存项目")])],1),t._v(" "),a("MenuItem",{attrs:{name:"4"}},[a("Icon",{attrs:{type:"ios-paper"}}),t._v(" "),a("span",{on:{click:t.importProject}},[t._v("导入项目")])],1),t._v(" "),a("MenuItem",{attrs:{name:"5"}},[a("Icon",{attrs:{type:"ios-paper"}}),t._v(" "),a("span",{on:{click:t.toggleAppid}},[t._v("切换 AppID "),t.appId?a("span",[t._v(t._s(t.appId.app_name))]):t._e()])],1)],1)])],1),t._v(" "),a("Layout",[a("Sider",{style:{background:"#fff"},attrs:{"hide-trigger":""}},[a("Menu",{attrs:{"active-name":"3-2",theme:"light",width:"auto","open-names":["3"],accordion:""}},[a("Submenu",{attrs:{name:"1"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-keypad"}}),t._v("\n              AppID管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"1-1"}},[a("router-link",{attrs:{to:"/iwork/appidList"}},[t._v("AppID管理")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"2"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-keypad"}}),t._v("\n              资源管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"2-1"}},[a("router-link",{attrs:{to:"/iwork/resourceList"}},[t._v("资源列表")])],1),t._v(" "),a("MenuItem",{attrs:{name:"2-2"}},[a("router-link",{attrs:{to:"/iwork/migrateList"}},[t._v("数据库迁移管理")])],1),t._v(" "),a("MenuItem",{attrs:{name:"2-3"}},[a("router-link",{attrs:{to:"/iwork/globalVarList"}},[t._v("全局变量管理")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"3"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-navigate"}}),t._v("\n              流程管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"3-1"}},[a("router-link",{attrs:{to:"/iwork/moduleList"}},[t._v("模块管理")])],1),t._v(" "),a("MenuItem",{attrs:{name:"3-2"}},[a("router-link",{attrs:{to:"/iwork/workList"}},[t._v("流程列表")])],1),t._v(" "),a("MenuItem",{attrs:{name:"3-3"}},[a("router-link",{attrs:{to:"/iwork/filterList"}},[t._v("过滤器管理")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"4"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-analytics"}}),t._v("\n              定时任务\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"4-1"}},[a("router-link",{attrs:{to:"/iwork/quartzList"}},[t._v("定时任务列表")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"5"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-barcode"}}),t._v("\n              日志管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"5-1"}},[a("router-link",{attrs:{to:"/iwork/runLogList"}},[t._v("日志列表")])],1),t._v(" "),a("MenuItem",{attrs:{name:"5-2"}},[a("router-link",{attrs:{to:"/iwork/dashboard"}},[t._v("统计仪表盘")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"6"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-barcode"}}),t._v("\n              帮助助手\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"6-1"}},[a("router-link",{attrs:{to:"/iwork/quickSql"}},[t._v("快捷sql")])],1),t._v(" "),a("MenuItem",{attrs:{name:"6-2"}},[a("router-link",{attrs:{to:"/iwork/audit"}},[t._v("内容审核系统")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"7"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-barcode"}}),t._v("\n              内容管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"7-1"}},[a("router-link",{attrs:{to:"/iwork/placementList"}},[t._v("占位符管理")])],1)],2)],1)],1),t._v(" "),a("Layout",{style:{padding:"24px"}},[a("Content",{style:{padding:"24px",minHeight:"480px",background:"#fff"}},[a("router-view")],1)],1)],1)],1)],1)},staticRenderFns:[]};var v=a("VU/8")(u,p,!1,function(t){a("7Mv1")},"data-v-38524a39",null);e.default=v.exports}});
//# sourceMappingURL=17.9d726220b1fec296dafb.js.map