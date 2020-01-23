<template>
  <div v-if="userName" style="border: 1px #dbdbdb solid;margin:2px 0 5px 5px;padding: 15px;">
    <IBeautifulLink>
      <Avatar :src="user_small_icon" icon="ios-person" size="default"/>&nbsp;
      <span v-if="nick_name">{{nick_name}}</span>
      <span v-else>{{userName}}</span>
    </IBeautifulLink>&nbsp;&nbsp;
    <IBeautifulLink style="font-size: 12px;float: right;"
      @onclick="$router.push({path:'/user/mine/detail',query:{username:'mine'}})">个人中心</IBeautifulLink>

    <div style="margin-top: 5px;">
      <Tabs :animated="false">
        <TabPane label="作者博文">
          <Row>
            <Col span="6">标题</Col>
            <Col span="6">分类</Col>
            <Col span="6">简介</Col>
            <Col span="6">时间</Col>
          </Row>
          <Row v-for="(blog,index) in blogs">
            <Col span="6" class="isoft_inline_ellipsis">
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
              <IBeautifulLink @onclick="$router.push({path:'/iblog/blog_detail',query:{blog_id:blog.id}})">
                {{blog.short_desc}}
              </IBeautifulLink>
            </Col>
            <Col span="6" class="isoft_inline_ellipsis">
              <Time :time="blog.last_updated_time" :interval="1" style="color:red;"/>
            </Col>
          </Row>
        </TabPane>
        <TabPane label="作者课程">
          <Row>
            <Col span="8">课程名称</Col>
            <Col span="8">课程类型</Col>
            <Col span="8">课程子类型</Col>
          </Row>
          <Row v-for="course in courses" :gutter="10">
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({path:'/ilearning/course_detail',query:{course_id:course.id}})">
                {{course.course_name}}
              </IBeautifulLink>
            </Col>
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({ path:'/ilearning/course_search', query: { search: course.course_type }})">
                {{course.course_type}}
              </IBeautifulLink>
            </Col>
            <Col span="8" class="isoft_inline_ellipsis">
              <IBeautifulLink @onclick="$router.push({ path: '/ilearning/course_search', query: { search: course.course_sub_type }})">
                {{course.course_sub_type}}
              </IBeautifulLink>
            </Col>
          </Row>
        </TabPane>
        <TabPane label="作者书单">
          <Row>
            <Col span="8">图书名称</Col>
            <Col span="8">课程类型</Col>
            <Col span="8">课程子类型</Col>
          </Row>
          <Row v-for="(book,index) in books">
            <Row>
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
  import {BookList, GetCourseListByUserName, GetUserDetail, queryPageBlog} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink"

  export default {
    name: "UserAbout",
    components: {IBeautifulLink},
    props:{
      userName: {
        type: String,
        default: ''
      },
    },
    data(){
      return {
        // 当前 userName 的书单列表
        books: [],
        // 当前 userName 的博客列表
        blogs: [],
        // 当前 userName 的课程列表
        courses:[],
        // 当前 user 对应头像信息
        user_small_icon:'',
        nick_name: '',
      }
    },
    methods:{
      refreshUserInfo:function () {
        this.refreshBookList();
        this.refreshBlogList();
        this.refreshCourseList();
        this.refreshUserDetail();
      },
      refreshUserDetail:async function(){
        const result = await GetUserDetail(this.userName);
        if(result.status == "SUCCESS"){
          this.user_small_icon = result.user.small_icon;
          this.nick_name = result.user.nick_name;
        }
      },
      refreshBookList: async function () {
        const result = await BookList({
          search_type: '',
          search_user_name: this.userName,
        });
        if (result.status == "SUCCESS") {
          this.books = result.books;
        }
      },
      refreshBlogList: async function () {
        const result = await queryPageBlog({
          offset: 20,
          current_page: 1,
          search_user_name: this.userName,
        });
        if (result.status == "SUCCESS") {
          this.blogs = result.blogs;
        }
      },
      refreshCourseList:async function () {
        const result = await GetCourseListByUserName(this.userName);
        if(result.status=="SUCCESS"){
          this.courses = result.courses;
        }
      },
    },
    watch:{
      "userName": "refreshUserInfo"      // 如果 userName 有变化,会再次执行该方法
    },
    mounted(){
      this.refreshUserInfo();
    }
  }
</script>

<style scoped>

</style>
