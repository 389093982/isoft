<template>
  <div v-if="getUserName()" style="margin:2px 0 5px 5px;padding: 15px;min-height: 350px;">
    <IBeautifulLink>
      <img style="cursor: pointer;border-radius: 50%;"
           width="30" height="30" :src="user_small_icon" @error="defImg()">
      <span v-if="nick_name">{{nick_name}}</span>
      <span v-else>{{getUserName()}}</span>
    </IBeautifulLink>&nbsp;&nbsp;
    <IBeautifulLink style="font-size: 12px;float: right;" @onclick="$router.push({path:'/user/detail'})">个人中心
    </IBeautifulLink>

    <div style="margin-top: 5px;">
      <Tabs :animated="false">
        <TabPane icon="ios-card-outline" label="作者博文">
          <Row>
            <Col span="12">标题</Col>
            <Col span="6">分类</Col>
            <Col span="6">时间</Col>
          </Row>
          <Row v-for="(blog,index) in blogs" style="line-height: 28px;height: 28px;">
            <Col span="12" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({path:'/iblog/blog_detail',query:{blog_id:blog.id}})">
                {{blog.blog_title}}
              </IBeautifulLink>
            </Col>
            <Col span="6" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({path:'/iblog/blog_detail',query:{blog_id:blog.id}})">
                {{blog.catalog_name}}
              </IBeautifulLink>
            </Col>
            <Col span="6" class="isoft_inline_ellipsis">
              <Time :time="blog.last_updated_time" :interval="1" style="color:red;"/>
            </Col>
          </Row>
        </TabPane>
        <TabPane icon="ios-videocam-outline" label="作者课程">
          <Row>
            <Col span="8">课程名称</Col>
            <Col span="8">课程类型</Col>
            <Col span="8">课程子类型</Col>
          </Row>
          <Row v-for="course in courses" :gutter="10" style="line-height: 28px;height: 28px;">
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({path:'/ilearning/course_detail',query:{course_id:course.id}})">
                {{course.course_name}}
              </IBeautifulLink>
            </Col>
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink
                @onclick="$router.push({ path:'/ilearning/course_search', query: { search: course.course_type }})">
                {{course.course_type}}
              </IBeautifulLink>
            </Col>
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink
                @onclick="$router.push({ path: '/ilearning/course_search', query: { search: course.course_sub_type }})">
                {{course.course_sub_type}}
              </IBeautifulLink>
            </Col>
          </Row>
        </TabPane>
        <TabPane icon="ios-book-outline" label="作者书单">
          <Row>
            <Col span="8">图书名称</Col>
            <Col span="8">图书描述</Col>
            <Col span="8">时间</Col>
          </Row>
          <Row v-for="(book,index) in books">
            <Row style="line-height: 28px;height: 28px;">
              <Col span="8" class="isoft_inline_ellipsis">
                <IBeautifulLink @onclick="$router.push({path:'/ibook/book_detail',query:{book_id:book.id}})">
                  {{book.book_name}}
                </IBeautifulLink>
              </Col>
              <Col span="8" class="isoft_inline_ellipsis">{{book.book_desc}}</Col>
              <Col span="8">
                <Time :time="book.last_updated_time" :interval="1" style="color:red;"/>
              </Col>
            </Row>
          </Row>
        </TabPane>
      </Tabs>
    </div>
  </div>
</template>

<script>
  import {GetCourseListByUserName, GetUserDetail, queryPageBlog, QueryPageBookList} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink"
  import {checkEmpty, GetLoginUserName} from "../../tools"

  export default {
    name: "UserAbout",
    components: {IBeautifulLink},
    props: {
      userName: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        // 当前 userName 的书单列表
        books: [],
        // 当前 userName 的博客列表
        blogs: [],
        // 当前 userName 的课程列表
        courses: [],
        // 当前 user 对应头像信息
        user_small_icon: '',
        nick_name: '',
        defaultImg: require('../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      refreshUserInfo: function () {
        if (checkEmpty(this.userName) && checkEmpty(GetLoginUserName())) {
          return;
        }
        this.refreshQueryPageBookList();
        this.refreshBlogList();
        this.refreshCourseList();
        this.refreshUserDetail();
      },
      getUserName: function () {
        return !checkEmpty(this.userName) ? this.userName : GetLoginUserName();
      },
      refreshUserDetail: async function () {
        const result = await GetUserDetail(this.getUserName());
        if (result.status === "SUCCESS") {
          this.user_small_icon = result.user.small_icon;
          this.nick_name = result.user.nick_name;
        }
      },
      refreshQueryPageBookList: async function () {
        const result = await QueryPageBookList({
          search_type: '',
          search_user_name: this.getUserName(),
          current_page: 1,
          offset: 10,
        });
        if (result.status === "SUCCESS") {
          this.books = result.books;
        }
      },
      refreshBlogList: async function () {
        const result = await queryPageBlog({
          offset: 20,
          current_page: 1,
          search_user_name: this.getUserName(),
        });
        if (result.status === "SUCCESS") {
          this.blogs = result.blogs;
        }
      },
      refreshCourseList: async function () {
        const result = await GetCourseListByUserName({userName: this.getUserName(), current_page: 1, offset: 10});
        if (result.status === "SUCCESS") {
          this.courses = result.courses;
        }
      },
    },
    watch: {
      "userName": "refreshUserInfo"      // 如果 userName 有变化,会再次执行该方法
    },
    mounted() {
      this.refreshUserInfo();
    }
  }
</script>

<style scoped>

</style>
