<template>
  <div>
    <div class="isoft_bg_white isoft_pd10 clear">
      <h4>通过设置属性 long 可将按钮宽度设置为 100%，常用于弹窗内操作按钮</h4>
      <a style="float:right;right: 50px;bottom: 50px;"
         @click="$router.push({path:'/communicate/ask_expert'})">返回问题列表</a>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd10">
      <ul>
        <li v-for="(as, index) in answers"
            style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
          <h4 style="color: red;">专家回答({{index+1}} 楼)</h4>{{as.answer}}
          <p v-if="as.file_path">{{as.file_path}}</p>
          <Row>
            <Col span="6"></Col>
            <Col span="6">
              <span class="isoft_font12">回答人：{{as.created_time}}</span>
            </Col>
            <Col span="6" class="isoft_font12">
              <span class="isoft_font12">提出时间:<Time :time="as.created_time" :interval="1"/></span>
            </Col>
            <Col span="6" class="isoft_font12">好评(100) 差评(50)</Col>
          </Row>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: "AskAnswer",
    data() {
      return {
        answers: [
          {
            answer: '通过设置属性 long 可将按钮宽度设置为 100%，常用于弹窗内操作按钮',
            created_time: '2010-11-11 08:08:08',
            created_by: '389093982@qq.com',
          },
          {
            answer: '通过设置属性 long 可将按钮宽度设置为 100%，常用于弹窗内操作按钮',
            created_time: '2010-11-11 08:08:08',
            created_by: '389093982@qq.com',
          },
          {
            answer: '通过设置属性 long 可将按钮宽度设置为 100%，常用于弹窗内操作按钮',
            file_path: 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.txt',
            created_time: '2010-11-11 08:08:08',
            created_by: '389093982@qq.com',
          }
        ]
      }
    }
  }
</script>

<style scoped>

</style>
