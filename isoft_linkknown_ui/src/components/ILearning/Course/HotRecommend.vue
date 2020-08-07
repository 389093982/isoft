<template>
  <div>
    <!-- 列表形式显示 -->
    <div v-if="showMode==='list'">
      <IBeautifulCard title="热门课程推荐">
        <div slot="content" style="padding: 20px 50px 20px 20px;">
          <Row v-for="course in courses">
            <div @onclick="$router.push({path:'/ilearning/courseDetail',query:{course_id:course.id}})">
              <p class="isoft_hover_red isoft_inline_ellipsis">【热荐课程】&nbsp;{{course.course_name}}</p>
              <p class="isoft_hover_desc isoft_p3line" style="padding-left: 20px;">{{course.course_short_desc}}</p>
            </div>
          </Row>
        </div>
      </IBeautifulCard>
    </div>

    <!-- 图标形式显示 -->
    <IBeautifulCard v-else title="热门课程推荐">
      <div slot="content" style="min-height:350px;padding: 10px;">
        <ul class="clear">
          <li v-for="course in courses">
            <router-link :to="{path:'/ilearning/courseDetail',query:{course_id:course.id}}" style="position: relative">
              <img v-if="course.small_image" :src="course.small_image" height="100" width="155"/>
              <img v-else src="../../../../static/images/common_img/default.png" height="100" width="155"/>
              <!-- 播放图标 -->
              <div v-if="showDisplayIcon" class="ico_play" style="position: absolute;top: -65px;left: 50px;"></div>
              <p class="isoft_font12 isoft_inline_ellipsis">{{course.course_name}}</p>
              <span v-if="course.isCharge==='free'" class="isoft_free" style="position: absolute;top: -80px;left: 85px">免费</span>
              <span v-else-if="course.isCharge==='vip'" class="isoft_free" style="position: absolute;top: -80px;left: 85px">会员</span>
            </router-link>
          </li>
        </ul>

        <div class="isoft_font12 isoft_color_grey" style="text-align: center;margin: 20px 0 0 0;">
          提供 2019 年至 2020 年最新热门视频课程，每月定期更新，所有版本版权归 linkknown 所有
        </div>
      </div>
    </IBeautifulCard>
  </div>
</template>

<script>
  import IBeautifulCard from "../../Common/card/IBeautifulCard"
  import {GetHotCourseRecommend} from "../../../api"
  import IBeautifulLink from "../../Common/link/IBeautifulLink";


  export default {
    name: "HotRecommend",
    components: {IBeautifulLink, IBeautifulCard},
    props: {
      // 显示方式,支持 detail 和 list
      showMode: {
        type: [String],
        default: 'detail'
      },
      showDisplayIcon: {
        type: Boolean,
        default: false,
      }
    },
    data() {
      return {
        courses: [],
      }
    },
    methods: {
      refreshHotRecommend: async function () {
        const result = await GetHotCourseRecommend();
        if (result.status === "SUCCESS") {
          this.courses = result.courses;
        }
      }
    },
    mounted: function () {
      this.refreshHotRecommend();
    }
  }
</script>

<style scoped>
  a {
    color: black;
  }

  li {
    float: left;
    margin: 0 10px;
    padding: 10px 9px 0;
    width: 175px;
    border: 1px solid #FFFFFF;
    overflow: hidden;
    text-align: center;
    position: relative;
  }

  li:hover {
    background-color: #f4f4f4;
    border: 1px solid #d0cdd2;
  }

  li:hover a {
    color: red;
  }

  .ico_play {
    background: url(../../../../static/images/common_img/ico_play.png) no-repeat;
    position: absolute;
    top: 35px;
    left: 55px;
    width: 60px;
    height: 60px;
  }
</style>
