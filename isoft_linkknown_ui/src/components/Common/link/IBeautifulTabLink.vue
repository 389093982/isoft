<template>
  <IBeautifulCardPurple :title="pData.title">
    <div slot="content" style="padding: 20px;">
      <Tabs :animated="false">
        <TabPane :label="itemData.itemTitle" v-for="(itemData, index) in pData.pItemData">
          <Row :gutter="10">
            <Col span="6" v-for="item in itemData.items">
              <IBeautifulHoverBox :item="item"/>
            </Col>
          </Row>
        </TabPane>
      </Tabs>
    </div>
  </IBeautifulCardPurple>
</template>

<script>
  import IBeautifulHoverBox from "./IBeautifulHoverBox"
  import IBeautifulCardPurple from "../../Common/card/IBeautifulCardPurple"

  export default {
    name: "IBeautifulTabLink",
    components: {IBeautifulHoverBox, IBeautifulCardPurple},
    data() {
      return {
        current_index: 0,
      }
    },
    props: {
      // 带有默认值的对象
      pData: {
        type: Object,
        // 对象或数组默认值必须从一个工厂函数获取
        default: function () {
          return {
            title: '热门产品',
            pItemData: [
              {
                itemTitle: '热门',
                items: [
                  {item_label: 'iwork 低代码开发框架', item_desc: '快速高效、上手简单的敏捷开发工具', item_link: 'https://www.baidu.com',},
                  {item_label: 'iwork 低代码开发框架', item_desc: '快速高效、上手简单的敏捷开发工具', item_link: 'https://www.baidu.com',},
                  {item_label: 'iwork 低代码开发框架', item_desc: '快速高效、上手简单的敏捷开发工具', item_link: 'https://www.baidu.com',},
                  {item_label: 'iwork 低代码开发框架', item_desc: '快速高效、上手简单的敏捷开发工具', item_link: 'https://www.baidu.com',},
                ],
              },
            ]
          }
        }
      },
    },
    methods: {},
  }
</script>

<style scoped>


</style>
