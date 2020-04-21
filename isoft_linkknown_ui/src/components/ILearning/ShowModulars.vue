<template>
  <div class="isoft_bg_white isoft_pd10 isoft_top5">
    <Row :gutter="10">
      <Col span="6">
        <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
             @click="$router.push({path:'/ilearning/jingpinCourse'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/common_img/course.jpg" style="width: 52px;height: 52px;"/>
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
             @click="$router.push({path:'/iblog/blogList'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/common_img/blog.jpg" style="width: 52px;height: 52px;"/>
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
             @click="$router.push({path:'/ibook/bookList'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/common_img/book.jpg" style="width: 52px;height: 52px;"/>
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
             @click="$router.push({path:'/site/siteIndex'})">
          <Row>
            <Col span="8">
              <img src="../../../static/images/common_img/more.jpg" style="width: 52px;height: 52px;"/>
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
