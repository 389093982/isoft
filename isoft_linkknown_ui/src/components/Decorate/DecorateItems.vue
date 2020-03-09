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
          <FormItem :prop="'items.' + index + '.media_path'"
                    :rules="{required: true, message: '图片/视频[' + index +']不能为空！', trigger: 'blur'}">
            <Input v-model.trim="item.media_path" placeholder="请选择图片/视频"></Input>
          </FormItem>
        </Col>
        <Col span="9">
          <FormItem :prop="'items.' + index + '.decorate_text'"
                    :rules="{required: true, message: '文本描述[' + index +']不能为空！', trigger: 'blur'}">
            <Input type="textarea" :rows="3" v-model.trim="item.decorate_text" placeholder="请输入文本描述"></Input>
          </FormItem>
        </Col>
        <Col span="9">
          <FormItem :prop="'items.' + index + '.link_href'"
                    :rules="{required: true, message: '链接地址[' + index +']不能为空！', trigger: 'blur'}">
            <Input v-model.trim="item.link_href" placeholder="请输入链接地址"></Input>
          </FormItem>

          <div style="position: absolute;top: 3px;right: -60px;cursor: pointer;">
            <Icon type="md-close" @click="handleRemove(index)" title="删除"/>
            <Icon type="md-add" @click="handleAdd" title="增加"/>
            <Icon type="md-checkmark" @click="handleSave(index)" title="保存"/>
          </div>
        </Col>
      </Row>
    </Form>

    <div v-else @click="handleAdd" class="isoft_button_green1" style="width: 400px;margin: 0 auto;">您还没有装修项，立马创建</div>
  </div>
</template>

<script>
  import {EditDecorateItem, LoadDecorateData} from "../../api"

  export default {
    name: "DecorateItems",
    props: {
      decorate: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        index: 1,   // 动态表单有多少项
        formDynamic: {
          items: [
            {
              id: 0,
              media_path: '',
              decorate_text: '',
              link_href: '',
              index: 1,
            }
          ]
        },
      }
    },
    methods: {
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
      refreshLoadDecorateData: async function () {
        const result = await LoadDecorateData({
          decorate_id: this.decorate.id,
          referer_type: this.decorate.referer_type,
          referer_id: this.decorate.referer_id
        });
        if (result.status === "SUCCESS") {
          this.formDynamic.items = result.decorate_items;
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
          this.refreshLoadDecorateData();
        } else {
          this.$Message.error(result.errorMsg);
        }
      }
    },
    mounted() {
      if (this.decorate) {
        this.refreshLoadDecorateData();
      }
    },
    watch: {
      decorate: function () {
        this.refreshLoadDecorateData();
      }
    }
  }
</script>

<style scoped>

</style>
