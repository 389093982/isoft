webpackJsonp([25],{nS7l:function(o,t){},pwhd:function(o,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={name:"ISimpleConfirmModal",props:{modalTitle:{type:String,default:"模态框标题"},modalWidth:{type:Number,default:800},footerHide:{type:Boolean,default:!1},modalTop:{type:String,default:"100px"}},data:function(){return{showFormModal:!1}},methods:{showModal:function(){this.showFormModal=!0},hideModal:function(){this.showFormModal=!1},handleSubmit:function(){this.$emit("handleSubmit")}}},l={render:function(){var o=this,t=o.$createElement,e=o._self._c||t;return e("span",[e("Modal",{attrs:{width:o.modalWidth,title:o.modalTitle,transfer:!1,"footer-hide":o.footerHide,"mask-closable":!1,styles:{top:o.modalTop}},on:{"on-ok":o.handleSubmit},model:{value:o.showFormModal,callback:function(t){o.showFormModal=t},expression:"showFormModal"}},[e("div",[o._t("default")],2)])],1)},staticRenderFns:[]};var d=e("VU/8")(a,l,!1,function(o){e("nS7l")},"data-v-2ada6476",null);t.default=d.exports}});
//# sourceMappingURL=25.6c453a226617428ec89d.js.map