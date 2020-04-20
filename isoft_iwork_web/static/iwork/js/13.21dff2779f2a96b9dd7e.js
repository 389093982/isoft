webpackJsonp([13],{GOAI:function(e,t){},l3nS:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=r("Xxa5"),s=r.n(a),n=r("exGp"),i=r.n(n),o=r("gyMJ"),c={name:"MigrateList",data:function(){var e=this;return{cysqls:[{label:"互换两列主键 id",sql:"UPDATE course_video a JOIN course_video b ON(a.id = ? AND b.id = ?) OR (a.id = ? AND b.id = ?) SET a.video_name = b.video_name, b.video_name = a.video_name, a.first_play = b.first_play, b.first_play = a.first_play, a.second_play = b.second_play, b.second_play = a.second_play;"},{label:"删除列",sql:"alter table course_video drop column video_number;"},{label:"添加列",sql:"alter table user add column nick_name varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '';alter table blog_article add column to_top int(11) NOT NULL DEFAULT -1;"},{label:"创建表",sql:"CREATE TABLE `verify_code`( `user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '用户名', `verify_code` INT(11) NOT NULL DEFAULT -1 COMMENT '验证码', verify_code_expired DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '验证码过期时间', PRIMARY KEY (`user_name`)) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;"},{label:"insertOrUpdate",sql:"INSERT INTO test VALUES (1,'b4','c4') ON DUPLICATE KEY UPDATE b=VALUES(b),c=VALUES(c);"},{label:"获取表自增 id",sql:"SELECT AUTO_INCREMENT FROM information_schema.tables WHERE  table_schema = 'isoft_linkknown' AND table_name='book_catalog';"}],tabVal:"lst",logs:[],timer:"",currentResourceName:"",resources:[],migrates:[],current_page:1,total:0,offset:10,columns1:[{title:"id",key:"id",width:100},{title:"migrate_name",key:"migrate_name",width:350},{title:"effective",key:"effective",width:100,render:function(t,r){return t("span",{style:{color:e.migrates[r.index].effective?"blue":"grey"}},e.migrates[r.index].effective?"生效":"失效")}},{title:"migrate_hash",key:"migrate_hash",width:350},{title:"操作",key:"operate",width:180,fixed:"right",render:function(t,r){return t("div",[t("Button",{props:{type:"success",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.editMigrate(e.migrates[r.index].id)}}},"编辑"),t("Button",{props:{type:"info",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.toggleMigrateEffective(e.migrates[r.index].id)}}},"生效/失效")])}}]}},methods:{refreshMigrateList:function(){var e=i()(s.a.mark(function e(){var t;return s.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(o.A)(this.offset,this.current_page);case 2:t=e.sent,this.migrates=t.migrates,this.resources=t.resources,this.total=t.paginator.totalcount;case 6:case"end":return e.stop()}},e,this)}));return function(){return e.apply(this,arguments)}}(),handleChange:function(e){this.current_page=e,this.refreshMigrateList()},handlePageSizeChange:function(e){this.offset=e,this.refreshMigrateList()},editMigrate:function(e){var t;t=void 0!=e&&null!=e?this.$router.resolve({path:"/iwork/editMigrate",query:{id:e}}):this.$router.resolve({path:"/iwork/editMigrate"}),window.open(t.href,"_blank")},refreshMigrateLogs:function(){var e=i()(s.a.mark(function e(t){var r;return s.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(o.I)(t);case 2:"SUCCESS"==(r=e.sent).status&&(this.logs=r.logs,1==r.over&&clearInterval(this.timer));case 4:case"end":return e.stop()}},e,this)}));return function(t){return e.apply(this,arguments)}}(),executeMigrate:function(){var e=i()(s.a.mark(function e(t){var r,a;return s.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return this.tabVal="log",e.next=3,Object(o.y)(this.currentResourceName,t);case 3:"SUCCESS"==(r=e.sent).status&&((a=this).timer=setInterval(function(){a.refreshMigrateLogs(r.trackingId)},1e3));case 5:case"end":return e.stop()}},e,this)}));return function(t){return e.apply(this,arguments)}}(),toggleMigrateEffective:function(){var e=i()(s.a.mark(function e(t){return s.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(o._10)(t);case 2:"SUCCESS"==e.sent.status?(this.$Message.success("操作成功!"),this.refreshMigrateList()):this.$Message.error("操作失败!");case 4:case"end":return e.stop()}},e,this)}));return function(t){return e.apply(this,arguments)}}(),renderBr:function(e){return e.replace(/\n/g,"<br>")}},mounted:function(){this.refreshMigrateList()}},l={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("Row",{staticStyle:{"margin-bottom":"10px"}},[r("Col",{attrs:{span:"10"}},[r("Button",{staticStyle:{"margin-bottom":"6px"},attrs:{type:"success",size:"small"},on:{click:function(t){e.editMigrate(null)}}},[e._v("新建迁移")])],1),e._v(" "),r("Col",{attrs:{span:"14"}},[r("Select",{staticStyle:{width:"300px"},model:{value:e.currentResourceName,callback:function(t){e.currentResourceName=t},expression:"currentResourceName"}},e._l(e.resources,function(t){return r("Option",{attrs:{value:t.resource_name}},[e._v("\n          "+e._s(t.resource_name)+" - "+e._s(t.resource_dsn)+"\n        ")])})),e._v(" "),r("Button",{staticStyle:{"margin-bottom":"6px"},attrs:{type:"success",size:"small"},on:{click:function(t){e.executeMigrate(!1)}}},[e._v("执行迁移")]),e._v(" "),r("Button",{staticStyle:{"margin-bottom":"6px"},attrs:{type:"success",size:"small"},on:{click:function(t){e.executeMigrate(!0)}}},[e._v("清理DB并执行迁移")])],1)],1),e._v(" "),r("Tabs",{attrs:{value:e.tabVal}},[r("TabPane",{attrs:{label:"列表",name:"lst"}},[r("Table",{attrs:{border:"",columns:e.columns1,data:e.migrates,size:"small"}})],1),e._v(" "),r("TabPane",{attrs:{label:"预览",name:"views"}},[r("Scroll",{attrs:{height:"450"}},e._l(e.migrates,function(t){return r("div",{staticStyle:{"border-bottom":"1px solid green",padding:"10px"}},[r("Row",[r("Col",{attrs:{span:"1"}},[r("span",{staticStyle:{color:"red"}},[e._v(e._s(t.id))])]),e._v(" "),r("Col",{attrs:{span:"23"}},[r("span",{domProps:{innerHTML:e._s(e.renderBr(t.migrate_sql))}})])],1)],1)}))],1),e._v(" "),r("TabPane",{attrs:{label:"常用语句",name:"cy"}},e._l(e.cysqls,function(t,a){return r("div",[r("p",{staticStyle:{color:"red"}},[e._v(e._s(t.label))]),e._v(" "),r("p",[e._v(e._s(t.sql))])])})),e._v(" "),r("TabPane",{attrs:{label:"执行日志",name:"log"}},[r("Scroll",{attrs:{height:"450"}},e._l(e.logs,function(t){return r("p",{style:{color:t.status?"grey":"red"}},[e._v(e._s(t.tracking_detail))])}))],1)],1),e._v(" "),r("Page",{attrs:{total:e.total,"page-size":e.offset,"show-total":"","show-sizer":"",styles:{"text-align":"center","margin-top":"10px"}},on:{"on-change":e.handleChange,"on-page-size-change":e.handlePageSizeChange}})],1)},staticRenderFns:[]};var u=r("VU/8")(c,l,!1,function(e){r("GOAI")},"data-v-6899dcfc",null);t.default=u.exports}});
//# sourceMappingURL=13.21dff2779f2a96b9dd7e.js.map