<template>
  <span v-if="course">
    <p style="color: #d6241e;">
      浏览量：{{course.watch_number}}
    </p>
    <p>
      <span>课程名称：{{course.course_name}}</span>
      <span class="isoft_hover_red" @click="$router.push({path:'/ilearning/boutiqueCourse'})"
            style="margin-left: 30px;">搜索同类资源</span>
      <span class="isoft_hover_red" @click="$router.push($router.push({ path: '/user/guide'}))"
            style="margin-left: 30px;">开课流程</span>
    </p>
    <p>作者：
      <span v-if="user && renderNickName(course.course_author)">{{renderNickName(course.course_author)}}</span>
    </p>
    <p>课程类型：{{course.course_type}}</p>
    <p>课程子类型：{{course.course_sub_type}}</p>
    <p>课程简介：{{course.course_short_desc}}</p>
    <p>课程集数：{{course.course_number}}</p>
    <p v-if="course.course_label">标签语：
      <Tag v-for="(clabel, index) in clabels">{{clabel}}</Tag>
    </p>

    <span v-if="showCourseSpace" class="isoft_hover_red forwardCourseSpace"
          @click="$router.push($router.push({ path: '/ilearning/course_space'}))">进入我的课程空间</span>
  </span>
</template>

<script>
  import IBeautifulLink from "../../Common/link/IBeautifulLink";
  import {GetUserDetail} from "../../../api"
  import {checkEmpty, strSplit} from "../../../tools"

  export default {
    name: "CourseMeta",
    components: {IBeautifulLink},
    props: {
      course: {
        type: Object,
        default: null,
      },
      showCourseSpace: {    // 是否显示我的课程空间
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        user: null,
      }
    },
    methods: {
      RenderUserInfoByName: async function () {
        const result = await GetUserDetail(this.course.course_author);
        if (result.status === "SUCCESS") {
          this.user = result.user;
        }
      },
      renderNickName: function (user_name) {
        return !checkEmpty(this.user.nick_name) ? this.user.nick_name : user_name;
      },
    },
    computed: {
      clabels: function () {
        return strSplit(this.course.course_label, "/").filter(label => !checkEmpty(label));
      }
    },
    mounted() {
      this.RenderUserInfoByName();
    }
  }
</script>

<style scoped>
  .forwardCourseSpace {
    background-color: #d6eecb;
    padding: 8px 12px;
    position: absolute;
    right: 10px;
    bottom: 10px;

    animation: move_dh_1 5s infinite;
  }

  @keyframes move_dh_1 {
    0% {
      transform: rotateY(0deg)
    }
    25% {
      transform: rotateY(10deg)
    }
    50% {
      transform: rotateY(20deg)
    }
    75% {
      transform: rotateY(10deg)
    }
    100% {
      transform: rotateY(0deg)
    }
  }
</style>
