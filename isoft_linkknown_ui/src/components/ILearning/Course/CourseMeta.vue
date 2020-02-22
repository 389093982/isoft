<template>
  <span v-if="course">
    <p style="color: #d6241e;">
      浏览量：{{course.watch_number}}
      课程分数：<Rate disabled show-text allow-half :v-model="course.score"/>
      &nbsp; <span>如何提升得分？</span>
    </p>
    <p>
      课程名称：{{course.course_name}}
      <span class="isoft_hover_red" @click="$router.push({path:'/ilearning/index'})"
            style="margin-left: 30px;">搜索同类资源</span>
      <span class="isoft_hover_red" @click="$router.push($router.push({ path: '/ilearning/course_space'}))"
            style="margin-left: 30px;">我的课程空间</span>
      <span class="isoft_hover_red" @click="" style="margin-left: 30px;">开课流程</span>
    </p>
    <p>作者：
      <span v-if="user && renderNickName(course.course_author)">{{renderNickName(course.course_author)}}</span>
      <span v-else>{{course.course_author}}</span>
    </p>
    <p>课程类型：{{course.course_type}}</p>
    <p>课程子类型：{{course.course_sub_type}}</p>
    <p>课程简介：{{course.course_short_desc}}</p>
    <p>课程集数：{{course.course_number}}</p>
    <p v-if="course.course_label">标签语：
      <Tag v-for="(clabel, index) in clabels">{{clabel}}</Tag>
    </p>
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
    },
    data() {
      return {
        user_small_icon: '',
        user: null,
      }
    },
    methods: {
      RenderUserInfoByName: async function () {
        const result = await GetUserDetail(this.course.course_author);
        if (result.status === "SUCCESS") {
          this.user_small_icon = result.user.small_icon;
          this.user = result.user;
        }
      },
      renderNickName: function (user_name) {
        return !checkEmpty(this.user.nick_name) ? this.user.nick_name : user_name;
      }
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

</style>
