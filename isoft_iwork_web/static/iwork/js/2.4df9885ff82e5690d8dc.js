webpackJsonp([2],{AVUi:function(e,r){},Gu7T:function(e,r,t){"use strict";r.__esModule=!0;var s,a=t("c/Tr"),o=(s=a)&&s.__esModule?s:{default:s};r.default=function(e){if(Array.isArray(e)){for(var r=0,t=Array(e.length);r<e.length;r++)t[r]=e[r];return t}return(0,o.default)(e)}},"PmV+":function(e,r){},eLfx:function(e,r){},fCil:function(e,r,t){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var s=t("Xxa5"),a=t.n(s),o=t("exGp"),n=t.n(o),u=t("GCuT"),i=t("gyMJ"),c=t("Nb9Q"),l=t("XZCL"),d=t("I1g2"),p={name:"ResourceEdit",components:{WorkStepComponent:t("leY6").a,ISimpleBtnTriggerModal:d.a},data:function(){return{globalVars:[],formValidate:{resource_id:0,resource_name:"",resource_type:"",resource_url:"",resource_dsn:"",resource_username:"",resource_password:"",env_name:""},ruleValidate:{resource_name:[{required:!0,message:"resource_name 不能为空!",trigger:"blur"}],resource_type:[{required:!0,message:"resource_type 不能为空!",trigger:"change"}],resource_url:[{required:!0,message:"resource_url 不能为空!",trigger:"blur"}],resource_dsn:[{required:!0,message:"resource_dsn 不能为空!",trigger:"blur"}],resource_username:[{required:!0,message:"resource_username 不能为空!",trigger:"blur"}],resource_password:[{required:!0,message:"resource_password 不能为空!",trigger:"blur"}]}}},methods:{checkShow:function(e){return"db"!==e},dragstart:function(e,r){e.dataTransfer.setData("Text",r)},allowDrop:function(e){e.preventDefault()},drop:function(e,r){e.stopPropagation(),e.preventDefault();var t=e.dataTransfer.getData("Text");this.$set(this.formInline,r,t)},initData:function(){var e=n()(a.a.mark(function e(r){var t;return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return this.$refs.triggerModal.triggerClick(),e.next=3,Object(i.M)(r);case 3:"SUCCESS"==(t=e.sent).status&&(this.formValidate.resource_id=t.resource.id,this.formValidate.resource_name=t.resource.resource_name,this.formValidate.resource_type=t.resource.resource_type,this.formValidate.resource_url=t.resource.resource_url,this.formValidate.resource_dsn=t.resource.resource_dsn,this.formValidate.resource_username=t.resource.resource_username,this.formValidate.resource_password=t.resource.resource_password,this.formValidate.env_name=t.resource.env_name);case 5:case"end":return e.stop()}},e,this)}));return function(r){return e.apply(this,arguments)}}(),handleSubmit:function(e){var r,t=this;this.$refs[e].validate((r=n()(a.a.mark(function e(r){return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:if(!r){e.next=5;break}return e.next=3,Object(i.s)(t.formValidate.resource_id,t.formValidate.resource_name,t.formValidate.resource_type,t.formValidate.resource_url,t.formValidate.resource_dsn,t.formValidate.resource_username,t.formValidate.resource_password);case 3:"SUCCESS"==e.sent.status?(t.$Message.success("提交成功!"),t.$refs.triggerModal.hideModal(),t.handleReset("formValidate"),t.$emit("handleSuccess")):t.$Message.error("提交失败!");case 5:case"end":return e.stop()}},e,t)})),function(e){return r.apply(this,arguments)}))},handleReset:function(e){this.$refs[e].resetFields()},refreshAllGlobalVars:function(){var e=n()(a.a.mark(function e(){var r;return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(i.D)();case 2:"SUCCESS"==(r=e.sent).status&&(this.globalVars=r.globalVars);case 4:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}()},mounted:function(){this.refreshAllGlobalVars()}},m={render:function(){var e=this,r=e.$createElement,t=e._self._c||r;return t("ISimpleBtnTriggerModal",{ref:"triggerModal",attrs:{"btn-text":"新增资源信息","btn-size":"small","modal-title":"新增/编辑资源信息","modal-top":"50px","modal-width":1e3}},[t("Row",[t("Col",{attrs:{span:"6"}},[t("Scroll",e._l(e.globalVars,function(r,s){return t("Tag",[t("span",{attrs:{draggable:"true"},on:{dragstart:function(t){e.dragstart(t,r)}}},[e._v(e._s(r))])])}))],1),e._v(" "),t("Col",{attrs:{span:"18"}},[t("Form",{ref:"formValidate",attrs:{model:e.formValidate,rules:e.ruleValidate,"label-width":140}},[t("FormItem",{attrs:{label:"resource_name",prop:"resource_name"}},[t("Input",{attrs:{placeholder:"请输入 resource_name"},model:{value:e.formValidate.resource_name,callback:function(r){e.$set(e.formValidate,"resource_name","string"==typeof r?r.trim():r)},expression:"formValidate.resource_name"}})],1),e._v(" "),t("FormItem",{attrs:{label:"resource_type",prop:"resource_type"}},[t("Select",{attrs:{placeholder:"请选择 resource_type"},model:{value:e.formValidate.resource_type,callback:function(r){e.$set(e.formValidate,"resource_type",r)},expression:"formValidate.resource_type"}},[t("Option",{attrs:{value:"db"}},[e._v("db")]),e._v(" "),t("Option",{attrs:{value:"sftp"}},[e._v("sftp")]),e._v(" "),t("Option",{attrs:{value:"ssh"}},[e._v("ssh")])],1)],1),e._v(" "),e.checkShow(e.formValidate.resource_type)?t("FormItem",{attrs:{label:"resource_url",prop:"resource_url"}},[t("Input",{attrs:{placeholder:"请输入 resource_url"},model:{value:e.formValidate.resource_url,callback:function(r){e.$set(e.formValidate,"resource_url","string"==typeof r?r.trim():r)},expression:"formValidate.resource_url"}})],1):e._e(),e._v(" "),t("FormItem",{attrs:{label:"resource_dsn",prop:"resource_dsn"}},[t("Input",{attrs:{type:"textarea",rows:3,placeholder:"请输入 resource_dsn"},on:{drop:function(r){e.drop(r,"resource_dsn")},dragover:function(r){e.allowDrop(r)}},model:{value:e.formValidate.resource_dsn,callback:function(r){e.$set(e.formValidate,"resource_dsn","string"==typeof r?r.trim():r)},expression:"formValidate.resource_dsn"}})],1),e._v(" "),e.checkShow(e.formValidate.resource_type)?t("FormItem",{attrs:{label:"resource_username",prop:"resource_username"}},[t("Input",{attrs:{placeholder:"请输入 resource_username"},model:{value:e.formValidate.resource_username,callback:function(r){e.$set(e.formValidate,"resource_username","string"==typeof r?r.trim():r)},expression:"formValidate.resource_username"}})],1):e._e(),e._v(" "),e.checkShow(e.formValidate.resource_type)?t("FormItem",{attrs:{label:"resource_password",prop:"resource_password"}},[t("Input",{attrs:{placeholder:"请输入 resource_password"},model:{value:e.formValidate.resource_password,callback:function(r){e.$set(e.formValidate,"resource_password","string"==typeof r?r.trim():r)},expression:"formValidate.resource_password"}})],1):e._e(),e._v(" "),t("FormItem",[t("Button",{staticStyle:{"margin-right":"6px"},attrs:{type:"success"},on:{click:function(r){e.handleSubmit("formValidate")}}},[e._v("提交")]),e._v(" "),t("Button",{staticStyle:{"margin-right":"6px"},attrs:{type:"warning"},on:{click:function(r){e.handleReset("formValidate")}}},[e._v("重置")])],1)],1)],1)],1)],1)},staticRenderFns:[]};var f=t("VU/8")(p,m,!1,function(e){t("eLfx")},"data-v-7992c213",null).exports,h={name:"ResourceList",components:{ISimpleLeftRightRow:c.default,ISimpleSearch:l.default,ResourceEdit:f},data:function(){var e=this;return{mysqlDsnEgg:"root:123456@tcp(127.0.0.1:3306)/iwork_learning?allowNativePasswords=true&charset=utf8&loc=Asia%2FShanghai&parseTime=True",current_page:1,total:0,offset:10,search:"",resources:[],columns1:[{title:"resource_name",key:"resource_name",width:150},{title:"resource_type",key:"resource_type",width:120},{title:"resource_url",key:"resource_url",width:150,ellipsis:!0,tooltip:!0},{title:"resource_dsn",key:"resource_dsn",width:320,ellipsis:!0,tooltip:!0},{title:"resource_username",key:"resource_username",width:160},{title:"resource_password",key:"resource_password",width:200},{title:"last_updated_time",key:"last_updated_time",width:150,render:function(e,r){return e("div",Object(u.f)(new Date(r.row.last_updated_time),"yyyy-MM-dd hh:mm"))}},{title:"操作",key:"operate",width:280,fixed:"right",render:function(r,t){return r("div",[r("Button",{props:{type:"error",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.validateResource(e.resources[t.index].id)}}},"连接测试"),r("Button",{props:{type:"success",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.$refs.resource_edit.initData(e.resources[t.index].id)}}},"编辑"),r("Button",{props:{type:"warning",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.deleteResource(e.resources[t.index].id)}}},"删除")])}}]}},methods:{handleCopy:function(e){var r=this;Object(u.e)(e,function(){r.$Message.success("复制成功！")})},validateResource:function(){var e=n()(a.a.mark(function e(r){var t;return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(i._11)(r);case 2:"SUCCESS"===(t=e.sent).status?this.$Message.success("验证通过!"):this.$Message.error("验证失败!"+t.errorMsg);case 4:case"end":return e.stop()}},e,this)}));return function(r){return e.apply(this,arguments)}}(),deleteResource:function(){var e=n()(a.a.mark(function e(r){return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(i.j)(r);case 2:"SUCCESS"===e.sent.status&&this.refreshResourceList();case 4:case"end":return e.stop()}},e,this)}));return function(r){return e.apply(this,arguments)}}(),refreshResourceList:function(){var e=n()(a.a.mark(function e(){var r;return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(i._5)(this.offset,this.current_page,this.search);case 2:"SUCCESS"===(r=e.sent).status&&(this.resources=r.resources,this.total=r.paginator.totalcount);case 4:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}(),handleChange:function(e){this.current_page=e,this.refreshResourceList()},handlePageSizeChange:function(e){this.offset=e,this.refreshResourceList()},handleSearch:function(e){this.offset=10,this.current_page=1,this.search=e,this.refreshResourceList()}},mounted:function(){this.refreshResourceList()}},_={render:function(){var e=this,r=e.$createElement,t=e._self._c||r;return t("div",{staticStyle:{margin:"10px"}},[t("div",{staticStyle:{display:"flex","margin-bottom":"10px","margin-right":"10px"}},[t("div",{staticStyle:{width:"15%"}},[t("ResourceEdit",{ref:"resource_edit",on:{handleSuccess:e.refreshResourceList}})],1),e._v(" "),t("div",{staticStyle:{width:"35%"}},[t("span",{staticClass:"dsnBox",staticStyle:{position:"relative"}},[t("span",{staticStyle:{cursor:"pointer"}},[e._v("查看常用 dsn 链接串示例")]),e._v(" "),t("div",{staticClass:"dsnDemo"},[t("Tag",[e._v("mysql")]),e._v(" "),t("span",{on:{click:function(r){e.handleCopy(e.mysqlDsnEgg)}}},[e._v(e._s(e.mysqlDsnEgg))])],1)])]),e._v(" "),t("div",{staticStyle:{width:"50%"}},[t("ISimpleSearch",{on:{handleSimpleSearch:e.handleSearch}})],1)]),e._v(" "),t("Table",{attrs:{border:"",columns:e.columns1,data:e.resources,size:"small"}}),e._v(" "),t("Page",{attrs:{total:e.total,"page-size":e.offset,"show-total":"","show-sizer":"",styles:{"text-align":"center","margin-top":"10px"}},on:{"on-change":e.handleChange,"on-page-size-change":e.handlePageSizeChange}})],1)},staticRenderFns:[]};var g=t("VU/8")(h,_,!1,function(e){t("PmV+")},"data-v-6d46ce28",null);r.default=g.exports},leY6:function(e,r,t){"use strict";var s=t("Gu7T"),a=t.n(s),o=t("lHA8"),n=t.n(o),u=t("Xxa5"),i=t.n(u),c=t("exGp"),l=t.n(c),d=t("gyMJ"),p=t("GCuT"),m={name:"WorkStepComponent",data:function(){return{showComponentDrawer:!1,nodeMetas:[],works:[],moduleNames:[],showAllModuleName:!1,nodeGroups:[],showAllNodeGroup:!1}},methods:{showComponent:function(e){return!Object(p.j)(e,["work_start","work_end"])},dragstart:function(e,r){e.dataTransfer.setData("Text",r)},toggleShow:function(){this.showComponentDrawer=!this.showComponentDrawer},refreshAllWorks:function(){var e=l()(i.a.mark(function e(){var r;return i.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(d.C)();case 2:"SUCCESS"===(r=e.sent).status&&(this.works=r.works,this.moduleNames=this.getUniqueArr(this.works.map(function(e){return e.module_name})));case 4:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}(),getUniqueArr:function(e){var r=new n.a(e);return[].concat(a()(r))},refreshNodeMetas:function(){var e=l()(i.a.mark(function e(){var r;return i.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(d.K)("nodeMetas");case 2:"SUCCESS"===(r=e.sent).status&&(this.nodeMetas=r.nodeMetas,this.nodeGroups=this.getUniqueArr(r.nodeMetas.map(function(e){return e.group})));case 4:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}()},mounted:function(){this.refreshAllWorks(),this.refreshNodeMetas()}},f={render:function(){var e=this,r=e.$createElement,t=e._self._c||r;return t("Drawer",{attrs:{title:"全部组件",placement:"left",closable:!0,mask:!1,scrollable:""},model:{value:e.showComponentDrawer,callback:function(r){e.showComponentDrawer=r},expression:"showComponentDrawer"}},[t("Tabs",{attrs:{value:"tabPane1"}},[t("TabPane",{attrs:{label:"组件",name:"tabPane1"}},[t("div",{staticClass:"searchTypeBox"},[t("span",{class:{searchTypeChoosed:!e.showAllNodeGroup,searchType:e.showAllNodeGroup},on:{click:function(r){e.showAllNodeGroup=!1}}},[e._v("分类")]),e._v(" "),t("span",{class:{searchTypeChoosed:e.showAllNodeGroup,searchType:!e.showAllNodeGroup},on:{click:function(r){e.showAllNodeGroup=!0}}},[e._v("全部")])]),e._v(" "),e.showAllNodeGroup?t("div",e._l(e.nodeMetas,function(r){return t("span",{attrs:{draggable:"true"},on:{dragstart:function(t){e.dragstart(t,"work_type__"+r.name)}}},[e.showComponent(r.name)?t("Tag",{attrs:{title:"描述信息待完善"}},[e._v(e._s(r.name))]):e._e()],1)})):t("Collapse",e._l(e.nodeGroups,function(r){return t("Panel",{attrs:{name:r}},[e._v("\n          "+e._s(r)+"  组\n          "),t("div",{attrs:{slot:"content"},slot:"content"},e._l(e.nodeMetas,function(s){return s.group===r?t("div",{attrs:{draggable:"true"},on:{dragstart:function(r){e.dragstart(r,"work_type__"+s.name)}}},[e.showComponent(s.name)?t("Tag",{attrs:{title:"描述信息待完善"}},[e._v(e._s(s.name))]):e._e()],1):e._e()}))])}))],1),e._v(" "),t("TabPane",{attrs:{label:"流程",name:"tabPane2"}},[t("div",{staticClass:"searchTypeBox"},[t("span",{class:{searchTypeChoosed:!e.showAllModuleName,searchType:e.showAllModuleName},on:{click:function(r){e.showAllModuleName=!1}}},[e._v("分类")]),e._v(" "),t("span",{class:{searchTypeChoosed:e.showAllModuleName,searchType:!e.showAllModuleName},on:{click:function(r){e.showAllModuleName=!0}}},[e._v("全部")])]),e._v(" "),e.showAllModuleName?t("div",e._l(e.works,function(r){return t("div",{attrs:{draggable:"true"},on:{dragstart:function(t){e.dragstart(t,"work_name__"+r.work_name)}}},[t("Tag",[t("span",{on:{click:function(t){e.$router.push({path:"/iwork/workList",query:{work_name:r.work_name}})}}},[e._v(e._s(r.work_name))])])],1)})):t("Collapse",e._l(e.moduleNames,function(r){return t("Panel",{attrs:{name:r}},[e._v("\n          "+e._s(r)+"  模块\n          "),t("div",{attrs:{slot:"content"},slot:"content"},e._l(e.works,function(s){return s.module_name===r?t("div",{attrs:{draggable:"true"},on:{dragstart:function(r){e.dragstart(r,"work_name__"+s.work_name)}}},[t("Tag",[t("span",{on:{click:function(r){e.$router.push({path:"/iwork/workList",query:{work_name:s.work_name}})}}},[e._v(e._s(s.work_name))])])],1):e._e()}))])}))],1)],1)],1)},staticRenderFns:[]};var h=t("VU/8")(m,f,!1,function(e){t("AVUi")},"data-v-ccbc0084",null);r.a=h.exports}});
//# sourceMappingURL=2.4df9885ff82e5690d8dc.js.map