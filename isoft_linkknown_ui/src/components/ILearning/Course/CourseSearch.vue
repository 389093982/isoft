<template>
  <div>

    <div style="display: flex">
      <div style="width: 63%;background-color: white">
        <div class="isoft_bg_white isoft_pd10 isoft_mr10">
          <div
            style="width:100%;text-align: center;background: rgba(220,220,220,0.28);margin: 10px 0 10px 0;padding: 10px;cursor: pointer;">
            <p style="font-size: 20px;" class="hovered hvr-grow isoft_hover_red2">您搜索的课程是 :  "  {{$route.query.search}}  "</p>
          </div>

          <div v-if="courses.length > 0">
            <div style="border: 1px solid #f4f4f4;padding: 15px;margin-left: 5px;min-height: 500px;">
              <Row style="border-bottom: 1px solid #f4f4f4;padding: 10px;" v-for="course in courses">
                <Col span="8">
                  <router-link :to="{path:'/ilearning/courseDetail',query:{course_id:course.id}}">
                    <h4>课程名称：{{course.course_name}}</h4>
                    <img v-if="course.small_image" :src="course.small_image" height="120" width="180"/>
                    <img v-else src="../../../../static/images/common_img/default.png" height="120" width="180"/>
                  </router-link>
                </Col>
                <Col span="16">
                  <CourseMeta :course="course"/>
                </Col>
              </Row>
            </div>
          </div>
          <div v-else style="text-align: center;margin: 50px 0;">
            未找到 {{$route.query.search}} 的搜索结果,
            <IBeautifulLink font-weight="bold" @onclick="$router.push({path:'/ilearning/jingpinCourse'})">试试找找其它资源
            </IBeautifulLink>
          </div>
        </div>
      </div>
      <div style="width: 32%;background-color: white;margin-left: 5px">
        <WaitYourAnswer></WaitYourAnswer>
        <img style="margin-top: 5px" src="../../../../static/images/common_img/linkknown_tip_message.jpg" height="700" width=100%/>
      </div>
    </div>

  </div>
</template>

<script>
  import {SearchCourseList} from "../../../api"
  import CourseMeta from "./CourseMeta";
  import IBeautifulLink from "../../Common/link/IBeautifulLink";
  import WaitYourAnswer from "../../Expert/WaitYourAnswer";

  export default {
    name: "CourseSearch",
    components: {WaitYourAnswer, IBeautifulLink, CourseMeta},
    data() {
      return {
        courses: [],
      }
    },
    methods: {
      refreshCourseSearch: async function (search) {
        const result = await SearchCourseList({search});
        if (result.status == "SUCCESS") {
          this.courses = result.courses;
        }
      }
    },
    mounted: function () {
      // $route为当前router跳转对象里面可以获取name、path、query、params等
      // $router为VueRouter实例,想要导航到不同URL,则使用$router.push方法
      // 返回上一个history也是使用$router.go方法
      const search = this.$route.query.search;
      this.refreshCourseSearch(search);
    }
  }
</script>

<style scoped>
  a {
    color: black;
  }

  a:hover {
    color: red;
  }
</style>
