webpackJsonp([20],{HZGD:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("Xxa5"),l=n.n(a),r=n("exGp"),i=n.n(r),m=n("gyMJ"),s={name:"EditPlacement",data:function(){return{showRemark:!0,formInline:{placement_name:"",placement_desc:"",placement_label:"",element_limit:"-1",placement_type:""},ruleInline:{placement_name:[{required:!0,validator:function(e,t,n){""===t?n(new Error("占位符名称不能为空!")):t.indexOf("placement_")<0?n(new Error("占位符名称必须以 placement_ 开头!")):n()},trigger:"blur"}],placement_desc:[{required:!0,message:"Please fill in the placement_desc.",trigger:"blur"}],placement_label:[{required:!0,message:"Please fill in the placement_label.",trigger:"blur"}]}}},methods:{handleSubmit:function(){var e,t=this;this.$refs.formInline.validate((e=i()(l.a.mark(function e(n){var a,r;return l.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:if(!n){e.next=8;break}return a=void 0==t.$route.query.id?0:t.$route.query.id,e.next=4,Object(m.w)(a,t.formInline.placement_name,t.formInline.placement_desc,t.formInline.placement_label,t.formInline.element_limit,t.formInline.placement_type);case 4:"SUCCESS"==(r=e.sent).status?t.$Message.success("提交成功!"):t.$Message.error(r.errorMsg),e.next=9;break;case 8:t.$Message.error("校验不通过!");case 9:case"end":return e.stop()}},e,t)})),function(t){return e.apply(this,arguments)}))},handleGoBack:function(){this.$router.push({path:"/iwork/placementList"})},refreshPlacement:function(){var e=i()(l.a.mark(function e(t){var n,a;return l.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(m._8)(t);case 2:"SUCCESS"==(n=e.sent).status&&(a=n.placement,this.formInline.placement_name=a.placement_name,this.formInline.placement_desc=a.placement_desc,this.formInline.placement_label=a.placement_label,this.formInline.placement_type=a.placement_type,this.formInline.element_limit=a.element_limit);case 4:case"end":return e.stop()}},e,this)}));return function(t){return e.apply(this,arguments)}}()},mounted:function(){void 0!=this.$route.query.id&&this.$route.query.id>0&&this.refreshPlacement(this.$route.query.id)}},o={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",[n("Button",{attrs:{type:"success",size:"small"},on:{click:function(t){e.showRemark=!e.showRemark}}},[e._v("显示备注")]),e._v(" "),n("Form",{ref:"formInline",attrs:{model:e.formInline,rules:e.ruleInline,"label-width":100}},[n("Row",[n("Col",{attrs:{span:"12"}},[n("div",{directives:[{name:"show",rawName:"v-show",value:e.showRemark,expression:"showRemark"}],staticClass:"remark"},[e._v("备注：placement_name:仅做命名区分,不用于界面展示")]),e._v(" "),n("FormItem",{attrs:{prop:"placement_name",label:"占位符名称"}},[n("Input",{attrs:{type:"text",placeholder:"placement_name"},model:{value:e.formInline.placement_name,callback:function(t){e.$set(e.formInline,"placement_name",t)},expression:"formInline.placement_name"}})],1),e._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:e.showRemark,expression:"showRemark"}],staticClass:"remark"},[e._v("备注：placement_label:界面块元素顶级名称,用于界面展示")]),e._v(" "),n("FormItem",{attrs:{prop:"placement_label",label:"页面显示名称"}},[n("Input",{attrs:{type:"text",placeholder:"placement_label"},model:{value:e.formInline.placement_label,callback:function(t){e.$set(e.formInline,"placement_label",t)},expression:"formInline.placement_label"}})],1),e._v(" "),n("FormItem",{attrs:{prop:"placement_type",label:"占位符类型"}},[n("Select",{attrs:{placeholder:"placement_type"},model:{value:e.formInline.placement_type,callback:function(t){e.$set(e.formInline,"placement_type","string"==typeof t?t.trim():t)},expression:"formInline.placement_type"}},[n("Option",{attrs:{value:"all"}},[e._v("默认类型 -- all")]),e._v(" "),n("Option",{attrs:{value:"text_link"}},[e._v("文字链接 -- text_link")]),e._v(" "),n("Option",{attrs:{value:"text_event"}},[e._v("文字事件 -- text_event")]),e._v(" "),n("Option",{attrs:{value:"img_text_link"}},[e._v("图文链接 -- img_text_link")]),e._v(" "),n("Option",{attrs:{value:"img_text_event"}},[e._v("图文事件 -- img_text_event")])],1)],1)],1),e._v(" "),n("Col",{attrs:{span:"12"}},[n("div",{directives:[{name:"show",rawName:"v-show",value:e.showRemark,expression:"showRemark"}],staticClass:"remark"},[e._v("备注：placement_desc:仅做说明,不用于界面展示")]),e._v(" "),n("FormItem",{attrs:{prop:"placement_desc",label:"占位符描述"}},[n("Input",{attrs:{type:"text",placeholder:"placement_desc"},model:{value:e.formInline.placement_desc,callback:function(t){e.$set(e.formInline,"placement_desc",t)},expression:"formInline.placement_desc"}})],1),e._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:e.showRemark,expression:"showRemark"}],staticClass:"remark"},[e._v("备注：element_limit:占位符元素最大显示数量,小于1默认1000")]),e._v(" "),n("FormItem",{attrs:{prop:"element_limit",label:"元素显示数量"}},[n("Input",{attrs:{type:"number",placeholder:"element_limit"},model:{value:e.formInline.element_limit,callback:function(t){e.$set(e.formInline,"element_limit",t)},expression:"formInline.element_limit"}})],1),e._v(" "),n("FormItem",[n("Button",{staticStyle:{"margin-right":"6px"},attrs:{type:"success"},on:{click:e.handleSubmit}},[e._v("提交")]),e._v(" "),n("Button",{staticStyle:{"margin-right":"6px"},attrs:{type:"warning"},on:{click:e.handleGoBack}},[e._v("返回")])],1)],1)],1)],1)],1)},staticRenderFns:[]};var c=n("VU/8")(s,o,!1,function(e){n("sojl")},"data-v-0594c1fe",null);t.default=c.exports},sojl:function(e,t){}});
//# sourceMappingURL=20.20fec2c89f4ba4f360e1.js.map