webpackJsonp([21],{"7fRg":function(t,e){},"9Bh1":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("Xxa5"),n=a.n(i),r=a("exGp"),s=a.n(r),l=a("I1g2"),o=a("gyMJ"),d=a("GCuT"),c={name:"WorkValidate",components:{ISimpleBtnTriggerModal:l.a},props:{work_id:{type:Number,default:-1}},data:function(){var t=this;return{showDetailDialog:!1,validating:!1,validateDetails:[],tracking_id:"",columns1:[{title:"work_name",key:"work_name"},{title:"work_step_name",key:"work_step_name"},{title:"detail",key:"detail",width:500,render:function(e,a){return e("span",{style:{color:Object(d.a)(t.validateDetails[a.index].work_step_name)?"green":"red",overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"},attrs:{title:t.validateDetails[a.index].detail}},t.validateDetails[a.index].detail)}},{title:"操作",key:"operate",render:function(e,a){return e("div",[e("Button",{props:{type:"success",size:"small"},style:{marginRight:"5px"},on:{click:function(){t.$Modal.confirm({title:"确认",width:1e3,content:a.row.detail})}}},"详情")])}}]}},methods:{validateWork:function(){var t=s()(n.a.mark(function t(){return n.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:if(1!=this.validating){t.next=4;break}this.$Message.error("校验中,请稍后！"),t.next=10;break;case 4:return this.validating=!0,t.next=7,Object(o._12)(this.work_id);case 7:"SUCCESS"==t.sent.status&&this.refreshValidateResult(),this.validating=!1;case 10:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}(),refreshValidateResult:function(){var t=s()(n.a.mark(function t(){var e;return n.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(o.U)(this.work_id);case 2:"SUCCESS"==(e=t.sent).status?(this.validateDetails=e.details,this.tracking_id=e.details[0].tracking_id):this.$Message.error(e.errorMsg);case 4:case"end":return t.stop()}},t,this)}));return function(){return t.apply(this,arguments)}}()}},u={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("ISimpleBtnTriggerModal",{ref:"triggerModal",attrs:{"btn-text":"项目校验","btn-size":"small","modal-title":"查看校验结果","modal-width":1e3,"modal-top":"50px"}},[a("Button",{attrs:{type:"success",size:"small"},on:{click:t.validateWork}},[t._v("校验")]),t._v(" "),a("Button",{attrs:{type:"success",size:"small"},on:{click:t.refreshValidateResult}},[t._v("刷新校验结果")]),t._v(" "),a("div",{staticStyle:{margin:"20px","min-height":"300px"}},[a("p",{staticStyle:{color:"green"}},[t._v("last tracking_id: "+t._s(t.tracking_id))]),t._v(" "),a("Scroll",{attrs:{height:"350"}},[a("Table",{attrs:{border:"",columns:t.columns1,data:t.validateDetails,size:"small"}})],1)],1)],1)},staticRenderFns:[]};var p=a("VU/8")(c,u,!1,function(t){a("7fRg")},"data-v-6794fa71",null);e.default=p.exports},I1g2:function(t,e,a){"use strict";var i={name:"ISimpleBtnTriggerModal",props:{btnText:{type:String,default:"按钮标题"},btnSize:{type:String,default:"default"},btnFolat:{type:String,default:"none"},modalTitle:{type:String,default:"模态框标题"},modalWidth:{type:Number,default:800},modalTop:{type:String,default:"100px"}},data:function(){return{showFormModal:!1}},methods:{hideModal:function(){this.showFormModal=!1},triggerClick:function(){this.showFormModal=!0},btnClick:function(){this.showFormModal=!0,this.$emit("btnClick")}}},n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("span",[a("Button",{style:{float:t.btnFolat},attrs:{type:"success",size:t.btnSize},on:{click:t.btnClick}},[t._v(t._s(t.btnText))]),t._v(" "),a("Modal",{attrs:{width:t.modalWidth,title:t.modalTitle,"footer-hide":!0,transfer:!1,"mask-closable":!1,styles:{top:t.modalTop}},model:{value:t.showFormModal,callback:function(e){t.showFormModal=e},expression:"showFormModal"}},[a("div",[t._t("default")],2)])],1)},staticRenderFns:[]};var r=a("VU/8")(i,n,!1,function(t){a("YceB")},"data-v-9df0c40e",null);e.a=r.exports},YceB:function(t,e){}});
//# sourceMappingURL=21.dcfa9aa5c7080ba56f7a.js.map