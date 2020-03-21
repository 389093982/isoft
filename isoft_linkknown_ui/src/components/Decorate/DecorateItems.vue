<template>
  <div v-if="decorate">
    <Form ref="formDynamic" :model="formDynamic" v-if="formDynamic.items && formDynamic.items.length > 0">
      <Row :gutter="10" style="text-align: center;">
        <Col span="6">图片/视频</Col>
        <Col span="9">文本描述</Col>
        <Col span="9">链接地址
        </Col>
      </Row>
      <Row :gutter="10" v-for="(item, index) in formDynamic.items" :key="index">
        <Col span="6">
          <img width="220px" height="160px" :src="item.media_path" @error="defImg()"
               class="isoft_point_cursor" title="点击图片换一张" @click="uploadMedia(index)"/>
        </Col>
        <Col span="9">
          <FormItem :prop="'items.' + index + '.decorate_text'"
                    :rules="{required: true, message: '文本描述不能为空！', trigger: 'blur'}">
            <Input type="textarea" :rows="7" v-model.trim="item.decorate_text" placeholder="请输入文本描述"></Input>
          </FormItem>
        </Col>
        <Col span="9">
          <FormItem :prop="'items.' + index + '.link_href'"
                    :rules="{required: true, message: '链接地址不能为空！', trigger: 'blur'}">
            <Input v-model.trim="item.link_href" placeholder="请输入链接地址"></Input>
          </FormItem>

          <div style="position: absolute;top: 3px;right: -60px;cursor: pointer;">
            <Icon type="md-close" @click="handleRemove(index)" title="删除"/>
            <Icon type="md-add" @click="handleAdd" title="增加"/>
            <Icon type="md-checkmark" @click="handleSave(index)" title="保存"/>
          </div>
        </Col>
      </Row>

      <IFileUpload ref="fileUpload" :show-button="false" :auto-hide-modal="true" :multiple="false"
                   :format="['jpg','jpeg','png','gif']" @uploadComplete="uploadComplete" :action="fileUploadUrl"
                   uploadLabel="上传图片/视频"/>
    </Form>

    <span v-else>
      <Spin fix size="large" v-if="isLoading">
        <div class="isoft_loading"></div>
      </Spin>
      <div v-else @click="handleAdd" class="isoft_button_green1" style="width: 400px;margin: 0 auto;">您还没有装修项，立马创建</div>
    </span>
  </div>
</template>

<script>
  import {EditDecorateItem, fileUploadUrl, LoadDecorateItems} from "../../api"
  import IFileUpload from "../Common/file/IFileUpload";
  import {handleSpecial} from "../../tools";

  export default {
    name: "DecorateItems",
    components: {IFileUpload},
    props: {
      decorate: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        isLoading: true,
        fileUploadUrl: fileUploadUrl + "?table_name=decorate_item&table_field=media_path",
        defaultImg: require('../../assets/default.png'),
        fileUploadIndex: -1,
        formDynamic: {
          items: [
            // {
            //   id: 0,
            //   media_path: '',
            //   decorate_text: '',
            //   link_href: '',
            // }
          ]
        },
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      uploadMedia: function (index) {
        this.fileUploadIndex = index;
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: function (result) {
        if (result.status === "SUCCESS") {
          this.formDynamic.items[this.fileUploadIndex].media_path = handleSpecial(result.fileServerPath);
        }
      },
      handleRemove(index) {
        this.formDynamic.items.splice(index, 1);
      },
      handleAdd() {
        this.formDynamic.items.push({
          id: 0,
          media_path: '',
          decorate_text: '',
          link_href: '',
        });
      },
      refreshLoadDecorateItems: async function () {
        try {
          this.isLoading = true;
          const result = await LoadDecorateItems({
            decorate_name: this.decorate.decorate_name,
            referer_type: this.decorate.referer_type,
            referer_id: this.decorate.referer_id
          });
          if (result.status === "SUCCESS") {
            this.formDynamic.items = result.decorate_items ? result.decorate_items : [];
          }
        } finally {
          this.isLoading = false;
        }
      },
      handleSave: async function (index) {
        let params = {
          'id': this.formDynamic.items[index].id,
          'decorate_id': this.decorate.id,
          'media_path': this.formDynamic.items[index].media_path,
          'decorate_text': this.formDynamic.items[index].decorate_text,
          'link_href': this.formDynamic.items[index].link_href,
        };
        const result = await EditDecorateItem(params);
        if (result.status === "SUCCESS") {
          this.$Message.success("保存成功！");
          this.refreshLoadDecorateItems();
        } else {
          this.$Message.error(result.errorMsg);
        }
      }
    },
    mounted() {
      if (this.decorate) {
        this.refreshLoadDecorateItems();
      }
    },
    watch: {
      decorate: function () {
        this.refreshLoadDecorateItems();
      }
    }
  }
</script>

<style scoped>

</style>
