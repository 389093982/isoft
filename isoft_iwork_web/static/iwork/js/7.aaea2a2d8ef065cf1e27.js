webpackJsonp([7],{GmnW:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=n("Xxa5"),r=n.n(a),s=n("exGp"),o=n.n(s),i=n("gyMJ"),u={name:"IWorkLayout",data:function(){return{appId:null,version:"1.0.4"}},methods:{tolinkknown:function(){window.open("http://www.linkknown.com/resource/resourceList","_blank")},saveProject:function(){var t=o()(r.a.mark(function t(){return r.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(i._8)();case 2:"SUCCESS"===t.sent.status&&this.$Message.success("保存成功!");case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}(),importProject:function(){var t=o()(r.a.mark(function t(){return r.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(i.P)();case 2:"SUCCESS"===t.sent.status&&this.$Message.success("导入成功!");case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}(),toggleAppid:function(){this.$router.push({path:"/iwork/appidList"})}},mounted:function(){var t=localStorage.getItem("iwork_appId");null===this.appId&&null!=t&&void 0!==t&&(this.appId=JSON.parse(t),this.$Message.success("已选择 AppID "+this.appId.app_name))}},p={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"layout"},[a("Layout",[a("Header",[a("Menu",{attrs:{mode:"horizontal",theme:"dark","active-name":"1"}},[a("div",{staticClass:"layout-iwork"},[a("Row",[a("Col",[a("div",[a("img",{staticClass:"linkknownlogo",attrs:{src:n("O4l+"),title:"API Market 低代码开发平台"},on:{click:t.tolinkknown}})])]),t._v(" "),a("Col",[a("div",{staticStyle:{"margin-left":"10px"}},[t._v("IWork "+t._s(t.version))])])],1)],1),t._v(" "),a("div",{staticClass:"layout-nav"},[a("MenuItem",{attrs:{name:"1"}},[a("Icon",{attrs:{type:"ios-navigate"}}),t._v("\n            使用说明\n          ")],1),t._v(" "),a("MenuItem",{attrs:{name:"2"}},[a("Icon",{attrs:{type:"ios-keypad"}}),t._v("\n            用户协议\n          ")],1),t._v(" "),a("MenuItem",{attrs:{name:"3"}},[a("Icon",{attrs:{type:"ios-analytics"}}),t._v("\n            License管理\n          ")],1),t._v(" "),a("MenuItem",{attrs:{name:"4"}},[a("Icon",{attrs:{type:"ios-paper"}}),t._v(" "),a("span",{on:{click:t.saveProject}},[t._v("保存项目")])],1),t._v(" "),a("MenuItem",{attrs:{name:"4"}},[a("Icon",{attrs:{type:"ios-paper"}}),t._v(" "),a("span",{on:{click:t.importProject}},[t._v("导入项目")])],1),t._v(" "),a("MenuItem",{attrs:{name:"5"}},[a("Icon",{attrs:{type:"ios-paper"}}),t._v(" "),a("span",{on:{click:t.toggleAppid}},[t._v("切换 AppID "),t.appId?a("span",[t._v(t._s(t.appId.app_name))]):t._e()])],1)],1)])],1),t._v(" "),a("Layout",[a("Sider",{style:{background:"#fff"},attrs:{"hide-trigger":""}},[a("Menu",{attrs:{"active-name":"3-2",theme:"light",width:"auto","open-names":["3"],accordion:""}},[a("Submenu",{attrs:{name:"1"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-keypad"}}),t._v("\n              AppID管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"1-1"}},[a("router-link",{attrs:{to:"/iwork/appidList"}},[t._v("AppID管理")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"2"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-keypad"}}),t._v("\n              资源管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"2-1"}},[a("router-link",{attrs:{to:"/iwork/resourceList"}},[t._v("连接资源管理")])],1),t._v(" "),a("MenuItem",{attrs:{name:"2-2"}},[a("router-link",{attrs:{to:"/iwork/migrateList"}},[t._v("数据库迁移管理")])],1),t._v(" "),a("MenuItem",{attrs:{name:"2-3"}},[a("router-link",{attrs:{to:"/iwork/globalVarList"}},[t._v("全局变量管理")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"3"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-navigate"}}),t._v("\n              流程管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"3-1"}},[a("router-link",{attrs:{to:"/iwork/moduleList"}},[t._v("模块管理")])],1),t._v(" "),a("MenuItem",{attrs:{name:"3-2"}},[a("router-link",{attrs:{to:"/iwork/workList"}},[t._v("流程列表")])],1),t._v(" "),a("MenuItem",{attrs:{name:"3-3"}},[a("router-link",{attrs:{to:"/iwork/filterList"}},[t._v("过滤器管理")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"4"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-analytics"}}),t._v("\n              定时任务\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"4-1"}},[a("router-link",{attrs:{to:"/iwork/quartzList"}},[t._v("定时任务列表")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"5"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-barcode"}}),t._v("\n              日志管理\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"5-1"}},[a("router-link",{attrs:{to:"/iwork/runLogList"}},[t._v("日志列表")])],1),t._v(" "),a("MenuItem",{attrs:{name:"5-2"}},[a("router-link",{attrs:{to:"/iwork/dashboard"}},[t._v("统计仪表盘")])],1)],2),t._v(" "),a("Submenu",{attrs:{name:"6"}},[a("template",{slot:"title"},[a("Icon",{attrs:{type:"ios-barcode"}}),t._v("\n              帮助助手\n            ")],1),t._v(" "),a("MenuItem",{attrs:{name:"6-1"}},[a("router-link",{attrs:{to:"/iwork/quickSql"}},[t._v("快捷sql")])],1),t._v(" "),a("MenuItem",{attrs:{name:"6-2"}},[a("router-link",{attrs:{to:"/iwork/audit"}},[t._v("内容审核系统")])],1)],2)],1)],1),t._v(" "),a("Layout",{style:{padding:"24px"}},[a("Content",{style:{padding:"24px",minHeight:"480px",background:"#fff"}},[a("router-view")],1)],1)],1)],1)],1)},staticRenderFns:[]};var c=n("VU/8")(u,p,!1,function(t){n("RGWf")},"data-v-55ab9017",null);e.default=c.exports},"O4l+":function(t,e,n){t.exports=n.p+"static/iwork/img/linkknown.870331f.jpg"},RGWf:function(t,e){}});
//# sourceMappingURL=7.aaea2a2d8ef065cf1e27.js.map