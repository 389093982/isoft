<template>
  <div style="margin: 0 15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">

    <div style="display: flex;padding: 50px;">
      <div style="width: 70%;padding: 0 50px 0 0;">
        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
          <FormItem label="产品类型" prop="good_type">
            <Input v-model.trim="formValidate.good_type" placeholder="请输入产品类型,示例：服务类|广告服务|广告制作"/>
          </FormItem>
          <FormItem label="产品名称" prop="good_name">
            <Input v-model.trim="formValidate.good_name" placeholder="请输入产品名称"/>
          </FormItem>
          <FormItem label="产品标签" prop="good_tag">
            <Input v-model.trim="formValidate.good_tag" placeholder="请输入产品标签,示例：专业|高水准|精品"/>
          </FormItem>
          <FormItem label="产品描述" prop="good_desc">
            <Input v-model.trim="formValidate.good_desc" type="textarea" :rows="5" placeholder="请输入产品描述"/>
          </FormItem>
          <FormItem label="产品金额" prop="good_price">
            <Input v-model.trim="formValidate.good_price" placeholder="请选择产品金额"/>
          </FormItem>
          <FormItem label="卖家姓名" prop="good_seller">
            <Input v-model.trim="formValidate.good_seller" :readonly="true"/>
          </FormItem>
          <FormItem label="卖家联系方式" prop="seller_contact">
            <Input v-model.trim="formValidate.seller_contact" placeholder="请输入卖家联系方式"/>
          </FormItem>

          <FormItem label="产品图片" prop="good_images">
            <Scroll height="160">
              <span style="height: 140px;">
                <img v-for="good_image in formValidate.good_images" :src="good_image"
                     width="120px" height="90px" style="margin: 5px;"/>
              </span>
            </Scroll>
            <IFileUpload ref="fileUpload" :auto-hide-modal="true" btn-size="small"
                         @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传图片"/>
          </FormItem>

          <FormItem>
            <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
          </FormItem>
        </Form>
      </div>

      <div style="width: 30%;">
        <div>
          <span>1</span>
          <span class="hovered hvr-grow hoverLinkColor isoft_point_cursor">发布服务或产品</span>
        </div>
        <div>
          <span>2</span>
          <span class="hovered hvr-grow hoverLinkColor isoft_point_cursor">装饰店铺</span>
        </div>
        <div>
          <span>3</span>
          <span class="hovered hvr-grow hoverLinkColor isoft_point_cursor">完善认证资格</span>
        </div>

        <div>
          <span>4</span>
          <span class="hovered hvr-grow hoverLinkColor isoft_point_cursor">装饰店铺</span>
        </div>
      </div>
    </div>

    <Decorate/>
  </div>
</template>

<script>
  import IFileUpload from "../Common/file/IFileUpload";
  import {fileUploadUrl, GetGoodDetail, GoodEdit} from "../../api"
  import {copyObj, GetLoginUserName} from "../../tools"
  import Decorate from "../Decorate/Decorate";

  export default {
    name: "BusinessEdit",
    components: {Decorate, IFileUpload},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=good&table_field=good_images",
        formValidate: {
          good_id: -1,
          good_type: '',
          good_name: '',
          good_tag: '',
          good_desc: '',
          good_price: 0,     // 负数表示暂无报价
          good_images: [],
          good_seller: '',
          seller_contact: '',
        },
        ruleValidate: {
          good_type: [
            {required: true, message: '产品类型不能为空', trigger: 'blur'}
          ],
          good_name: [
            {required: true, message: '产品名称不能为空', trigger: 'blur'}
          ],
          good_tag: [
            {required: true, message: '产品标签不能为空', trigger: 'blur'}
          ],
          good_desc: [
            {required: true, message: '产品描述不能为空', trigger: 'blur'}
          ],
          good_price: [
            {required: true, message: '产品价格不能为空', trigger: 'blur'}
          ],
          seller_contact: [
            {required: true, message: '卖家联系方式不能为空', trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      convertObjToArr(obj) {
        if (typeof obj === typeof []) {
          return obj;
        }
        let arr = [];
        for (key in obj) {
          arr.push(obj[key]);
        }
        return arr;
      },
      uploadComplete(result, file) {
        if (result.status === "SUCCESS") {
          let _good_images = this.convertObjToArr(this.formValidate.good_images);
          _good_images.push(result.fileServerPath);
          this.$set(this.formValidate, "good_images", _good_images);
        }
      },
      handleSubmit: function (name) {
        if (this.formValidate.good_desc.length < 50) {
          this.$Message.error('产品描述太短，不能少于 50 个字符!');
          return;
        }
        if (this.formValidate.good_images.length === 0) {
          this.$Message.error('必须上传一张图片!');
          return;
        }
        var _this = this;
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            let params = copyObj(_this.formValidate);
            params.good_images = JSON.stringify(_this.formValidate.good_images);
            const result = await GoodEdit(params);
            if (result.status === "SUCCESS") {
              this.$router.push({path: '/business/list'});
            } else {
              _this.$Message.error('提交失败!');
            }
          } else {
            _this.$Message.error('验证失败!');
          }
        })
      },
      refreshGoodDetail: async function (good_id) {
        const result = await GetGoodDetail(good_id);
        if (result.status === "SUCCESS") {
          this.formValidate = result.good;
          this.formValidate.good_images = JSON.parse(result.good.good_images);
        }
      }
    },
    mounted() {
      this.formValidate.good_seller = GetLoginUserName();
      if (this.$route.query.id > 0) {
        this.refreshGoodDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
