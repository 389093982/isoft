<template>
  <div style="border: 1px solid #eee;margin: 50px;padding: 50px;">
    <div style="text-align: right;"><span>常用装饰位</span><span>新增装饰位 <Icon type="md-add"/></span></div>

    <div>
      <Row :gutter="10" style="text-align: center;">
        <Col span="8">图片/视频</Col>
        <Col span="8">文本描述</Col>
        <Col span="8">链接地址</Col>
      </Row>
      <Form ref="formDynamic" :model="formDynamic">
        <Row :gutter="10" v-for="(item, index) in formDynamic.items" v-if="item.status" :key="index">
          <Col span="8">
            <FormItem :prop="'items.' + index + '.img_path'"
                      :rules="{required: true, message: '图片/视频[' + item.index +']不能为空！', trigger: 'blur'}">
              <Input v-model.trim="item.img_path" placeholder="请选择图片/视频"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem :prop="'items.' + index + '.decorate_text'"
                      :rules="{required: true, message: '文本描述[' + item.index +']不能为空！', trigger: 'blur'}">
              <Input v-model.trim="item.decorate_text" placeholder="请输入文本描述"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem :prop="'items.' + index + '.link_href'"
                      :rules="{required: true, message: '链接地址[' + item.index +']不能为空！', trigger: 'blur'}">
              <Input v-model.trim="item.link_href" placeholder="请输入链接地址"></Input>
            </FormItem>

            <div style="position: absolute;top: 3px;right: -30px;">
              <Icon type="md-close" @click="handleRemove(index)"/>
              <Icon type="md-add" @click="handleAdd"/>
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
    data() {
      return {
        index: 1,   // 动态表单有多少项
        formDynamic: {
          items: [
            {
              id: 0,
              img_path: '',
              decorate_text: '',
              link_href: '',
              index: 1,
              status: 1
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
          img_path: '',
          decorate_text: '',
          link_href: '',
          index: this.index,
          status: 1
        });
      },
      handleRemove(index) {
        this.formDynamic.items[index].status = 0;
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            alert(this.formDynamic.items.length);
          }
        })
      },
      refreshDecorate: async function () {
        let params = {
          'referer_type': 'referer_type_test',
          'referer_id': 1,
          'decorate_name': 'decorate_name',
          'decorate_icon': 'decorate_icon',
        }
        const result = await EditDecorate(params);
        if (result.status === "SUCCESS") {
          alert(JSON.stringify(result));
        }
      },
      refreshLoadDecorateData: async function () {
        let params = {
          'referer_type': 'referer_type_test',
          'referer_id': 1,
        }
        const result = await LoadDecorateData(params);
        if (result.status === "SUCCESS") {
          alert(JSON.stringify(result));
        }
      },
      refreshDecorateItem: async function () {
        let params = {
          'decorate_id': 1,
          'media_path': 'media_path',
          'decorate_text': 'decorate_text',
          'link_href': 'link_href',
        }
        const result = await EditDecorateItem(params);
        if (result.status === "SUCCESS") {
          alert(JSON.stringify(result));
        }
      }
    },
    mounted() {
      this.refreshDecorate();
      this.refreshLoadDecorateData();
      this.refreshDecorateItem();
    }
  }
</script>

<style scoped>

</style>
