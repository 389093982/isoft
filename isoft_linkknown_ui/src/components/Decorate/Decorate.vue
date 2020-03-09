<template>
  <div style="border: 1px solid #eee;margin: 50px;padding: 50px;">
    <div v-if="decorate">{{decorate.decorate_name}}</div>
    <div v-else style="text-align: right;">
      <span class="isoft_hover_red" @click="editDecorate"><Icon type="md-add"/>暂无装修,快速新建</span>
    </div>

    <div v-if="decorate">
      <Row :gutter="10" style="text-align: center;">
        <Col span="8">图片/视频</Col>
        <Col span="8">文本描述</Col>
        <Col span="8">链接地址
          <Icon type="md-add" @click="handleAdd"/>
        </Col>
      </Row>
      <Form ref="formDynamic" :model="formDynamic">
        <Row :gutter="10" v-for="(item, index) in formDynamic.items" :key="index">
          <Col span="8">
            <FormItem :prop="'items.' + index + '.media_path'"
                      :rules="{required: true, message: '图片/视频[' + index +']不能为空！', trigger: 'blur'}">
              <Input v-model.trim="item.media_path" placeholder="请选择图片/视频"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem :prop="'items.' + index + '.decorate_text'"
                      :rules="{required: true, message: '文本描述[' + index +']不能为空！', trigger: 'blur'}">
              <Input v-model.trim="item.decorate_text" placeholder="请输入文本描述"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem :prop="'items.' + index + '.link_href'"
                      :rules="{required: true, message: '链接地址[' + index +']不能为空！', trigger: 'blur'}">
              <Input v-model.trim="item.link_href" placeholder="请输入链接地址"></Input>
            </FormItem>

            <div style="position: absolute;top: 3px;right: -30px;">
              <Icon type="md-close" @click="handleRemove(index)"/>
              <Icon type="md-add" @click="handleAdd"/>
              <Icon type="md-checkmark" @click="handleSave(index)"/>
            </div>
          </Col>
        </Row>
        <FormItem>
          <Button type="success" size="small" @click="handleSubmit('formDynamic')" style="margin-right: 6px">提交</Button>
        </FormItem>
      </Form>
    </div>
  </div>
</template>

<script>
  import {EditDecorate, EditDecorateItem, LoadDecorateData} from "../../api"

  export default {
    name: "Decorate",
    props: {
      referer_type: {
        type: String,
        default: '',
      },
      referer_id: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        decorate: null,
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
      handleAdd() {
        this.index++;
        this.formDynamic.items.push({
          id: 0,
          media_path: '',
          decorate_text: '',
          link_href: '',
        });
      },
      handleRemove(index) {
        this.formDynamic.items.splice(index, 1);
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            alert(this.formDynamic.items.length);
          }
        })
      },
      editDecorate: async function () {
        let params = {
          'referer_type': this.referer_type,
          'referer_id': this.referer_id,
          'decorate_name': '默认装修位',
          'decorate_icon': '',
        }
        const result = await EditDecorate(params);
        if (result.status === "SUCCESS") {
          this.refreshLoadDecorateData();
        }
      },
      refreshLoadDecorateData: async function () {
        const result = await LoadDecorateData({referer_type: this.referer_type, referer_id: this.referer_id});
        if (result.status === "SUCCESS") {
          this.decorate = result.decorate;
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
        }
        const result = await EditDecorateItem(params);
        if (result.status === "SUCCESS") {
          this.refreshLoadDecorateData();
        }
      }
    },
    mounted() {
      this.refreshLoadDecorateData();
    }
  }
</script>

<style scoped>

</style>
