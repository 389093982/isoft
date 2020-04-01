<template>
  <span v-if="course">
    <p style="color: #d6241e;">
      浏览量 : {{course.watch_number}}  ,  评论数 : {{course.comments}}
    </p>
    <p>
      <span>
        <span style="color: #777">课程名称 : </span>
        <span class="courseDes">{{course.course_name}}</span>
      </span>
      <span class="isoft_hover_red" @click="$router.push({path:'/ilearning/jingpinCourse'})" style="margin-left: 30px;">
        搜索同类资源
      </span>
      <span class="isoft_hover_red" @click="$router.push($router.push({ path: '/user/userGuide'}))" style="margin-left: 30px;">
        开课流程
      </span>
    </p>
    <p>
      <span style="color: #777">作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者 : </span>
      <span class="courseDes" v-if="user && renderNickName(course.course_author)">{{renderNickName(course.course_author)}}</span>
    </p>
    <p>
      <span style="color: #777">课程类型 : </span>
      <span class="courseDes">{{course.course_type}} / {{course.course_sub_type}}</span>
    </p>
    <p>
      <span style="color: #777">课程简介 :  </span>
      <span class="courseDes" style="font-size: 12px">{{course.course_short_desc}}</span>
    </p>
    <p>
      <span style="color: #777">课程集数 : </span>
      <span class="courseDes">{{course.course_number}}</span>
    </p>
    <p v-if="course.course_label">
      <TagRender :tags="clabels"/>
    </p>

    <div style="width: 50%;float: right;margin-top: -30px;">
        <span v-if="showCourseSpace" class="isoft_button_blue"
              @click="$router.push($router.push({ path: '/ilearning/courseSpace'}))">进入我的课程空间</span>
    </div>
  </span>
</template>

<script>
  import IBeautifulLink from "../../Common/link/IBeautifulLink";
  import {GetUserDetail} from "../../../api"
  import {checkEmpty, strSplit} from "../../../tools"
  import TagRender from "../../Common/TagRender";

  export default {
    name: "CourseMeta",
    components: {TagRender, IBeautifulLink},
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
  .courseDes{
    color: #9b9896;
    cursor: pointer;
  }
  .courseDes:hover{
    color: #777;
    cursor: pointer;
  }

</style>
