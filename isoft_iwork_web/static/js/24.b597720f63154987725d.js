webpackJsonp([24],{gPTX:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={name:"IKeyValueForm",props:{formKeyLabel:{type:String,default:"formKeyLabel"},formValueLabel:{type:String,default:"formValueLabel"},formKeyPlaceholder:{type:String,default:"请输入 formKey"},formValuePlaceholder:{type:String,default:"请输入 formValue"},formkeyValidator:{type:Function},formvalueValidator:{type:Function}},data:function(){return{formValidate:{formmode:null,formkey:"",formvalue:""},ruleValidate:{formkey:[{validator:this.formkeyValidator,trigger:"blur"},{required:!0,message:"formkey 不能为空!",trigger:"blur"}],formvalue:[{validator:this.formvalueValidator,trigger:"blur"},{required:!0,message:"formvalue 不能为空!",trigger:"blur"}]}}},methods:{handleSubmit:function(e){var t=this;this.$refs[e].validate(function(e){e&&t.$emit("handleSubmit",t.formValidate.formmode,t.formValidate.formkey,t.formValidate.formvalue)})},handleSubmitSuccess:function(e){this.formValidate.formmode=null,this.handleReset("formValidate"),this.$Message.success(e)},handleSubmitError:function(e){this.$Message.error(e)},initFormData:function(e,t,r){this.formValidate.formmode=e,this.formValidate.formkey=t,this.formValidate.formvalue=r},handleReset:function(e){this.$refs[e].resetFields()}}},o={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("Form",{ref:"formValidate",attrs:{model:e.formValidate,rules:e.ruleValidate,"label-width":150}},[r("FormItem",{attrs:{label:e.formKeyLabel,prop:"formkey"}},[r("Input",{attrs:{placeholder:e.formKeyPlaceholder},model:{value:e.formValidate.formkey,callback:function(t){e.$set(e.formValidate,"formkey","string"==typeof t?t.trim():t)},expression:"formValidate.formkey"}})],1),e._v(" "),r("FormItem",{attrs:{label:e.formValueLabel,prop:"formvalue"}},[r("Input",{attrs:{type:"textarea",rows:4,placeholder:e.formValuePlaceholder},model:{value:e.formValidate.formvalue,callback:function(t){e.$set(e.formValidate,"formvalue","string"==typeof t?t.trim():t)},expression:"formValidate.formvalue"}})],1),e._v(" "),e._t("extra"),e._v(" "),r("FormItem",[r("Button",{staticStyle:{"margin-right":"6px"},attrs:{type:"success"},on:{click:function(t){e.handleSubmit("formValidate")}}},[e._v("提交")]),e._v(" "),r("Button",{staticStyle:{"margin-right":"6px"},attrs:{type:"warning"},on:{click:function(t){e.handleReset("formValidate")}}},[e._v("重置")])],1)],2)},staticRenderFns:[]};var l=r("VU/8")(a,o,!1,function(e){r("z2Zl")},"data-v-f91914b2",null);t.default=l.exports},z2Zl:function(e,t){}});
//# sourceMappingURL=24.b597720f63154987725d.js.map