<template>
  <div style="margin: 0 15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">

    <div style="display: flex;padding: 50px;">
      <div style="width: 70%;padding: 0 50px 0 0;">
        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">

          <div class="isoft_info_tip isoft_font14 isoft_color_grey3" style="margin-bottom: 10px;">
            商品&nbsp;/&nbsp;服务基本信息
          </div>

          <FormItem label="发布类型" prop="good_type">
            <Select v-model="formValidate.good_type" :disabled="$route.query.id > 0">
              <Option v-for="(goodType, index) in goodTypes" :value="goodType" :key="index">{{goodType}}</Option>
            </Select>
          </FormItem>
          <FormItem label="发布名称" prop="good_name">
            <Input v-model.trim="formValidate.good_name" placeholder="请输入发布名称"/>
          </FormItem>
          <FormItem label="发布描述" prop="good_desc">
            <Input v-model.trim="formValidate.good_desc" type="textarea" :rows="5" placeholder="请输入发布描述"/>
          </FormItem>
          <FormItem v-if="formValidate.good_type === '商品'" label="发布金额" prop="good_price">
            <Input v-model.trim="formValidate.good_price" placeholder="请选择发布金额"/>
          </FormItem>

          <div class="isoft_info_tip isoft_font14 isoft_color_grey3" style="margin: 10px 0;">特色装扮</div>

          <FormItem label="发布标签" prop="good_tag">
            <Input v-model.trim="formValidate.good_tag" placeholder="请输入发布标签,示例：专业|高水准|精品"/>

            <div style="margin: 10px 0;">
              <span class="isoft_tag3" v-for="(tag, index) in parseTag(formValidate.good_tag)">{{tag}}</span>
            </div>
          </FormItem>
          <FormItem label="是否有图" prop="good_images">
            <RadioGroup v-model="existImgs">
              <Radio label="无图"></Radio>
              <Radio label="有图"></Radio>
            </RadioGroup>
            <span style="color: red;">
              <span v-if="existImgs === '有图'">上传图片越多，越能吸引他人兴趣哦~</span>
              <span v-else>没有图片，太孤单啦~</span>
            </span>
            <span v-if="existImgs === '有图'">
              <Scroll height="160">
                <span style="height: 140px;">
                  <img v-for="good_image in formValidate.good_images" :src="good_image"
                       width="120px" height="90px" style="margin: 5px;"/>
                </span>
              </Scroll>
              <div v-if="formValidate.good_images && formValidate.good_images.length > 0"
                   style="border-top: 1px solid #eee;padding: 10px;margin-top: 10px;color: red;">大部分用户都上传三张以上图片哦，继续加油吧！</div>

              <IFileUpload ref="fileUpload" :auto-hide-modal="true" btn-size="small"
                           @uploadComplete="uploadComplete" :action="fileUploadUrl"
                           :uploadLabel="formValidate.good_images && formValidate.good_images.length > 0 ? '继续添加' : '上传图片'"/>
            </span>
          </FormItem>
          <FormItem label="亮点介绍" prop="highlights">
            <Input v-model.trim="formValidate.highlights" type="textarea" :rows="5" placeholder="请用心填写亮点介绍，会有更好的推广效果奥"/>
          </FormItem>

          <div class="isoft_info_tip isoft_font14 isoft_color_grey3" style="margin: 10px 0;">更多信息</div>

          <FormItem label="卖家联系方式" prop="seller_contact">
            <Input v-model.trim="formValidate.seller_contact" placeholder="请输入卖家联系方式"/>
          </FormItem>

          <FormItem>
            <div style="width: 50%;margin: 0 auto;">
              <span class="isoft_button_blue" @click="handleSubmit('formValidate')">提交</span>
            </div>
          </FormItem>
        </Form>
      </div>

      <div style="width: 30%;">
        <div>
          <span>1</span>
          <span class="hovered hvr-grow isoft_hover_red2 isoft_point_cursor">发布服务或发布</span>
        </div>
        <div>
          <span>2</span>
          <span class="hovered hvr-grow isoft_hover_red2 isoft_point_cursor">装饰店铺</span>
        </div>
        <div>
          <span>3</span>
          <span class="hovered hvr-grow isoft_hover_red2 isoft_point_cursor">完善认证资格</span>
        </div>

        <div>
          <span>4</span>
          <span class="hovered hvr-grow isoft_hover_red2 isoft_point_cursor">装饰店铺</span>
        </div>

        <div>
          猜你喜欢
        </div>

        <div>
          优惠专栏
        </div>
      </div>
    </div>

    <Decorate v-if="$route.query.id > 0" referer_type="business_decorate" :referer_id="$route.query.id"
              :decorate_names="['商品详情装饰位','商品详情装饰位2222']"/>
  </div>
</template>

<script>
  import IFileUpload from "../Common/file/IFileUpload";
  import {fileUploadUrl, GetGoodDetail, GoodEdit} from "../../api"
  import {checkEmpty, checkNotEmpty, copyObj, GetLoginUserName, strSplit} from "../../tools"
  import Decorate from "../Decorate/Decorate";

  export default {
    name: "BusinessEdit",
    components: {Decorate, IFileUpload},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=good&table_field=good_images",
        goodTypes: this.GLOBAL.goodTypes,
        existImgs: "有图",
        formValidate: {
          good_id: -1,
          good_type: '',
          good_name: '',
          good_tag: '',
          good_desc: '',
          good_price: 0,     // 负数表示暂无报价
          good_images: [],
          highlights: '',
          good_seller: '',
          seller_contact: '',
        },
        ruleValidate: {
          good_type: [
            {required: true, message: '发布类型不能为空', trigger: 'blur'}
          ],
          good_name: [
            {required: true, message: '发布名称不能为空', trigger: 'blur'}
          ],
          good_tag: [
            {required: true, message: '发布标签不能为空', trigger: 'blur'}
          ],
          good_desc: [
            {required: true, message: '发布描述不能为空', trigger: 'blur'}
          ],
          good_price: [
            {required: true, message: '发布价格不能为空', trigger: 'blur'}
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
          this.$Message.error('发布描述太短，不能少于 50 个字符!');
          return;
        }
        var _this = this;
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            let params = copyObj(_this.formValidate);
            params.good_images = _this.formValidate.good_images && _this.formValidate.good_images.length > 0 ?
              JSON.stringify(_this.formValidate.good_images) : '';
            const result = await GoodEdit(params);
            if (result.status === "SUCCESS") {
              this.$router.push({path: '/business/businessList'});
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
          this.formValidate.good_images = checkNotEmpty(result.good.good_images) ? JSON.parse(result.good.good_images) : [];
        }
      },
      parseTag: function (good_tag) {
        return strSplit(good_tag, "|").filter(tag => !checkEmpty(tag));
      },
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
