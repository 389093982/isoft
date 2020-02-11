<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <p>
        <Button type="success" style="width: 20%;">我要提问</Button>
        <Button type="success" style="width: 20%;">我要提问</Button>
      </p>

      2元 5元 10元 20元 50元 100元提问

      我来回答

      好评率不够，无法提现，已自动转为积分

      热门专家 问答数 回答数 好评数

      我的专家标签
      回答人，回答人专家标签
      设置答案可见性
      非提问者需要花积分查看答案
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd10">
      <Row style="border-bottom: 1px solid #e6e6e6;padding: 20px;height: 62px;">
        <Col span="3" offset="4" style="text-align: center;"><a>暂无回答</a></Col>
        <Col span="3" style="text-align: center;"><a>热门回答</a></Col>
        <Col span="3" style="text-align: center;"><a>全部回答</a></Col>
        <Col span="3" style="text-align: center;">
          <router-link to="/iblog/blog_edit">我要提问</router-link>
        </Col>
        <Col span="3" style="text-align: center;"><a>我的问题</a></Col>
      </Row>

      <ul>
        <li v-for="(qs, index) in questions"
            style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
          <h4>{{qs.question}}</h4>
          <Row>
            <Col span="6">
              <span class="isoft_font12">提出时间:<Time :time="qs.created_time" :interval="1"/></span>
            </Col>
            <Col span="6">
              <span class="isoft_font12">提出人:{{qs.created_by}}</span>
            </Col>
            <Col span="6" class="isoft_font12">好评(100) 差评(50)</Col>
            <Col span="6">
              <span class="isoft_font12"><a @click="$router.push({path:'/communicate/ask_answer'})">查看答案</a></span>
            </Col>
          </Row>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: "AskExpert",
    data() {
      return {
        questions: [
          {
            question: '通过设置属性 long 可将按钮宽度设置为 100%，常用于弹窗内操作按钮',
            created_time: '2008-11-11 08:08:08',
            created_by: '389093982@qq.com',
          },
          {
            question: '使用者也可以直接通过给组件添加 style 来设置更细节的样式，比如定宽。',
            created_time: '2010-11-11 08:08:08',
            created_by: '389093982@qq.com',
          },
          {
            question: '使用者也可以直接通过给组件添加 style 来设置更细节的样式，比如定宽。',
            created_time: '2019-11-11 08:08:08',
            created_by: '389093982@qq.com',
          }
        ]
      }
    }
  }
</script>

<style scoped>

</style>
