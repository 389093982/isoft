<template>
  <div class="isoft_bg_white isoft_pd10 isoft_top5">
    <Row :gutter="10">
      <Col span="6">
        <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
             @click="$router.push({path:'/ilearning/index'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/course.jpg" style="width: 52px;height: 52px;"/>
            </Col>
            <Col span="16">
              <h4>精品课程</h4>
              <p>好评如潮好评如潮</p>
            </Col>
          </Row>
        </div>
      </Col>
      <Col span="6">
        <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
             @click="$router.push({path:'/iblog/blog_list'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/blog.jpg" style="width: 52px;height: 52px;"/>
            </Col>
            <Col span="16">
              <h4>话题博客</h4>
              <p>好评如潮好评如潮</p>
            </Col>
          </Row>
        </div>
      </Col>
      <Col span="6">
        <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
             @click="$router.push({path:'/ibook/book_list'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/book.jpg" style="width: 52px;height: 52px;"/>
            </Col>
            <Col span="16">
              <h4>热门书单</h4>
              <p>好评如潮好评如潮</p>
            </Col>
          </Row>
        </div>
      </Col>
      <Col span="6">
        <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
             @click="$router.push({path:'/site/index'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/more.jpg" style="width: 52px;height: 52px;"/>
            </Col>
            <Col span="16">
              <h4>更多资源</h4>
              <p>查看更多关于链知网的相关资源</p>
            </Col>
          </Row>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
	export default {
		name: "ShowModulars"
	}
</script>

<style scoped>

</style>
