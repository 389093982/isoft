<template>
  <div>
    <div v-if="myCourses && myCourses.length > 0">
      <Row v-for="myCourse in myCourses" style="border-bottom: 1px solid #f4f4f4;padding: 10px;">
        <Col span="8">
          <h4 class="isoft_inline_ellipsis">课程名称：{{myCourse.course_name}}</h4>
          <p>
            <img v-if="myCourse.small_image" :src="myCourse.small_image" height="120" width="180"/>
            <img v-else src="../../../assets/default.png" height="120" width="180"/>
          </p>
          <p>
            <span>
              <IFileUpload ref="fileUpload" :extra-data="myCourse.id" btn-size="small" :auto-hide-modal="true" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="更换封面"/>
            </span>
            <span style="margin-left: 10px">
              <span v-if="myCourse.isCharge==='charge'" style="color: #ff6900">
                <Icon type="logo-yen" />
                <span style="font-size: 20px">
                  {{myCourse.price}}
                </span>
              </span>
              <span v-else style="color: #cc0000;font-size: 15px">免费视频</span>
            </span>
          </p>
        </Col>
        <Col span="14">
          <CourseMeta :course="myCourse" :show-course-space="false"/>
          <p>
            <router-link :to="{path:'/ilearning/courseSpace/editCourse',query:{course_id:myCourse.id}}"
                         style="color:#1000db;font-family: Arial;font-weight: 700;">编辑课程信息
            </router-link>
            <router-link :to="{path:'/ilearning/courseDetail',query:{course_id:myCourse.id}}"
                         style="color:green;font-family: Arial;font-weight: 700;">查看视频详情
            </router-link>
            <UploadVideo :course="myCourse" @uploadComplete="uploadVideoComplete"/>
          </p>
        </Col>
      </Row>
      <div style="text-align: center;margin-top: 10px">
        <Page :total="totalCount" :page-size="offset" :current="current_page" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
      </div>
    </div>
    <div v-if="myCourses && myCourses.length == 0" style="padding: 30px 10px;">
      您还没有任何课程奥，如果你想传播你的知识，
      <IBeautifulLink font-weight="bold" @onclick="$router.push({path:'/ilearning/courseSpace/editCourse'})">
        请前去开课！
      </IBeautifulLink>
      <IBeautifulLink @onclick="">如何开课呢？</IBeautifulLink>
    </div>
    <Spin fix size="large" v-if="isLoading">
      <div class="isoft_loading"></div>
    </Spin>
  </div>
</template>

<script>
  import {EndUpdate, fileUploadUrl, GetCourseListByUserName, UpdateCourseIcon} from "../../../api"
  import UploadVideo from "../Course/UploadVideo"
  import {GetLoginUserName, handleSpecial} from "../../../tools"
  import CourseMeta from "../Course/CourseMeta";
  import IFileUpload from "../../Common/file/IFileUpload";

  export default {
    name: "MyCourseList",
    components: {IFileUpload, CourseMeta, UploadVideo},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=course&table_field=small_icon",
        isLoading: true,
        // 我的课程
        myCourses: '',
        totalCount:0,
        current_page:1,
        offset:10,
      }
    },
    methods: {
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          let uploadFilePath = data.fileServerPath;
          let courseId = data.extraData;
          const result = await UpdateCourseIcon(courseId, handleSpecial(uploadFilePath));
          if (result.status === "SUCCESS") {
            this.refreshMyCourseList();
          }
        }
      },
      refreshMyCourseList: async function () {
        this.isLoading = true;
        try {
          var userName = GetLoginUserName();
          const result = await GetCourseListByUserName({"userName":userName,"current_page":this.current_page,"offset":this.offset});
          if (result.status === "SUCCESS") {
            this.myCourses = result.courses;
            this.totalCount = result.paginator.totalcount;
          }
        } finally {
          this.isLoading = false;
        }
      },
      uploadVideoComplete: function () {
        this.refreshMyCourseList();
      },
      pageChange:function (page) {
        this.current_page = page;
        this.refreshMyCourseList()
      },
      pageSizeChange:function (pageSize) {
        this.offset = pageSize;
        this.refreshMyCourseList()
      },
    },
    mounted: function () {
      this.refreshMyCourseList();
    }
  }
</script>

<style scoped>
  @import "../../../assets/css/isoft_common.css";

</style>
